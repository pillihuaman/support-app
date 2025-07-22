package pillihuaman.com.pe.support.Service.Implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqQuotation;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.RespQuotation;
import pillihuaman.com.pe.support.Service.QuotationService;
import pillihuaman.com.pe.support.Service.mapper.QuotationMapper;
import pillihuaman.com.pe.support.foreing.NeuroIaFileStorageService;
import pillihuaman.com.pe.support.repository.bussiness.DesignDetails;
import pillihuaman.com.pe.support.repository.bussiness.Quotation;
import pillihuaman.com.pe.support.repository.bussiness.QuotationItem;
import pillihuaman.com.pe.support.repository.bussiness.QuotationTotals;
import pillihuaman.com.pe.support.repository.bussiness.dao.QuotationDAO;
import pillihuaman.com.pe.support.RequestResponse.dto.RespFileMetadata;
import pillihuaman.com.pe.support.repository.product.FileMetadata;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class QuotationServiceImpl implements QuotationService {

    private static final Logger logger = LoggerFactory.getLogger(QuotationServiceImpl.class);

    // ===================================================================================
    // <<< INICIO: REFACTORIZACIÓN CON LÓGICA DE COSTEO POR OBJETIVO >>>
    // ===================================================================================

    // --- ESTRATEGIA DE PRECIOS OBJETIVO (Para competir con Gamarra) ---
    private static final BigDecimal TARGET_SALE_PRICE_FULL_SET = new BigDecimal("50.00");
    private static final BigDecimal TARGET_SALE_PRICE_POLO_ONLY = new BigDecimal("35.00"); // Precio objetivo para solo polo
    private static final BigDecimal TARGET_SALE_PRICE_SHORT_ONLY = new BigDecimal("25.00"); // Precio objetivo para solo short

    // --- COSTOS DE PRODUCCIÓN INTERNOS (Estrategia "A favor tuyo") ---
    private static final BigDecimal DEFAULT_FABRIC_PRICE_PER_KILO = new BigDecimal("28.00");
    private static final BigDecimal DEFAULT_SEWING_COST_FOR_SET = new BigDecimal("6.00");

    // --- CONSTANTES DE PRODUCCIÓN ---
    private static final BigDecimal FABRIC_YIELD_METERS_PER_KG = new BigDecimal("3.60");
    private static final BigDecimal SUBLIMATION_PRICE_PER_METER = new BigDecimal("8.00");
    private static final BigDecimal SOCKS_PRICE = new BigDecimal("5.00");
    private static final BigDecimal MISC_COSTS_PER_GARMENT = new BigDecimal("2.00");
    private static final BigDecimal EMBROIDERY_PRICE_PER_LOGO = new BigDecimal("5.00");

    // --- CONSTANTES DE CONSUMO DE TELA (en metros) ---
    private static final BigDecimal FABRIC_CONSUMPTION_FULL_SET = new BigDecimal("2.0");
    private static final BigDecimal FABRIC_CONSUMPTION_POLO_ONLY = new BigDecimal("1.0");
    private static final BigDecimal FABRIC_CONSUMPTION_SHORT_ONLY = new BigDecimal("1.0");

    // ===================================================================================
    // <<< FIN: REFACTORIZACIÓN CON LÓGICA DE COSTEO POR OBJETIVO >>>
    // ===================================================================================

    @Autowired
    private QuotationDAO quotationDAO;
    @Autowired
    private QuotationMapper quotationMapper;
    @Autowired
    private NeuroIaFileStorageService neuroIaFileStorageService;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public RespBase<RespQuotation> createQuotation(ReqQuotation reqDto, MultipartFile logoFile, List<MultipartFile> referenceImages, MyJsonWebToken jwt, String rawAuthToken) {

        // --- PASO 1, 2, 3: Mapear, Calcular y Guardar la entidad inicial ---
        logger.info("Iniciando la creación de una nueva cotización.");
        Quotation quotation = quotationMapper.toEntity(reqDto);
        quotation.setStatus("PENDING");

        // <<< CAMBIO: Se llama al nuevo método de cálculo de totales >>>
        QuotationTotals totals = calculateCompetitiveTotals(quotation);
        quotation.setTotals(totals);

        Quotation savedQuotation = quotationDAO.saveQuotation(quotation, jwt);
        String quotationId = savedQuotation.getId().toHexString();
        logger.info("Cotización inicial creada con ID: {}. Preparando subida de archivos.", quotationId);

        // --- PASO 4: RECOLECTAR ARCHIVOS Y METADATOS PARA UNA ÚNICA LLAMADA ---
        List<MultipartFile> allFilesToUpload = new ArrayList<>();
        List<Map<String, String>> allMetadata = new ArrayList<>();
        // Mapa para rastrear la posición de cada tipo de archivo en la petición
        Map<Integer, String> fileIndexToTypeMap = new HashMap<>();
        int currentIndex = 0;

        if (logoFile != null && !logoFile.isEmpty()) {
            allFilesToUpload.add(logoFile);
            allMetadata.add(Map.of("typeFile", "quotation_logo"));
            fileIndexToTypeMap.put(currentIndex, "logo");
            currentIndex++;
        }
        if (referenceImages != null) {
            for (MultipartFile refImage : referenceImages) {
                if (refImage != null && !refImage.isEmpty()) {
                    allFilesToUpload.add(refImage);
                    allMetadata.add(Map.of("typeFile", "quotation_reference"));
                    fileIndexToTypeMap.put(currentIndex, "reference");
                    currentIndex++;
                }
            }
        }

        // --- PASO 5: REALIZAR LA SUBIDA EN LOTE (SI HAY ARCHIVOS) ---
        if (!allFilesToUpload.isEmpty()) {
            try {
                logger.info("Subiendo {} archivos en una sola llamada para la cotización ID: {}", allFilesToUpload.size(), quotationId);
                String metadataJson = objectMapper.writeValueAsString(allMetadata);

                RespBase<List<RespFileMetadata>> uploadResponse = neuroIaFileStorageService.uploadFilesToNeuroIA(
                        allFilesToUpload.toArray(new MultipartFile[0]), metadataJson, quotationId, rawAuthToken);

                if (uploadResponse.getStatus().getSuccess() && uploadResponse.getPayload() != null) {
                    // Usamos el mapa de índices para procesar la respuesta y asignar cada archivo correctamente
                    processUploadResponse(savedQuotation, uploadResponse.getPayload(), fileIndexToTypeMap);
                    logger.info("Archivos para la cotización {} subidos y procesados correctamente.", quotationId);
                } else {
                    throw new RuntimeException("La subida de archivos a neuroIA falló: " + uploadResponse.getStatus().getError());
                }
            } catch (Exception e) {
                logger.error("Error crítico durante la subida de archivos para la cotización ID {}. Se devolverá un éxito parcial.", quotationId, e);
                RespBase<RespQuotation> partialSuccessResponse = new RespBase<>(quotationMapper.toDto(savedQuotation));
                partialSuccessResponse.getStatus().setError(
                        RespBase.Status.Error.builder()
                                .code("FILE_UPLOAD_FAILED")
                                .messages(List.of("La cotización fue creada pero los archivos no pudieron ser procesados: " + e.getMessage()))
                                .build()
                );
                return partialSuccessResponse;
            }
        }

        // --- PASO 6 Y 7: GUARDAR LA VERSIÓN FINAL Y DEVOLVER ---
        logger.debug("Actualizando la cotización {} con los metadatos de los archivos.", quotationId);
        Quotation finalQuotation = quotationDAO.saveQuotation(savedQuotation, jwt);
        return new RespBase<>(quotationMapper.toDto(finalQuotation));
    }

    @Override
    public RespBase<RespQuotation> updateQuotation(String id, ReqQuotation reqDto, MultipartFile logoFile, List<MultipartFile> referenceImages, List<String> filesToDelete, MyJsonWebToken jwt, String rawAuthToken) {

        // PASO 1: OBTENER LA COTIZACIÓN EXISTENTE
        logger.info("Iniciando actualización para la cotización ID: {}", id);
        Optional<Quotation> existingQuotationOpt = quotationDAO.findById(id);
        if (existingQuotationOpt.isEmpty()) {
            logger.warn("No se encontró la cotización con ID: {} para actualizar.", id);
            return new RespBase<>(null); // O un RespBase con error
        }
        Quotation existingQuotation = existingQuotationOpt.get();

        // PASO 2: ELIMINAR ARCHIVOS MARCADOS
        if (filesToDelete != null && !filesToDelete.isEmpty()) {
            logger.info("Se eliminarán {} archivos para la cotización ID: {}", filesToDelete.size(), id);
            filesToDelete.forEach(fileId -> {
                try {
                    neuroIaFileStorageService.deleteFileFromNeuroIA(fileId, rawAuthToken);
                    if (existingQuotation.getDesignDetails().getLogoFile() != null && fileId.equals(existingQuotation.getDesignDetails().getLogoFile().getId())) {
                        existingQuotation.getDesignDetails().setLogoFile(null);
                    }
                    if (existingQuotation.getDesignDetails().getReferenceImages() != null) {
                        existingQuotation.getDesignDetails().getReferenceImages().removeIf(img -> fileId.equals(img.getId()));
                    }
                } catch (Exception e) {
                    logger.error("Error al eliminar el archivo ID {}. El proceso continuará.", fileId, e);
                }
            });
        }

        // PASO 3 Y 4: ACTUALIZAR DATOS DEL DTO Y RECALCULAR TOTALES
        quotationMapper.updateEntityFromDto(reqDto, existingQuotation);

        // <<< CAMBIO: Se recalcula con la nueva lógica >>>
        QuotationTotals newTotals = calculateCompetitiveTotals(existingQuotation);
        existingQuotation.setTotals(newTotals);

        // PASO 5: SUBIR NUEVOS ARCHIVOS EN UNA SOLA LLAMADA
        List<MultipartFile> allFilesToUpload = new ArrayList<>();
        List<Map<String, String>> allMetadata = new ArrayList<>();
        Map<Integer, String> fileIndexToTypeMap = new HashMap<>();
        int currentIndex = 0;

        if (logoFile != null && !logoFile.isEmpty()) {
            allFilesToUpload.add(logoFile);
            allMetadata.add(Map.of("typeFile", "quotation_logo"));
            fileIndexToTypeMap.put(currentIndex, "logo");
            currentIndex++;
        }
        if (referenceImages != null) {
            for (MultipartFile refImage : referenceImages) {
                if (refImage != null && !refImage.isEmpty()) {
                    allFilesToUpload.add(refImage);
                    allMetadata.add(Map.of("typeFile", "quotation_reference"));
                    fileIndexToTypeMap.put(currentIndex, "reference");
                    currentIndex++;
                }
            }
        }

        if (!allFilesToUpload.isEmpty()) {
            try {
                logger.info("Actualizando cotización {}: subiendo {} nuevos archivos en una sola llamada.", id, allFilesToUpload.size());
                String metadataJson = objectMapper.writeValueAsString(allMetadata);

                RespBase<List<RespFileMetadata>> uploadResponse = neuroIaFileStorageService.uploadFilesToNeuroIA(
                        allFilesToUpload.toArray(new MultipartFile[0]), metadataJson, id, rawAuthToken);

                if (uploadResponse.getStatus().getSuccess() && uploadResponse.getPayload() != null) {
                    processUploadResponse(existingQuotation, uploadResponse.getPayload(), fileIndexToTypeMap);
                } else {
                    throw new RuntimeException("La subida de nuevos archivos a neuroIA falló.");
                }
            } catch (Exception e) {
                logger.error("Error durante la subida de nuevos archivos para la cotización ID {}.", id, e);
            }
        }

        // PASO 6: GUARDAR LA ENTIDAD ACTUALIZADA Y DEVOLVER
        Quotation updatedQuotation = quotationDAO.saveQuotation(existingQuotation, jwt);
        logger.info("Cotización ID: {} actualizada exitosamente.", id);
        return new RespBase<>(quotationMapper.toDto(updatedQuotation));
    }

    private void processUploadResponse(Quotation quotation, List<RespFileMetadata> uploadedFiles, Map<Integer, String> indexToTypeMap) {
        if (quotation.getDesignDetails() == null) {
            quotation.setDesignDetails(new DesignDetails());
        }
        if (quotation.getDesignDetails().getReferenceImages() == null) {
            quotation.getDesignDetails().setReferenceImages(new ArrayList<>());
        }

        for (int i = 0; i < uploadedFiles.size(); i++) {
            RespFileMetadata uploadedFileDto = uploadedFiles.get(i);
            FileMetadata fileEntity = mapDtoToEntity(uploadedFileDto);
            String fileType = indexToTypeMap.get(i);

            if ("logo".equals(fileType)) {
                quotation.getDesignDetails().setLogoFile(fileEntity);
            } else if ("reference".equals(fileType)) {
                quotation.getDesignDetails().getReferenceImages().add(fileEntity);
            }
        }
    }


    private List<RespFileMetadata> uploadFilesToNeuroIA(MultipartFile[] files, String typeFile, String entityId, String authToken) throws JsonProcessingException {
        String metadataJson = objectMapper.writeValueAsString(
                Collections.singletonList(Map.of("typeFile", typeFile))
        );
        RespBase<List<RespFileMetadata>> response = neuroIaFileStorageService.uploadFilesToNeuroIA(files, metadataJson, entityId, authToken);
        if (response != null && response.getStatus() != null && Boolean.TRUE.equals(response.getStatus().getSuccess())) {
            return response.getPayload();
        } else {
            String errorMessage = (response != null && response.getStatus() != null && response.getStatus().getError() != null)
                    ? response.getStatus().getError().toString()
                    : "Unknown error from file service.";
            logger.error("Failed to upload files via neuroIA service. Reason: {}", errorMessage);
            return Collections.emptyList();
        }
    }


    // ===================================================================================
    // <<< INICIO: LÓGICA DE CÁLCULO DE PRECIOS REFACTORIZADA >>>
    // ===================================================================================

    /**
     * REEMPLAZA el antiguo `calculateAndSetTotals`.
     * Este método calcula la cotización final basándose en el PRECIO DE VENTA OBJETIVO,
     * no en costos fijos.
     * @param quotation La entidad de cotización a procesar.
     * @return Un objeto QuotationTotals con los montos finales para el cliente.
     */
    private QuotationTotals calculateCompetitiveTotals(Quotation quotation) {
        logger.info("Calculando totales para la cotización ID {} con la nueva lógica de 'costeo por objetivo'.", quotation.getId());

        BigDecimal garmentsSubtotal = BigDecimal.ZERO;
        BigDecimal totalProductionCost = BigDecimal.ZERO; // Para análisis interno (inversión)
        int totalGarments = 0;

        for (QuotationItem item : quotation.getItems()) {
            BigDecimal unitProductionCost = calculateUnitProductionCost(item);

            BigDecimal targetSalePrice;
            if (item.isFullSet()) {
                targetSalePrice = TARGET_SALE_PRICE_FULL_SET;
            } else {
                // Asumimos que si no es conjunto, es solo polo. Se podría expandir si hay shorts solos.
                targetSalePrice = TARGET_SALE_PRICE_POLO_ONLY;
            }

            BigDecimal itemQuantity = new BigDecimal(item.getQuantity());

            garmentsSubtotal = garmentsSubtotal.add(targetSalePrice.multiply(itemQuantity));
            totalProductionCost = totalProductionCost.add(unitProductionCost.multiply(itemQuantity));
            totalGarments += item.getQuantity();
        }

        BigDecimal totalDesignCost = BigDecimal.ZERO; // El costo de diseño ya está implícito en el precio objetivo
        BigDecimal grandTotal = garmentsSubtotal.add(totalDesignCost);

        logger.info("Cálculo finalizado para cotización ID {}. Inversión Total Estimada: S/ {}, Cotización Total al Cliente: S/ {}",
                quotation.getId(),
                totalProductionCost.setScale(2, RoundingMode.HALF_UP),
                grandTotal.setScale(2, RoundingMode.HALF_UP));

        return QuotationTotals.builder()
                .totalGarments(totalGarments)
                .garmentsSubtotal(garmentsSubtotal)
                .designTotal(totalDesignCost)
                .grandTotal(grandTotal)
                .build();
    }

    /**
     * Método auxiliar que calcula el costo de producción para UNA SOLA unidad (polo o conjunto).
     * Utiliza las constantes de costos internos para el cálculo.
     * @param item El ítem específico del pedido (contiene isFullSet).
     * @return El costo de producción como un BigDecimal.
     */
    private BigDecimal calculateUnitProductionCost(QuotationItem item) {
        BigDecimal fabricConsumption;
        BigDecimal sewingCost;
        BigDecimal socksCost;

        if (item.isFullSet()) {
            fabricConsumption = FABRIC_CONSUMPTION_FULL_SET;
            sewingCost = DEFAULT_SEWING_COST_FOR_SET;
            socksCost = SOCKS_PRICE;
        } else {
            fabricConsumption = FABRIC_CONSUMPTION_POLO_ONLY;
            sewingCost = DEFAULT_SEWING_COST_FOR_SET.multiply(new BigDecimal("0.60"));
            socksCost = BigDecimal.ZERO;
        }

        BigDecimal fabricCostPerMeter = DEFAULT_FABRIC_PRICE_PER_KILO.divide(FABRIC_YIELD_METERS_PER_KG, 2, RoundingMode.HALF_UP);
        BigDecimal unitFabricCost = fabricConsumption.multiply(fabricCostPerMeter);
        BigDecimal unitSublimationCost = fabricConsumption.multiply(SUBLIMATION_PRICE_PER_METER);

        // La lógica de bordado podría añadirse aquí si se incluyera en el 'Item'
        BigDecimal unitEmbroideryCost = BigDecimal.ZERO;

        return unitFabricCost
                .add(unitSublimationCost)
                .add(sewingCost)
                .add(unitEmbroideryCost)
                .add(socksCost)
                .add(MISC_COSTS_PER_GARMENT);
    }

    // ===================================================================================
    // <<< FIN: LÓGICA DE CÁLCULO DE PRECIOS REFACTORIZADA >>>
    // ===================================================================================


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

    @Override
    public RespBase<RespQuotation> getQuotationById(String id, String token) {
        Optional<Quotation> quotationOpt = quotationDAO.findById(id);

        if (quotationOpt.isEmpty()) {
            return new RespBase<>(null);
        }

        Quotation quotation = quotationOpt.get();
        enrichQuotationWithPresignedUrls(quotation, token);

        return new RespBase<>(quotationMapper.toDto(quotation));
    }

    @Override
    public RespBase<List<RespQuotation>> getAllQuotations(String token) {
        List<Quotation> quotations = quotationDAO.findAll();
        quotations.forEach(quotation -> enrichQuotationWithPresignedUrls(quotation, token));

        List<RespQuotation> dtos = quotations.stream()
                .map(quotationMapper::toDto)
                .collect(Collectors.toList());
        return new RespBase<>(dtos);
    }

    @Override
    public RespBase<String> deleteQuotation(String id, MyJsonWebToken jwt, String rawAuthToken) {
        logger.info("Iniciando el proceso de eliminación para la cotización con ID: {}", id);
        Optional<Quotation> quotationOpt = quotationDAO.findById(id);

        if (quotationOpt.isEmpty()) {
            logger.warn("No se encontró la cotización con ID: {} para eliminar.", id);
            return new RespBase<>("Failed to delete quotation or quotation not found.");
        }
        Quotation quotation = quotationOpt.get();

        List<String> fileIdsToDelete = new ArrayList<>();
        if (quotation.getDesignDetails() != null) {
            if (quotation.getDesignDetails().getLogoFile() != null && quotation.getDesignDetails().getLogoFile().getId() != null) {
                fileIdsToDelete.add(quotation.getDesignDetails().getLogoFile().getId());
            }
            if (quotation.getDesignDetails().getReferenceImages() != null) {
                quotation.getDesignDetails().getReferenceImages().stream()
                        .filter(file -> file.getId() != null)
                        .forEach(file -> fileIdsToDelete.add(file.getId()));
            }
        }

        if (!fileIdsToDelete.isEmpty()) {
            logger.info("Se encontraron {} archivos asociados a la cotización {}. Procediendo a eliminarlos.", fileIdsToDelete.size(), id);
            fileIdsToDelete.forEach(fileId -> {
                try {
                    logger.debug("Solicitando la eliminación del archivo con ID: {}", fileId);
                    neuroIaFileStorageService.deleteFileFromNeuroIA(fileId, rawAuthToken);
                } catch (Exception e) {
                    logger.error("Error al solicitar la eliminación del archivo ID {}. El proceso continuará.", fileId, e);
                }
            });
        } else {
            logger.info("La cotización con ID: {} no tiene archivos asociados para eliminar.", id);
        }

        boolean isDeleted = quotationDAO.deleteById(id, jwt);

        String message = isDeleted
                ? "Quotation deleted successfully."
                : "Failed to delete quotation from database.";

        RespBase<String> response = new RespBase<>(message);
        response.getStatus().setSuccess(isDeleted);

        logger.info("Proceso de eliminación para la cotización {} finalizado con resultado: {}", id, message);
        return response;
    }

    private void enrichQuotationWithPresignedUrls(Quotation quotation, String authToken) {
        if (quotation == null || quotation.getDesignDetails() == null) {
            return;
        }

        logger.debug("Enriqueciendo la cotización ID {} con URLs pre-firmadas.", quotation.getId().toHexString());

        DesignDetails details = quotation.getDesignDetails();

        Stream<FileMetadata> allFiles = Stream.concat(
                details.getLogoFile() != null ? Stream.of(details.getLogoFile()) : Stream.empty(),
                details.getReferenceImages() != null ? details.getReferenceImages().stream() : Stream.empty()
        );

        allFiles.filter(Objects::nonNull)
                .filter(file -> file.getId() != null && file.getTypeFile() != null)
                .forEach(file -> {
                    try {
                        RespBase<Map<String, Object>> response = neuroIaFileStorageService.getPresignedUrl(
                                file.getId(), // Asumiendo que el ID es la S3 Key
                                file.getTypeFile(),
                                authToken
                        );

                        if (response.getStatus().getSuccess() && response.getPayload() != null) {
                            String presignedUrl = (String) response.getPayload().get("url");
                            if (presignedUrl != null) {
                                file.setUrl(presignedUrl);
                                logger.trace("URL actualizada para el archivo ID {}: {}", file.getId(), presignedUrl);
                            }
                        } else {
                            logger.warn("No se pudo obtener la URL pre-firmada para el archivo ID {}. Se mantendrá la URL existente.", file.getId());
                        }
                    } catch (Exception e) {
                        logger.error("Excepción al intentar obtener la URL pre-firmada para el archivo ID {}.", file.getId(), e);
                    }
                });
    }
}