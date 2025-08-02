package pillihuaman.com.pe.support.repository.product.dao.implement;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.repository.AzureAbstractMongoRepositoryImpl;
import pillihuaman.com.pe.support.repository.product.ProductView;
import pillihuaman.com.pe.support.repository.product.dao.ProducViewtDAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Projections.*;

@Component
@Repository
public class ProductViewDaoImplement extends AzureAbstractMongoRepositoryImpl<ProductView> implements ProducViewtDAO {

    public ProductViewDaoImplement() {
        DS_WRITE = Constantes.DW;
        COLLECTION = Constantes.COLLECTION_PRODUCT_VIEW;
    }

    @Override
    public Class<ProductView> provideEntityClass() {
        return ProductView.class;
    }

    // Guarda una vista del producto
    @Override
    public void saveView(ProductView view) {
        if (view == null || view.getProductId() == null) return;
        MongoCollection<ProductView> collection = getCollection(COLLECTION, ProductView.class);
        view.setViewedAt(new Date());
        collection.insertOne(view);
    }

    // Devuelve todas las vistas
    @Override
    public List<ProductView> findAllViews() {
        MongoCollection<ProductView> collection = getCollection(COLLECTION, ProductView.class);
        return collection.find().into(new ArrayList<>());
    }

    // Agrupa vistas por producto y cuenta las vistas
    @Override
    public List<Document> countViewsGroupedByProductId() {
        MongoCollection<Document> rawCollection = getRawCollection(COLLECTION);

        List<Bson> pipeline = Arrays.asList(
                group("$productId", sum("viewCount", 1)),
                project(fields(computed("productId", "$_id"), include("viewCount"), excludeId()))
        );

        return rawCollection.aggregate(pipeline).into(new ArrayList<>());
    }

    // Guarda usando el método oficial
    public ProductView saveProductView(ProductView view) {
        saveView(view);
        return view;
    }

    // Actualiza una vista existente
    public ProductView updateProductView(ObjectId id, ProductView view) {
        MongoCollection<ProductView> collection = getCollection(COLLECTION, ProductView.class);
        Bson filter = Filters.eq("_id", id);
        Bson update = Updates.combine(
                Updates.set("productId", view.getProductId()),
                Updates.set("fileId", view.getFileId()),
                Updates.set("userId", view.getUserId()),
                Updates.set("viewedAt", new Date())
        );
        collection.updateOne(filter, update);
        return collection.find(filter).first();
    }

    // Busca vistas por usuario
    @Override
    public List<ProductView> findByUserId(String userId) {
        MongoCollection<ProductView> collection = getCollection(COLLECTION, ProductView.class);
        return collection.find(Filters.eq("userId", userId)).into(new ArrayList<>());
    }

    // Retorna IDs de productos más vistos
    @Override
    public List<ObjectId> getMostViewedProductIds() {
        return countViewsGroupedByProductId().stream()
                //.limit(limit)
                .map(doc -> doc.get("_id", ObjectId.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductView findByProductId(String productId) {
        MongoCollection<ProductView> collection = getCollection(COLLECTION, ProductView.class);
        return collection
                .find(Filters.eq("_id",  new ObjectId( productId)))
                .sort(Sorts.descending("viewedAt"))  // Por si hay múltiples, toma la más reciente
                .first();
    }

}
