package pillihuaman.com.pe.support.repository.product.dao.implement;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqProduct;
import pillihuaman.com.pe.support.repository.AzureAbstractMongoRepositoryImpl;
import pillihuaman.com.pe.support.repository.product.Product;
import pillihuaman.com.pe.support.repository.product.dao.ProductDAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@Repository
public class ProductDaoImplement extends AzureAbstractMongoRepositoryImpl<Product> implements ProductDAO {
    ProductDaoImplement() {
        DS_WRITE = Constantes.DW;
        // DS_READ = Constants.DR;
        COLLECTION = Constantes.COLLECTION_PRODUCT;
    }
    @Override
    public Class<Product> provideEntityClass() {
        return Product.class;
    }

    @Override
    public List<Product> listProducts(ReqProduct reqProduct) {
        MongoCollection<Product> collection = getCollection(this.COLLECTION, Product.class);

        List<Document> filters = new ArrayList<>();

        // Only find active Products
        filters.add(new Document("status", true));

        // Exact match for ObjectId (if provided)
        if (reqProduct.getId() != null && !reqProduct.getId().isEmpty()) {
            filters.add(new Document("_id", new ObjectId(reqProduct.getId())));
        }

        // Case-insensitive substring search for Name
        if (reqProduct.getName() != null && !reqProduct.getName().isEmpty()) {
            String regexPattern = ".*" + reqProduct.getName() + ".*";  // Find `mala+ 4` anywhere in the string
            filters.add(new Document("name", new Document("$regex", regexPattern).append("$options", "i")));
        }
        // Filter by Category (Exact Match)
        if (reqProduct.getCategory() != null && !reqProduct.getCategory().isEmpty()) {
            filters.add(new Document("category", reqProduct.getCategory()));
        }

        // Filter by Barcode (Exact Match)
        if (reqProduct.getBarcode() != null && !reqProduct.getBarcode().isEmpty()) {
            filters.add(new Document("barcode", reqProduct.getBarcode()));
        }

        Document query = filters.isEmpty() ? new Document() : new Document("$and", filters);
        // Pagination settings
        int page = (reqProduct.getPage() != null && reqProduct.getPage() > 0) ? reqProduct.getPage() : 1;
        int pageSize = (reqProduct.getPagesize() != null && reqProduct.getPagesize() > 0) ? reqProduct.getPagesize() : 10;
        int skip = (page - 1) * pageSize;
        // Execute query with pagination
        return collection.find(query)
                .sort(new Document("audit.dateRegister", -1)) // Sort by `dateRegister` descending
                .skip(skip)
                .limit(pageSize)
                .into(new ArrayList<>());
    }

    @Override
    public List<Product> findByUserId(String userId) {
        MongoCollection<Product> collection = getCollection(this.COLLECTION, Product.class);
        // Query for active Products only
        Document query = new Document("user.id", userId)
                .append("status", true);
        return collection.find(query)
                .into(new ArrayList<>());
    }


    @Override
    public Product saveProduct(Product Product, MyJsonWebToken token) {
        if (Product == null || token == null) {
            return null;
        }
        // Obtener la colección de MongoDB
        MongoCollection<Product> collection = getCollection(this.COLLECTION, Product.class);

        if (Product.getId() == null) {
            // Configurar datos de auditoría
            AuditEntity audit = new AuditEntity();
            audit.setCodUser(token.getUser().getId());
            audit.setMail(token.getUser().getMail());
            audit.setDateRegister(new Date());
            Product.setStatus(true);
            Product.setAudit(audit);
            // Insertar nuevo registro
            collection.insertOne(Product);
        } else {
            // Find existing document
            Document query = new Document("_id", Product.getId());
            Product existingProduct = collection.find(query).first();

            if (existingProduct != null) {
                // Update audit details
                Product.setStatus(true);
                AuditEntity audit = new AuditEntity();
                audit.setCodUser(token.getUser().getId());
                audit.setMail(token.getUser().getMail());
                audit.setDateUpdate(new Date());
                Product.setAudit(audit);

                // Update the document
                collection.replaceOne(query, Product);
                System.out.println("Documento actualizado con ID: " + Product.getId());
            } else {
                System.out.println("No se encontró el documento con ID: " + Product.getId());
            }
        }

        return Product;
    }



    @Override
    public boolean deleteInactiveProduct(MyJsonWebToken token, String id) {
        try {
            if (id == null || id.isEmpty() || token == null) {
                return false;
            }

            MongoCollection<Product> collection = getCollection(this.COLLECTION, Product.class);

            // Find the Product
            Document filter = new Document("_id", new ObjectId(id));
            Product Product = collection.find(filter).first();

            if (Product != null) {
                // Update audit details before deletion
                AuditEntity audit = new AuditEntity();
                audit.setCodUser(token.getUser().getId());
                audit.setMail(token.getUser().getMail());
                audit.setDateUpdate(new Date());
                Product.setStatus(false);
                Product.setAudit(audit);

                // Save updated audit info before deletion (optional, depends on business logic)
                collection.replaceOne(filter, Product);

                // Delete Product
                long deletedCount = collection.deleteOne(filter).getDeletedCount();
                return deletedCount > 0;
            }

            return false;
        } catch (Exception e) {
            System.err.println("Error deleting Product: " + e.getMessage());
            return false;
        }
    }


    /**
     * Realiza una búsqueda de texto inteligente y flexible en múltiples campos de productos.
     * La búsqueda se realiza en varias etapas dentro de un pipeline de agregación para mejorar la relevancia:
     * 1. Filtra solo productos activos (`status: true`).
     * 2. Utiliza el índice de texto de MongoDB para encontrar coincidencias con las palabras clave.
     * 3. Calcula un 'score' de relevancia basado en el `textScore` de MongoDB.
     * 4. Añade un 'boost' (impulso) a la puntuación de los productos cuyo nombre contenga la frase de búsqueda exacta.
     * 5. Ordena los resultados finales por esta puntuación combinada, mostrando los más relevantes primero.
     *
     * @param keywordsString La cadena de texto a buscar (ej: "polo sublimado algodon").
     * @param limit El número máximo de resultados a devolver.
     * @return Una lista de entidades de Producto que coinciden con los criterios, ordenadas por relevancia.
     */
    @Override
    public List<Product> searchProductsByKeywords(String keywordsString, int limit) {
        MongoCollection<Product> collection = getCollection(this.COLLECTION, Product.class);

        // Se construye el pipeline de agregación
        List<Document> pipeline = new ArrayList<>();

        // --- Etapa 1: $match - Filtrado Inicial ---
        // Se buscan documentos que estén activos Y que coincidan con los términos de búsqueda usando el índice de texto.
        // MongoDB procesa internamente la 'keywordsString' para buscar las palabras.
        Document initialMatch = new Document("$match",
                new Document("$and", Arrays.asList(
                        new Document("status", true),
                        new Document("$text", new Document("$search", keywordsString))
                ))
        );
        pipeline.add(initialMatch);

        // --- Etapa 2: $addFields - Añadir Campos para el Cálculo de Relevancia ---
        // Se añaden dos nuevos campos:
        // 1. 'score': La puntuación de relevancia base proporcionada por la búsqueda de texto ($meta: "textScore").
        // 2. 'nameMatchBoost': Un campo para potenciar las coincidencias de frase exacta en el nombre.
        Document addFields = new Document("$addFields",
                new Document("score", new Document("$meta", "textScore"))
                        .append("nameMatchBoost", new Document("$cond", new Document()
                                .append("if", new Document("$regexMatch", new Document("input", "$name").append("regex", keywordsString).append("options", "i")))
                                .append("then", 10) // Boost de 10 puntos si el nombre contiene la frase exacta
                                .append("else", 0)
                        ))
        );
        pipeline.add(addFields);

        // --- Etapa 3: $addFields - Calcular la Puntuación Final ---
        // Se crea un campo 'finalScore' que suma el score base y el boost.
        // Esto asegura que las coincidencias exactas en el nombre tengan una prioridad mucho mayor.
        Document finalScoreCalculation = new Document("$addFields",
                new Document("finalScore", new Document("$add", Arrays.asList("$score", "$nameMatchBoost")))
        );
        pipeline.add(finalScoreCalculation);


        // --- Etapa 4: $sort - Ordenar por Relevancia ---
        // Se ordena por la puntuación final en orden descendente. Los más relevantes aparecerán primero.
        Document sortStage = new Document("$sort", new Document("finalScore", -1));
        pipeline.add(sortStage);

        // --- Etapa 5: $limit - Limitar el Número de Resultados ---
        Document limitStage = new Document("$limit", limit);
        pipeline.add(limitStage);

        // --- Etapa 6 (Opcional): $project - Limpiar los campos de puntuación de la salida final ---
        // Para no devolver score, nameMatchBoost y finalScore al cliente.
        Document projectStage = new Document("$project",
                new Document("score", 0)
                        .append("nameMatchBoost", 0)
                        .append("finalScore", 0)
        );
        pipeline.add(projectStage);


        // System.out.println("Executing MongoDB Aggregation: " + pipeline); // Descomentar para depurar

        return collection.aggregate(pipeline, Product.class).into(new ArrayList<>());
    }


}
