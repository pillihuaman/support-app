package pillihuaman.com.pe.support.Service.Implement;

import com.mongodb.client.model.Filters;
import jakarta.servlet.http.HttpServletRequest;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.common.ReqBase;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.lib.common.RespBase.Status;
import pillihuaman.com.pe.lib.common.RespBase.Trace;
import pillihuaman.com.pe.support.RequestResponse.RespProduct;
import pillihuaman.com.pe.support.RequestResponse.dto.Mapper.MapperProduct;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqProduct;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqProductView;
import pillihuaman.com.pe.support.RequestResponse.dto.RespFileMetadata;
import pillihuaman.com.pe.support.RequestResponse.dto.RespImagenProductRank;
import pillihuaman.com.pe.support.RequestResponse.dto.RespProductView;
import pillihuaman.com.pe.support.Service.ProductViewService;
import pillihuaman.com.pe.support.foreing.ExternalApiService;
import pillihuaman.com.pe.support.repository.product.Product;
import pillihuaman.com.pe.support.repository.product.ProductView;
import pillihuaman.com.pe.support.repository.product.dao.ProducViewtDAO;
import pillihuaman.com.pe.support.repository.product.dao.ProductDAO;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProductViewServiceImpl implements ProductViewService {

    @Autowired
    private ProducViewtDAO productViewDAO;
    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private MapperProduct mapperProduct;
    @Autowired
    private ExternalApiService externalApiService;

    @Autowired
    private HttpServletRequest httpServletRequest;
    private static final Logger log = LoggerFactory.getLogger(ProductViewServiceImpl.class);
    @Override
    public RespBase<RespProductView> saveView(MyJsonWebToken jwt, ReqBase<ReqProductView> request) {
        ReqProductView req = request.getPayload();
        ProductView view = ProductView.builder()
                .productId(new ObjectId(req.getProductId()))
                .fileId(req.getFileId() != null ? new ObjectId(req.getFileId()) : null)
                .userId(req.getUserId())
                .viewedAt(new Date())
                .build();
        productViewDAO.saveView(view);

        RespBase<RespProductView> response = new RespBase<>();
        response.setPayload(RespProductView.builder()
                .id(view.getId().toHexString())
                .productId(view.getProductId().toHexString())
                .fileId(view.getFileId() != null ? view.getFileId().toHexString() : null)
                .userId(view.getUserId())
                .build());

        response.setStatus(Status.builder().success(true).build());
        response.setTrace(new Trace(jwt.getId()));
        return response;
    }

    @Override
    //@Cacheable(value = "productosPopulares")
    public RespBase<List<RespImagenProductRank>> getViews() {
        // --- PASO 1: OBTENER LA LISTA DE PRODUCTOS (DESDE LA CACHÉ O LA BD) ---
        // Llamamos a nuestro nuevo método privado. La primera vez será lento.
        // Las siguientes veces, esta línea se ejecutará en milisegundos.
        List<Product> productsToEnrich = null;

            productsToEnrich =productDAO.listProducts(new ReqProduct());


        if (productsToEnrich.isEmpty()) {
            // Si no hay productos, devolvemos una respuesta vacía.
            RespBase<List<RespImagenProductRank>> emptyResponse = new RespBase<>();
            emptyResponse.setPayload(Collections.emptyList());
            emptyResponse.setStatus(Status.builder().success(true).build());
            return emptyResponse;
        }
        // --- PASO 2: RECOLECTAR IDs (Lógica no cacheable) ---
        List<String> productIdsToFetchImages = productsToEnrich.stream()
                .map(product -> product.getId().toHexString())
                .collect(Collectors.toList());

        // --- PASO 3: OBTENER URLs FRESCAS (Lógica no cacheable) ---
        // Esta llamada a neuro-ia se hará en CADA petición, pero es rápida
        // porque neuro-ia tiene su propia caché para los metadatos.
        String authToken = httpServletRequest.getHeader("Authorization");

        List<RespFileMetadata> allImages = externalApiService.getImagesByProductIds(productIdsToFetchImages, authToken);

        // --- PASO 4: MAPEAR Y ENRIQUECER (Lógica no cacheable) ---
        Map<String, List<RespFileMetadata>> imagesByProductId = allImages.stream()
                .peek(meta -> {
                    // Para cada metadato, llama al servicio ultrarrápido para obtener su URL.
                  //  String signedUrl = externalApiService.getSignedUrlForKey(meta.getS3Key(), authToken);
                    //meta.setUrl(signedUrl); // Añade la URL fresca al objeto.
                })
                .collect(Collectors.groupingBy(RespFileMetadata::getProductId));

        List<RespImagenProductRank> ranks = productsToEnrich.stream().map(product -> {
            RespProduct respProduct = mapperProduct.toRespProduct(product);
            List<RespFileMetadata> productImages = imagesByProductId.getOrDefault(
                    product.getId().toHexString(),
                    Collections.emptyList()
            );
            respProduct.setFileMetadata(productImages);
            return RespImagenProductRank.builder()
                    .respProduct(respProduct)
                    .build();
        }).collect(Collectors.toList());

        // --- PASO 5: DEVOLVER LA RESPUESTA COMPLETA Y FRESCA ---
        RespBase<List<RespImagenProductRank>> response = new RespBase<>();
        response.setPayload(ranks);
        response.setStatus(Status.builder().success(true).build());
        return response;
    }

    @Override
    //@Cacheable(value = "getViewsByIdProduct", key = "#idProduct")
    public RespBase<RespImagenProductRank> getViewsByIdProduct(String idProduct) {
        // --- PASO 1: BUSCAR EL PRODUCTO EN LA BASE DE DATOS ---
        Product product = productDAO.findOneById(Filters.eq("_id", new ObjectId(idProduct)));

        RespBase<RespImagenProductRank> response = new RespBase<>();
        response.setTrace(new Trace("true")); // El trace se puede setear al inicio

        // --- PASO 2: VALIDAR SI EL PRODUCTO EXISTE ---
        if (product == null) {
            // Si no se encontró el producto, configuramos la respuesta para indicar el fallo.
            response.setPayload(null);
            response.setStatus(Status.builder().success(false).build());
            return response;
        }

        // --- PASO 3: OBTENER LAS IMÁGENES DEL SERVICIO EXTERNO ---
        // Creamos una lista con un solo ID para reutilizar el método masivo.
        List<String> productIdSingletonList = Collections.singletonList(idProduct);
       String authToken = httpServletRequest.getHeader("Authorization");
       List<RespFileMetadata> productImages = externalApiService.getImagesByProductIds(productIdSingletonList, authToken);

        // --- PASO 4: ENRIQUECER EL DTO DEL PRODUCTO ---
        // Mapeamos el producto de la BD a nuestro DTO de respuesta.
        RespProduct respProduct = mapperProduct.toRespProduct(product);
        // ¡Enriquecemos el DTO con su lista de imágenes!
       respProduct.setFileMetadata(productImages);

        // --- PASO 5: CONSTRUIR LA RESPUESTA FINAL ---
        RespImagenProductRank rank = RespImagenProductRank.builder()
                .respProduct(respProduct)
                .respProductView(null) // O la lógica que necesites para la vista
                .build();

        response.setPayload(rank);
        response.setStatus(Status.builder().success(true).build());

        return response;
    }

    @Override
    public RespBase<List<RespProductView>> getViewsByUserId(MyJsonWebToken jwt, String userId) {
        List<ProductView> views = productViewDAO.findByUserId(userId);
        List<RespProductView> viewDtos = views.stream().map(this::toDto).collect(Collectors.toList());

        RespBase<List<RespProductView>> response = new RespBase<>();
        response.setPayload(viewDtos);
        response.setStatus(Status.builder().success(true).build());
        response.setTrace(new Trace(jwt.getId()));
        return response;
    }

    @Override
    public RespBase<List<RespProductView>> getTopViewedProducts(MyJsonWebToken jwt, int limit) {
        List<ObjectId> topIds = productViewDAO.getMostViewedProductIds();
        List<RespProductView> topViews = topIds.stream()
                .map(id -> RespProductView.builder()
                        .productId(id.toHexString())
                        .build())
                .collect(Collectors.toList());

        RespBase<List<RespProductView>> response = new RespBase<>();
        response.setPayload(topViews);
        response.setStatus(Status.builder().success(true).build());
        response.setTrace(new Trace(jwt.getId()));
        return response;
    }

    private RespProductView toDto(ProductView view) {
        return RespProductView.builder()
                .id(view.getId().toHexString())
                .productId(view.getProductId().toHexString())
                .fileId(view.getFileId() != null ? view.getFileId().toHexString() : null)
                .userId(view.getUserId())
                .build();
    }

}