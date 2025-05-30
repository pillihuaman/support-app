package pillihuaman.com.pe.support.repository.common.dao.implement;

import com.mongodb.client.MongoCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.Service.mapper.SystemMapper;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqSystemEntities;
import pillihuaman.com.pe.support.RequestResponse.dto.RespMenuTree;
import pillihuaman.com.pe.support.RequestResponse.dto.RespSystemEntities;
import pillihuaman.com.pe.support.repository.AzureAbstractMongoRepositoryImpl;
import pillihuaman.com.pe.support.repository.common.CommonDataDocument;
import pillihuaman.com.pe.support.repository.common.dao.CommonDAO;
import pillihuaman.com.pe.support.repository.system.MenuItem;
import pillihuaman.com.pe.support.repository.system.Page;
import pillihuaman.com.pe.support.repository.system.System;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Repository
public class CommonDaoImplement extends AzureAbstractMongoRepositoryImpl<CommonDataDocument> implements CommonDAO {

    public CommonDaoImplement() {
        DS_WRITE = Constantes.DW;
        COLLECTION = Constantes.COLLECTION_COMMON_DATA;
    }
    private static Logger logger =  LogManager.getLogger();
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

}