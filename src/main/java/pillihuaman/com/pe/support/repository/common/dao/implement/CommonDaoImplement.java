package pillihuaman.com.pe.support.repository.common.dao.implement;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.repository.AzureAbstractMongoRepositoryImpl;
import pillihuaman.com.pe.support.repository.common.CommonDataDocument;
import pillihuaman.com.pe.support.repository.common.dao.CommonDAO;

import java.util.Optional;

@Component
@Repository
public class CommonDaoImplement extends AzureAbstractMongoRepositoryImpl<CommonDataDocument> implements CommonDAO {

    public CommonDaoImplement() {
        DS_WRITE = Constantes.DW;
        COLLECTION = Constantes.COLLECTION_COMMON_DATA;
    }

    private static Logger logger = LogManager.getLogger();

    @Override
    public Class<CommonDataDocument> provideEntityClass() {
        return CommonDataDocument.class;
    }

    @Override
    public Optional<CommonDataDocument> findById(String id) {
        if (id == null || id.isEmpty()) {
            return Optional.empty();
        }

        try {
            MongoCollection<CommonDataDocument> collection = getCollection(this.COLLECTION, CommonDataDocument.class);
            Document filter = new Document("_id", id);
            CommonDataDocument result = collection.find(filter).first();
            return Optional.ofNullable(result);
        } catch (Exception e) {
            logger.error("Error al buscar CommonDataDocument por ID: " + e.getMessage());
            return Optional.empty();
        }
    }
    @Override
    public CommonDataDocument save(CommonDataDocument document) {
        try {
            MongoCollection<CommonDataDocument> collection = getCollection(COLLECTION, provideEntityClass());
            Document filter = new Document("_id", document.getId());
            // La opción 'upsert' crea el documento si no existe
            ReplaceOptions options = new ReplaceOptions().upsert(true);
            collection.replaceOne(filter, document, options);
            logger.info("Documento de configuración con ID '{}' guardado/actualizado exitosamente.", document.getId());
            return document;
        } catch (Exception e) {
            logger.error("Error al guardar/actualizar CommonDataDocument con ID '{}': {}", document.getId(), e.getMessage(), e);
            // Considera lanzar una excepción personalizada aquí para un mejor manejo de errores.
            throw new RuntimeException("Error al guardar el documento de configuración.", e);
        }
    }
}