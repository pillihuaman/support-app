package pillihuaman.com.pe.support.Service.Implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.support.RequestResponse.dto.RespFileMetadata;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqQuotation;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.RespQuotation;
import pillihuaman.com.pe.support.Service.CommonService;
import pillihuaman.com.pe.support.Service.QuotationService;
import pillihuaman.com.pe.support.Service.mapper.QuotationMapper;
import pillihuaman.com.pe.support.foreing.NeuroIaFileStorageService;
import pillihuaman.com.pe.support.repository.bussiness.*;
import pillihuaman.com.pe.support.repository.bussiness.dao.QuotationDAO;
import pillihuaman.com.pe.support.repository.common.CommonDataDocument;
import pillihuaman.com.pe.support.repository.product.FileMetadata;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class QuotationServiceImpl implements QuotationService {

    private static final Logger logger = LoggerFactory.getLogger(QuotationServiceImpl.class);

    @Autowired
    private QuotationDAO quotationDAO;
    @Autowired
    private QuotationMapper quotationMapper;
    @Autowired
    private NeuroIaFileStorageService neuroIaFileStorageService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CommonService commonService;
    //private static final String DEFAULT_CONFIG_ID = "PRODUCTION_COSTS_SUBLIMADO_TEAM_FUTBOL";
    @Override
    public RespBase<RespQuotation> createQuotation(ReqQuotation reqDto, MultipartFile logoFile, List<MultipartFile> referenceImages, MyJsonWebToken jwt, String rawAuthToken) {
        logger.info("Iniciando la creación de una nueva cotización para el cliente: {}", reqDto.getClienteNombre());

        Quotation quotation = quotationMapper.toEntity(reqDto);
        quotation.setStatus("PENDING");

        ProductionCostConfig costConfig = loadProductionCostConfig(reqDto.getTipoCostoProduccion(),reqDto);
        QuotationTotals totals = calculateCompetitiveTotals(quotation, costConfig);
        quotation.setTotals(totals);

        Quotation savedQuotation = quotationDAO.saveQuotation(quotation, jwt);
        String quotationId = savedQuotation.getId().toHexString();
        logger.info("Cotización inicial creada con ID: {}. Preparando subida de archivos.", quotationId);

        handleFileUploads(savedQuotation, logoFile, referenceImages, rawAuthToken);

        Quotation finalQuotation = quotationDAO.saveQuotation(savedQuotation, jwt);

        // ▼▼▼ CORRECCIÓN ▼▼▼
        // Construir la respuesta correctamente
        RespBase<RespQuotation> response = new RespBase<>();
        response.setPayload(quotationMapper.toDto(finalQuotation));
        response.getStatus().setSuccess(true);
        return response;
    }


    @Override
    public RespBase<RespQuotation> updateQuotation(String id, ReqQuotation reqDto, MultipartFile logoFile, List<MultipartFile> referenceImages, List<String> filesToDelete, MyJsonWebToken jwt, String rawAuthToken) {
        logger.info("Iniciando actualización para la cotización ID: {}", id);

        // 1. Cargar la entidad existente de la BD.
        Quotation existingQuotation = quotationDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró la cotización con ID: " + id));

        // 2. Determinar qué archivos eliminar.
        // Comparamos los archivos en la BD con los que el frontend nos dice que deben existir.
        List<String> idsToDelete = determineFilesToDelete(existingQuotation, reqDto.getDesignDetails());
        handleFileDeletions(existingQuotation, idsToDelete, rawAuthToken);

        // 3. Actualizar los campos de texto y la lista de items.
        existingQuotation.getCustomerInfo().setContactName(reqDto.getClienteNombre());
        existingQuotation.getCustomerInfo().setContactEmail(reqDto.getClienteEmail());
        existingQuotation.getCustomerInfo().setContactPhone(reqDto.getClienteTelefono());
        existingQuotation.getDesignDetails().setDetailedDescription(reqDto.getDescripcionDetallada());
        existingQuotation.setItems(quotationMapper.mapItemsToEntity(reqDto.getItems()));
        existingQuotation.setAceptaTerminos(reqDto.isAceptaTerminos());

        // 4. Subir y añadir los nuevos archivos.
        handleFileUploads(existingQuotation, logoFile, referenceImages, rawAuthToken);

        // 5. Recalcular y guardar.
        ProductionCostConfig costConfig = loadProductionCostConfig(reqDto.getTipoCostoProduccion(), reqDto);
        QuotationTotals newTotals = calculateCompetitiveTotals(existingQuotation, costConfig);
        existingQuotation.setTotals(newTotals);

        Quotation updatedQuotation = quotationDAO.saveQuotation(existingQuotation, jwt);
        logger.info("Cotización ID: {} actualizada exitosamente.", id);

        RespBase<RespQuotation> response = new RespBase<>();
        response.setPayload(quotationMapper.toDto(updatedQuotation));
        response.getStatus().setSuccess(true);
        return response;
    }

    /**
     * ▼▼▼ NUEVO MÉTODO AUXILIAR ▼▼▼
     * Compara el estado actual en la BD con el estado deseado que envía el frontend
     * para determinar qué IDs de archivo deben ser eliminados.
     */
    private List<String> determineFilesToDelete(Quotation current, DesignDetails desiredState) {
        List<String> idsToDelete = new ArrayList<>();

        // Si no hay detalles de diseño en la BD, no hay nada que borrar.
        if (current.getDesignDetails() == null) {
            return idsToDelete;
        }

        // Caso del Logo
        FileMetadata currentLogo = current.getDesignDetails().getLogoFile();
        FileMetadata desiredLogo = (desiredState != null) ? desiredState.getLogoFile() : null;

        // Si antes había un logo pero ahora no se desea ninguno (o se reemplazará por uno nuevo), marcar para borrar.
        if (currentLogo != null && desiredLogo == null) {
            idsToDelete.add(currentLogo.getId());
        }

        // Caso de las Imágenes de Referencia
        List<FileMetadata> currentImages = current.getDesignDetails().getReferenceImages();
        List<FileMetadata> desiredImages = (desiredState != null) ? desiredState.getReferenceImages() : Collections.emptyList();

        if (currentImages != null && !currentImages.isEmpty()) {
            Set<String> desiredImageIds = desiredImages.stream()
                    .map(FileMetadata::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            for (FileMetadata currentImage : currentImages) {
                if (!desiredImageIds.contains(currentImage.getId())) {
                    idsToDelete.add(currentImage.getId());
                }
            }
        }

        return idsToDelete;
    }
    /**
     * Carga la configuración de costos de producción desde la base de datos.
     * Si no se encuentra una configuración con el ID especificado o si ocurre un error,
     * utiliza un objeto con valores de fallback por defecto.
     * @param configId El ID del documento de configuración (valor de 'tipoCostoProduccion').
     * @return Un objeto ProductionCostConfig con los valores a utilizar, NUNCA nulo.
     */
    private ProductionCostConfig loadProductionCostConfig(String requestedConfigId,ReqQuotation reqDto) {
        String configIdToLoad = (requestedConfigId != null && !requestedConfigId.trim().isEmpty())
                ? requestedConfigId
                : reqDto.getTipoCostoProduccion();

        Optional<ProductionCostConfig> configOpt = fetchConfigFromDb(configIdToLoad);

        if (configOpt.isPresent()) {
            return configOpt.get();
        }

        // Si la configuración solicitada no se encontró, intenta con la de por defecto como último recurso
        if (!reqDto.getTipoCostoProduccion().equals(configIdToLoad)) {
            logger.warn("Configuración solicitada '{}' no encontrada. Intentando con la configuración por defecto '{}'.", configIdToLoad, reqDto.getTipoCostoProduccion());
            configOpt = fetchConfigFromDb(reqDto.getTipoCostoProduccion());
            if (configOpt.isPresent()) {
                return configOpt.get();
            }
        }

        // Si llegamos aquí, ni la solicitada ni la de por defecto existen. Es un error crítico.
        String errorMessage = String.format(
                "¡Error Crítico de Configuración! No se pudo encontrar la configuración de costos por defecto con ID '%s' en la base de datos. La aplicación no puede realizar cálculos de precios.",
                reqDto.getTipoCostoProduccion()
        );
        logger.error(errorMessage);
        throw new IllegalStateException(errorMessage);
    }

    private Optional<ProductionCostConfig> fetchConfigFromDb(String configId) {
        try {
            return commonService.findById(configId)
                    .map(configDoc -> {
                        try {
                            logger.info("Configuración de costos con ID '{}' encontrada. Mapeando objeto anidado.", configId);
                            Object data = configDoc.getPayload();

                            // Paso 1: Convertir el objeto completo (CommonDataDocument) a un string JSON.
                            String jsonString = objectMapper.writeValueAsString(data);
                            logger.debug("JSON completo obtenido de la base de datos: {}", jsonString);

                            // Paso 2: Parsear el string JSON a un árbol de nodos genérico (JsonNode).
                            JsonNode rootNode = objectMapper.readTree(jsonString);

                            // Paso 3: Navegar al nodo anidado que nos interesa, que está bajo la clave "data".
                            JsonNode dataNode = rootNode.get("data");

                            // Importante: Verificar que el nodo "data" existe y no es nulo.
                            if (dataNode == null || dataNode.isNull()) {
                                logger.error("El documento de configuración con ID '{}' no contiene un campo 'data' válido.", configId);
                                return null;
                            }

                            // Paso 4: Ahora, convertir SOLAMENTE este nodo anidado a nuestra clase de configuración.
                            // El método treeToValue es perfecto para esto.
                            return objectMapper.treeToValue(dataNode, ProductionCostConfig.class);

                        } catch (JsonProcessingException e) {
                            logger.error("Error de procesamiento JSON al mapear la configuración con ID '{}'.", configId, e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull); // Filtra cualquier resultado nulo de los pasos anteriores.

        } catch (Exception e) {
            logger.error("Excepción general al intentar buscar la configuración con ID '{}'.", configId, e);
            return Optional.empty();
        }
    }
    @Override
    public RespBase<RespQuotation> getQuotationById(String id, String token) {
        Optional<Quotation> quotationOpt = quotationDAO.findById(id);

        if (quotationOpt.isEmpty()) {
            return new RespBase<>(null);
        }

        Quotation quotation = quotationOpt.get();
        enrichQuotationWithPresignedUrls(quotation, token);

        RespBase<RespQuotation> response = new RespBase<>();
        response.setPayload(quotationMapper.toDto(quotation));
        response.getStatus().setSuccess(true);
        return response;
    }
    @Override
    public RespBase<List<RespQuotation>> getAllQuotations(String token) {
        List<Quotation> quotations = quotationDAO.findAll();
        quotations.forEach(quotation -> enrichQuotationWithPresignedUrls(quotation, token));

        List<RespQuotation> dtos = quotations.stream()
                .map(quotationMapper::toDto)
                .collect(Collectors.toList());

        RespBase<List<RespQuotation>> response = new RespBase<>();
        response.setPayload(dtos);
        response.getStatus().setSuccess(true);
        return response;
    }
    @Override
    public RespBase<String> deleteQuotation(String id, MyJsonWebToken jwt, String rawAuthToken) {
        logger.info("Iniciando eliminación para la cotización ID: {}", id);
        Optional<Quotation> quotationOpt = quotationDAO.findById(id);

        if (quotationOpt.isEmpty()) {
            logger.warn("No se encontró la cotización con ID: {} para eliminar.", id);
            return new RespBase<>("No se pudo eliminar o no se encontró la cotización.");
        }
        Quotation quotation = quotationOpt.get();

        // Eliminar archivos asociados desde el servicio de almacenamiento
        handleFileDeletions(quotation, null, rawAuthToken);

        boolean isDeleted = quotationDAO.deleteById(id, jwt);
        String message = isDeleted ? "Cotización eliminada exitosamente." : "Falló la eliminación de la cotización.";
        RespBase<String> response = new RespBase<>(message);
        response.getStatus().setSuccess(isDeleted);

        logger.info("Proceso de eliminación para cotización {} finalizado con resultado: {}", id, message);
        return response;
    }
    private QuotationTotals calculateCompetitiveTotals(Quotation quotation, ProductionCostConfig costConfig) {
        String idCotizacion = quotation.getId() != null ? quotation.getId().toHexString() : "NUEVA";
        logger.info("Calculando totales para la cotización ID {} usando la configuración provista.", idCotizacion);

        BigDecimal garmentsSubtotal = BigDecimal.ZERO;
        BigDecimal totalProductionCost = BigDecimal.ZERO; // Para análisis interno
        int totalGarments = 0;

        for (QuotationItem item : quotation.getItems()) {
            BigDecimal unitProductionCost = calculateUnitProductionCost(item, costConfig);
            BigDecimal targetSalePrice = item.isFullSet()
                    ? costConfig.getTargetSalePriceFullSet()
                    : costConfig.getTargetSalePricePoloOnly();

            BigDecimal itemQuantity = new BigDecimal(item.getQuantity());

            garmentsSubtotal = garmentsSubtotal.add(targetSalePrice.multiply(itemQuantity));
            totalProductionCost = totalProductionCost.add(unitProductionCost.multiply(itemQuantity));
            totalGarments += item.getQuantity();
        }

        BigDecimal totalDesignCost = BigDecimal.ZERO;
        BigDecimal grandTotal = garmentsSubtotal.add(totalDesignCost);

        logger.info("Cálculo finalizado para cotización ID {}. Inversión Estimada: S/ {}, Cotización al Cliente: S/ {}",
                idCotizacion,
                totalProductionCost.setScale(2, RoundingMode.HALF_UP),
                grandTotal.setScale(2, RoundingMode.HALF_UP));

        return QuotationTotals.builder()
                .totalGarments(totalGarments)
                .garmentsSubtotal(garmentsSubtotal)
                .designTotal(totalDesignCost)
                .grandTotal(grandTotal)
                .build();
    }

    private BigDecimal calculateUnitProductionCost(QuotationItem item, ProductionCostConfig costConfig) {
        BigDecimal fabricConsumption;
        BigDecimal sewingCost;
        BigDecimal socksCost;

        if (item.isFullSet()) {
            fabricConsumption = costConfig.getFabricConsumptionFullSet();
            sewingCost = costConfig.getDefaultSewingCostForSet();
            socksCost = costConfig.getSocksPrice();
        } else {
            fabricConsumption = costConfig.getFabricConsumptionPoloOnly();
            sewingCost = costConfig.getDefaultSewingCostForSet().multiply(new BigDecimal("0.60"));
            socksCost = BigDecimal.ZERO;
        }

        BigDecimal fabricCostPerMeter = costConfig.getDefaultFabricPricePerKilo().divide(costConfig.getFabricYieldMetersPerKg(), 2, RoundingMode.HALF_UP);
        BigDecimal unitFabricCost = fabricConsumption.multiply(fabricCostPerMeter);
        BigDecimal unitSublimationCost = fabricConsumption.multiply(costConfig.getSublimationPricePerMeter());
        BigDecimal unitEmbroideryCost = BigDecimal.ZERO;

        return unitFabricCost
                .add(unitSublimationCost)
                .add(sewingCost)
                .add(unitEmbroideryCost)
                .add(socksCost)
                .add(costConfig.getMiscCostsPerGarment());
    }
    // --- MÉTODOS AUXILIARES (HELPERS) ---

    private void handleFileUploads(Quotation quotation, MultipartFile logoFile, List<MultipartFile> referenceImages, String rawAuthToken) {
        List<MultipartFile> allFilesToUpload = new ArrayList<>();
        List<Map<String, String>> allMetadata = new ArrayList<>();
        Map<Integer, String> fileIndexToTypeMap = new HashMap<>();
        int currentIndex = 0;

        if (logoFile != null && !logoFile.isEmpty()) {
            allFilesToUpload.add(logoFile);
            allMetadata.add(Map.of("typeFile", "quotation_logo"));
            fileIndexToTypeMap.put(currentIndex++, "logo");
        }
        if (referenceImages != null) {
            referenceImages.stream()
                    .filter(img -> img != null && !img.isEmpty())
                    .forEach(refImage -> {
                        allFilesToUpload.add(refImage);
                        allMetadata.add(Map.of("typeFile", "quotation_reference"));
                        fileIndexToTypeMap.put(allFilesToUpload.size() - 1, "reference");
                    });
        }

        if (!allFilesToUpload.isEmpty()) {
            try {
                logger.info("Subiendo {} archivos en lote para la cotización ID: {}", allFilesToUpload.size(), quotation.getId().toHexString());
                String metadataJson = objectMapper.writeValueAsString(allMetadata);
                RespBase<List<RespFileMetadata>> uploadResponse = neuroIaFileStorageService.uploadFilesToNeuroIA(
                        allFilesToUpload.toArray(new MultipartFile[0]), metadataJson, quotation.getId().toHexString(), rawAuthToken);

                if (uploadResponse.getStatus().getSuccess() && uploadResponse.getPayload() != null) {
                    processUploadResponse(quotation, uploadResponse.getPayload(), fileIndexToTypeMap);
                } else {
                    throw new RuntimeException("La subida de archivos a neuroIA falló: " + uploadResponse.getStatus().getError());
                }
            } catch (Exception e) {
                logger.error("Error crítico durante la subida de archivos para la cotización ID {}. La cotización se guardará sin los archivos.", quotation.getId().toHexString(), e);
                // La excepción no se relanza para permitir que el flujo principal continúe.
            }
        }
    }
    private void handleFileDeletions(Quotation quotation, List<String> explicitFilesToDelete, String rawAuthToken) {
        List<String> fileIdsToDelete = new ArrayList<>();
        if (explicitFilesToDelete != null) {
            fileIdsToDelete.addAll(explicitFilesToDelete);
        } else { // Si no se especifica, se eliminan todos los asociados (caso de deleteQuotation)
            if (quotation.getDesignDetails() != null) {
                if (quotation.getDesignDetails().getLogoFile() != null) {
                    fileIdsToDelete.add(quotation.getDesignDetails().getLogoFile().getId());
                }
                if (quotation.getDesignDetails().getReferenceImages() != null) {
                    quotation.getDesignDetails().getReferenceImages().stream()
                            .map(FileMetadata::getId)
                            .filter(Objects::nonNull)
                            .forEach(fileIdsToDelete::add);
                }
            }
        }

        if (!fileIdsToDelete.isEmpty()) {
            logger.info("Se eliminarán {} archivos para la cotización ID: {}", fileIdsToDelete.size(), quotation.getId().toHexString());
            fileIdsToDelete.forEach(fileId -> {
                try {
                    neuroIaFileStorageService.deleteFileFromNeuroIA(fileId, rawAuthToken);
                    if (quotation.getDesignDetails() != null) {
                        if (quotation.getDesignDetails().getLogoFile() != null && fileId.equals(quotation.getDesignDetails().getLogoFile().getId())) {
                            quotation.getDesignDetails().setLogoFile(null);
                        }
                        if (quotation.getDesignDetails().getReferenceImages() != null) {
                            quotation.getDesignDetails().getReferenceImages().removeIf(img -> fileId.equals(img.getId()));
                        }
                    }
                } catch (Exception e) {
                    logger.error("Error al eliminar el archivo ID {}. El proceso continuará.", fileId, e);
                }
            });
        }
    }

    private void processUploadResponse(Quotation quotation, List<RespFileMetadata> uploadedFiles, Map<Integer, String> indexToTypeMap) {
        if (quotation.getDesignDetails() == null) {
            quotation.setDesignDetails(new DesignDetails());
        }
        if (quotation.getDesignDetails().getReferenceImages() == null) {
            quotation.getDesignDetails().setReferenceImages(new ArrayList<>());
        }

        for (int i = 0; i < uploadedFiles.size(); i++) {
            FileMetadata fileEntity = mapDtoToEntity(uploadedFiles.get(i));
            String fileType = indexToTypeMap.get(i);
            if ("logo".equals(fileType)) {
                quotation.getDesignDetails().setLogoFile(fileEntity);
            } else if ("reference".equals(fileType)) {
                quotation.getDesignDetails().getReferenceImages().add(fileEntity);
            }
        }
    }

    private FileMetadata mapDtoToEntity(RespFileMetadata dto) {
        if (dto == null) return null;
        return FileMetadata.builder()
                .id(dto.getId())
                .filename(dto.getFilename())
                .url(dto.getUrl())
                .contentType(dto.getContentType())
                .size(dto.getSize())
                .typeFile(dto.getTypeFile())
                .build();
    }

    private void enrichQuotationWithPresignedUrls(Quotation quotation, String authToken) {
        if (quotation == null || quotation.getDesignDetails() == null) {
            return;
        }

        DesignDetails details = quotation.getDesignDetails();
        Stream<FileMetadata> allFiles = Stream.concat(
                details.getLogoFile() != null ? Stream.of(details.getLogoFile()) : Stream.empty(),
                details.getReferenceImages() != null ? details.getReferenceImages().stream() : Stream.empty()
        );

        allFiles.filter(Objects::nonNull)
                .forEach(file -> {
                    try {
                        RespBase<Map<String, Object>> response = neuroIaFileStorageService.getPresignedUrl(file.getId(), file.getTypeFile(), authToken);
                        if (response.getStatus().getSuccess() && response.getPayload() != null) {
                            file.setUrl((String) response.getPayload().get("url"));
                        }
                    } catch (Exception e) {
                        logger.error("Excepción al obtener URL pre-firmada para archivo ID {}.", file.getId(), e);
                    }
                });
    }
}