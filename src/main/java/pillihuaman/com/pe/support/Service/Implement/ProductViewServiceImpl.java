package pillihuaman.com.pe.support.Service.Implement;

import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.common.ReqBase;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.support.RequestResponse.dto.Mapper.MapperProduct;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqProduct;
import pillihuaman.com.pe.support.RequestResponse.dto.RespImagenProductRank;
import pillihuaman.com.pe.support.Service.ProductViewService;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqProductView;
import pillihuaman.com.pe.support.RequestResponse.dto.RespProductView;
import pillihuaman.com.pe.support.repository.product.Product;
import pillihuaman.com.pe.support.repository.product.ProductView;
import pillihuaman.com.pe.support.repository.product.dao.ProducViewtDAO;
import pillihuaman.com.pe.support.repository.product.dao.ProductDAO;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProductViewServiceImpl implements ProductViewService {

    @Autowired
    private ProducViewtDAO productViewDAO;
    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private MapperProduct mapperProduct;

    @Override
    public RespBase<RespProductView> saveView(MyJsonWebToken jwt, ReqBase<ReqProductView> request) {
        ReqProductView req = request.getData();
        ProductView view = ProductView.builder()
                .productId(new ObjectId(req.getProductId()))
                .fileId(req.getFileId() != null ? new ObjectId(req.getFileId()) : null)
                .userId(req.getUserId())
                .viewedAt(new Date())
                .build();
        productViewDAO.saveView(view);

        RespBase<RespProductView> response = new RespBase<>();
        response.setData(RespProductView.builder()
                .id(view.getId().toHexString())
                .productId(view.getProductId().toHexString())
                .fileId(view.getFileId() != null ? view.getFileId().toHexString() : null)
                .userId(view.getUserId())
                .build());

        response.setStatus(RespBase.Status.builder().success(true).build());
        response.setTrace(new RespBase.Trace(jwt.getId()));
        return response;
    }

    @Override
    public RespBase<List<RespImagenProductRank>> getViews(MyJsonWebToken jwt) {
        List<ObjectId> mostViewedProductIds = productViewDAO.getMostViewedProductIds();
        List<RespImagenProductRank> ranks = new ArrayList<>();

        if (mostViewedProductIds == null || mostViewedProductIds.isEmpty()) {
            List<Product> allProducts = productDAO.listProducts(new ReqProduct());
            for (Product product : allProducts) {
                ranks.add(RespImagenProductRank.builder()
                        .respProduct(mapperProduct.toRespProduct(product))
                        .respProductView(null)
                        .build());
            }
        } else {
            for (ObjectId id : mostViewedProductIds) {
                Product product = productDAO.findOneById(Filters.eq("_id", id));
                if (product != null) {
                    ranks.add(RespImagenProductRank.builder()
                            .respProduct(mapperProduct.toRespProduct(product))
                            .respProductView(toDto(productViewDAO.findOneById(Filters.eq("_id", id))))
                            .build());
                }
            }
        }

        RespBase<List<RespImagenProductRank>> response = new RespBase<>();
        response.setData(ranks);
        response.setStatus(RespBase.Status.builder().success(true).build());
        response.setTrace(new RespBase.Trace(jwt.getId()));
        return response;
    }


    @Override
    public RespBase<RespImagenProductRank> getViewsByIdProduct(String idProduct) {
        // 1. Inicializamos la lista, aunque ahora solo buscamos un solo producto
        List<RespImagenProductRank> ranks = new ArrayList<>();

        // 2. Buscamos el producto por su ID
        Product product = productDAO.findOneById(Filters.eq("_id", new ObjectId(idProduct)));

        // 3. Validamos si el producto fue encontrado (no es null)
        if (product != null) {
            ranks.add(RespImagenProductRank.builder()
                    .respProduct(mapperProduct.toRespProduct(product))
                    .build());
        }

        // 4. Creamos la respuesta base
        RespBase<RespImagenProductRank> response = new RespBase<>();

        // 5. Validamos si la lista 'ranks' tiene al menos un elemento antes de acceder a él
        if (!ranks.isEmpty()) {
            response.setData(ranks.get(0));
            response.getStatus().setSuccess(true);
        } else {
            // 6. Si no se encontró el producto, configuramos la respuesta para indicar el error
            response.setData(null);
            response.getStatus().setSuccess(false);
        }

        // 7. Configuración del trace, que es independiente del éxito de la operación
        response.setTrace(new RespBase.Trace("true"));

        return response;
    }


    @Override
    public RespBase<List<RespProductView>> getViewsByUserId(MyJsonWebToken jwt, String userId) {
        List<ProductView> views = productViewDAO.findByUserId(userId);
        List<RespProductView> viewDtos = views.stream().map(this::toDto).collect(Collectors.toList());

        RespBase<List<RespProductView>> response = new RespBase<>();
        response.setData(viewDtos);
        response.setStatus(RespBase.Status.builder().success(true).build());
        response.setTrace(new RespBase.Trace(jwt.getId()));
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
        response.setData(topViews);
        response.setStatus(RespBase.Status.builder().success(true).build());
        response.setTrace(new RespBase.Trace(jwt.getId()));
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