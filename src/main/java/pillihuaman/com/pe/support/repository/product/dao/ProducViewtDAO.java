package pillihuaman.com.pe.support.repository.product.dao;


import org.bson.Document;
import org.bson.types.ObjectId;
import pillihuaman.com.pe.support.repository.BaseMongoRepository;
import pillihuaman.com.pe.support.repository.product.ProductView;

import java.util.List;

public interface ProducViewtDAO extends BaseMongoRepository<ProductView> {

    // Guarda una vista de producto
    void saveView(ProductView view);

    // Lista todas las vistas registradas
    List<ProductView> findAllViews();

    // Agrupa vistas por productId y cuenta
    List<Document> countViewsGroupedByProductId();

    // Lista vistas por usuario
    List<ProductView> findByUserId(String userId);

    // Obtiene los productos más vistos
    List<ObjectId> getMostViewedProductIds();
    // Busca una vista por productId (opcionalmente la más reciente si hay varias)

    ProductView findByProductId(String productId);

}
