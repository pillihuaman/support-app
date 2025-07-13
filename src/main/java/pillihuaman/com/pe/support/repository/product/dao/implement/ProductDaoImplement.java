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
     * Realiza una búsqueda de texto flexible en múltiples campos de productos.
     * Este método está optimizado para ser consumido por el servicio de IA.
     *
     * @param keywordsString La cadena de texto a buscar (ej: "polo sublimado algodon").
     * @param limit El número máximo de resultados a devolver.
     * @return Una lista de entidades de Producto que coinciden con los criterios.
     */
    @Override
    public List<Product> searchProductsByKeywords(String keywordsString, int limit) {
        MongoCollection<Product> collection = getCollection(this.COLLECTION, Product.class);

        // 1. Separar la cadena de búsqueda en palabras clave individuales.
        // Ejemplo: "polo algodon" -> ["polo", "algodon"]
        List<String> keywords = List.of(keywordsString.toLowerCase().split("\\s+"));

        // 2. Construir una lista de filtros ($or) para cada palabra clave.
        // La consulta buscará documentos donde CUALQUIER palabra clave coincida en CUALQUIER campo especificado.
        List<Document> orClauses = new ArrayList<>();

        for (String keyword : keywords) {
            // Se crea un patrón regex para buscar la palabra clave en cualquier parte del texto.
            // La opción "i" lo hace case-insensitive (ignora mayúsculas/minúsculas).
            Document regex = new Document("$regex", keyword).append("$options", "i");

            // Se añade un filtro $or para esta palabra clave específica.
            // Buscará la palabra 'keyword' en cualquiera de los siguientes campos.
            orClauses.add(new Document("$or", List.of(
                    new Document("name", regex),
                    new Document("description", regex),
                    new Document("category", regex),
                    new Document("subcategory", regex),
                    new Document("brand", regex),
                    new Document("media.tags", regex) // Busca dentro del array de tags
            )));
        }

        // 3. Crear la consulta final combinando todos los filtros.
        Document query = new Document();

        // El filtro principal ($and) asegura que TODAS las condiciones se cumplan.
        List<Document> andClauses = new ArrayList<>();

        // Condición 1: El producto debe estar activo.
        andClauses.add(new Document("status", true));

        // Condición 2 (opcional): Si hay palabras clave, el producto debe coincidir con TODAS ellas.
        // Usamos $and en las cláusulas $or. Esto significa que el documento debe contener "polo" Y "algodon".
        if (!orClauses.isEmpty()) {
            andClauses.addAll(orClauses);
        }

        // Se construye la consulta final.
        if (!andClauses.isEmpty()) {
            query.put("$and", andClauses);
        }

        // 4. Ejecutar la consulta con el límite de resultados.
        // System.out.println("Executing MongoDB Query: " + query.toJson()); // Descomentar para depurar la consulta
        return collection.find(query)
                .limit(limit)
                .into(new ArrayList<>());
    }

}
