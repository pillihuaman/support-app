package pillihuaman.com.pe.support.Service.Implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.common.ReqBase;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.support.RequestResponse.RespProduct;
import pillihuaman.com.pe.support.RequestResponse.dto.Mapper.MapperProduct;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqProduct;
import pillihuaman.com.pe.support.RequestResponse.dto.RespFileMetadata;
import pillihuaman.com.pe.support.Service.ProductService;
import pillihuaman.com.pe.support.Service.SupplierService;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqSupplier;
import pillihuaman.com.pe.support.RequestResponse.dto.RespSupplier;
import pillihuaman.com.pe.support.foreing.NeuroIaFileStorageService;
import pillihuaman.com.pe.support.repository.product.FileMetadata;
import pillihuaman.com.pe.support.repository.product.Product;
import pillihuaman.com.pe.support.repository.product.dao.ProductDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private MapperProduct mapperProduct;
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    private NeuroIaFileStorageService neuroIaFileStorageService;
    @Autowired
    private ObjectMapper objectMapper;

    // Método para guardar o actualizar un Product
    @Override
    public RespBase<RespProduct> saveProduct(MyJsonWebToken jwt, ReqProduct reqProduct, List<MultipartFile> images, String rawAuthToken) {

        // --- PASO 1: MAPEAR DTO A ENTIDAD Y GUARDADO INICIAL ---
        // Se convierte el DTO del request a la entidad que se persistirá.
        Product productToSave = mapperProduct.toProduct(reqProduct);

        // Se realiza un primer guardado. Si el producto es nuevo, se le asigna un ID.
        // Si ya existe (el DTO tenía un ID), se actualizan los campos básicos.
        Product savedProduct = productDAO.saveProduct(productToSave, jwt);
        String productId = savedProduct.getId().toHexString();
        logger.info("Producto guardado/actualizado inicialmente con ID: {}. Procediendo a la subida de archivos.", productId);

        // --- PASO 2: SUBIR NUEVOS ARCHIVOS (SI SE PROPORCIONARON) ---
        if (images != null && !images.isEmpty()) {
            try {
                logger.debug("Subiendo {} nuevas imágenes para el producto ID: {}", images.size(), productId);

                // Se crea un JSON de metadata simple para enviar al servicio de archivos.
                List<Map<String, String>> metadataListForService = images.stream()
                        .map(file -> Map.of("typeFile", "CATALOG"))
                        .collect(Collectors.toList());
                String metadataJson = objectMapper.writeValueAsString(metadataListForService);

                // Se llama al servicio externo (neuroIA) para subir los archivos.
                RespBase<List<RespFileMetadata>> fileUploadResponse = neuroIaFileStorageService.uploadFilesToNeuroIA(
                        images.toArray(new MultipartFile[0]),
                        metadataJson,
                        productId,
                        rawAuthToken
                );

                // --- PASO 3: PROCESAR LA RESPUESTA DEL SERVICIO DE ARCHIVOS ---
                if (fileUploadResponse.getStatus().getSuccess() && fileUploadResponse.getPayload() != null) {
                    List<FileMetadata> newFileMetadata = fileUploadResponse.getPayload().stream()
                            .map(this::mapDtoToEntity)
                            .collect(Collectors.toList());

                    // 🔽 Aquí seteas la posición
                    newFileMetadata.forEach(file -> file.setPosition("ASIGNADA"));

                    if (savedProduct.getFileMetadata() == null) {
                        savedProduct.setFileMetadata(new ArrayList<>());
                    }

                    savedProduct.getFileMetadata().addAll(newFileMetadata);
                    logger.info("{} nuevos metadatos de archivo han sido asociados al producto {}.", newFileMetadata.size(), productId);

                } else {
                    logger.error("La subida de archivos para el producto {} falló, pero el guardado del producto continuará. Razón: {}", productId, fileUploadResponse.getStatus().getError());
                    // Dependiendo de las reglas de negocio, se podría lanzar una excepción aquí para abortar la operación.
                }

            } catch (Exception e) {
                logger.error("Error crítico durante el proceso de subida de archivos para el producto {}. El producto fue guardado sin las nuevas imágenes.", productId, e);
                // Aquí también se podría lanzar una excepción para abortar y revertir la transacción si fuera necesario.
            }
        }

        // --- PASO 4: GUARDADO FINAL ---
        // Se vuelve a guardar el producto. Esta vez, la entidad contiene la lista `fileMetadata` actualizada.
        // MongoDB reemplazará el documento existente con esta nueva versión.
        logger.debug("Realizando guardado final para el producto {} para persistir la metadata de archivos.", productId);
        Product finalProduct = productDAO.saveProduct(savedProduct, jwt);

        // --- PASO 5: DEVOLVER LA RESPUESTA FINAL ---
        // Se mapea la entidad final y completa al DTO de respuesta que verá el cliente.
        RespProduct respProduct = mapperProduct.toRespProduct(finalProduct);
        return new RespBase<>(respProduct);
    }

    /**
     * Método de utilidad para convertir el DTO de respuesta del servicio de archivos
     * a la entidad `FileMetadata` que se persiste dentro del documento `Product`.
     */
    private FileMetadata mapDtoToEntity(RespFileMetadata dto) {
        if (dto == null) return null;
        return FileMetadata.builder()
                .id(dto.getId())
                .filename(dto.getFilename())
                .url(dto.getUrl())
                .contentType(dto.getContentType())
                .size(dto.getSize())
                .position(dto.getPosition())
                .typeFile(dto.getTypeFile())
                .build();
    }


    // Método para obtener un Product específico
    @Override
    public RespBase<List<RespProduct>> getProduct(MyJsonWebToken jwt, ReqBase<ReqProduct> request) {
        List<Product> products = productDAO.listProducts(request.getData());
        if (products.isEmpty()) {
            return new RespBase<>(null);
        }
        List<RespProduct> responseList = products.stream().map(product -> {
            RespProduct resp = mapperProduct.toRespProduct(product);
            // Si hay supplierId, busca el nombre y lo asigna
            if (resp.getSupplierId() != null && !resp.getSupplierId().isEmpty()) {
                ReqSupplier reqSupplier = ReqSupplier.builder()
                        .id(resp.getSupplierId())
                        .build();

                List<RespSupplier> suppliers = supplierService.listSuppliers(reqSupplier);
                if (!suppliers.isEmpty()) {
                    resp.setSupplierName(suppliers.get(0).getName());
                }
            }

            return resp;
        }).toList();

        return new RespBase<>(responseList);
    }

    // Método para listar Products asociados a un usuario
    @Override
    public RespBase<RespProduct> listProductsByUser(MyJsonWebToken jwt, ReqBase<ReqProduct> request) {
        // Obtener la carga útil de la solicitud
        ReqProduct reqProduct = request.getData();
        List<Product> Products = productDAO.findByUserId(reqProduct.getId());
        RespBase<List<RespProduct>> res = new RespBase<>(null);
        return null;
    }

    @Override
    public RespBase<Boolean> deleteProduct(MyJsonWebToken jwt, String id) {
        boolean deleted = productDAO.deleteInactiveProduct(jwt, id);
        return new RespBase<>(deleted);
    }

    @Override
    public RespBase<List<RespProduct>> searchProductsByKeywords(String keywordsString, int limit) {
        try {
            // 1. Llamada al DAO (la lógica del DAO no cambia, sigue devolviendo List<Product>)
            List<Product> foundProducts = productDAO.searchProductsByKeywords(keywordsString, limit);

            // 2. Mapeo de Entidades a DTOs
            List<RespProduct> responseList = foundProducts.stream()
                    .map(mapperProduct::toRespProduct)
                    .collect(Collectors.toList());

            // 3. Construcción de la respuesta RespBase exitosa
            return RespBase.<List<RespProduct>>builder()
                    .payload(responseList)
                    .status(new RespBase.Status(true, null)) // success = true, error = null
                    .build();

        } catch (Exception e) {
            RespBase.Status.Error error = RespBase.Status.Error.builder()
                    .code("E5001") // Código de error interno, ej: E = Error, 500 = Servidor, 1 = Primer error definido
                    .httpCode("500") // El código HTTP como un simple String. El controller lo interpretará.
                    .messages(List.of("Ocurrió un error interno al procesar la búsqueda de productos.")) // Mensaje genérico para el cliente
                    .build();

            RespBase.Status status = new RespBase.Status(false, error);

            return RespBase.<List<RespProduct>>builder()
                    .status(status)
                    .payload(null) // En caso de error, el payload es nulo.
                    .build();
        }
    }
}

