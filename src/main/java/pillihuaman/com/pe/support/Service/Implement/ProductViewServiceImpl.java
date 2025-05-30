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

        return new RespBase<>(RespProductView.builder()
                .id(view.getId().toHexString())
                .productId(view.getProductId().toHexString())
                .fileId(view.getFileId() != null ? view.getFileId().toHexString() : null)
                .userId(view.getUserId())
                .build());
    }
    @Override
    public RespBase<List<RespImagenProductRank>> getViews(MyJsonWebToken jwt) {
        List<ObjectId> mostViewedProductIds = productViewDAO.getMostViewedProductIds(67);

        List<RespImagenProductRank> ranks = new ArrayList<>();

        if (mostViewedProductIds == null || mostViewedProductIds.isEmpty()) {
            // Si no hay ranking, obtener todos los productos
            List<Product> allProducts = productDAO.listProducts(new ReqProduct());
            for (Product product : allProducts) {
                RespImagenProductRank rank = RespImagenProductRank.builder()
                        .respProduct(mapperProduct.toRespProduct(product))
                        .respProductView(null)
                        .build();
                ranks.add(rank);
            }
            return new RespBase<>(ranks);
        }

        // Si hay IDs, buscar solo esos productos
        for (ObjectId id : mostViewedProductIds) {
            Optional<Product> optionalProduct = Optional.ofNullable(productDAO.findOneById(Filters.eq("_id", id)));
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                RespImagenProductRank rank = RespImagenProductRank.builder()
                        .respProduct(mapperProduct.toRespProduct(product))
                        .respProductView(toDto(productViewDAO.findOneById(Filters.eq("_id", id))))
                        .build();
                ranks.add(rank);
            }
        }


        return new RespBase<>(ranks);
    }

    @Override
    public RespBase<List<RespProductView>> getViewsByUserId(MyJsonWebToken jwt, String userId) {
        List<ProductView> views = productViewDAO.findByUserId(userId);
        return new RespBase<>(views.stream()
                .map(this::toDto)
                .collect(Collectors.toList()));
    }

    @Override
    public RespBase<List<RespProductView>> getTopViewedProducts(MyJsonWebToken jwt, int limit) {
        List<ObjectId> topIds = productViewDAO.getMostViewedProductIds(limit);
        List<RespProductView> topViews = topIds.stream()
                .map(id -> RespProductView.builder()
                        .productId(id.toHexString())
                        .build())
                .collect(Collectors.toList());
        return new RespBase<>(topViews);
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