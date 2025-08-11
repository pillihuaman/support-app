package pillihuaman.com.pe.support.repository.common.dao.implement;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
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

import java.util.ArrayList;
import java.util.List;
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

    @Override
    public List<CommonDataDocument> findAllByIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>(); // Devuelve lista vacía si no hay IDs
        }

        try {
            MongoCollection<CommonDataDocument> collection = getCollection(this.COLLECTION, CommonDataDocument.class);
            // Usamos el operador $in para buscar todos los documentos cuyos _id estén en la lista de IDs
            return collection.find(Filters.in("_id", ids)).into(new ArrayList<>());
        } catch (Exception e) {
            logger.error("Error al buscar CommonDataDocument por lista de IDs: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<CommonDataDocument> findByConfigType(String configType) {
        if (configType == null || configType.isEmpty()) {
            return new ArrayList<>();
        }

        try {
            MongoCollection<CommonDataDocument> collection = getCollection(this.COLLECTION, CommonDataDocument.class);
            // Filtro por el campo configType
            return collection.find(Filters.eq("configType", configType)).into(new ArrayList<>());
        } catch (Exception e) {
            logger.error("Error al buscar CommonDataDocument por configType '{}': {}", configType, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

}