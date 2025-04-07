package pillihuaman.com.pe.support.repository.store.dao.implement;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.RequestResponse.RespStore;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqStore;
import pillihuaman.com.pe.support.repository.AzureAbstractMongoRepositoryImpl;
import pillihuaman.com.pe.support.repository.store.Store;
import pillihuaman.com.pe.support.repository.store.dao.StoreDAO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Component
@Repository
public class StoreDaoImplement extends AzureAbstractMongoRepositoryImpl<Store> implements StoreDAO {
    StoreDaoImplement() {
        DS_WRITE = Constantes.DW;
        COLLECTION = Constantes.COLLECTION_STORE;
    }

    @Override
    public Class<Store> provideEntityClass() {
        return Store.class;
    }

    @Override
    public List<Store> listStores(ReqStore reqStore) {
        MongoCollection<Store> collection = getCollection(this.COLLECTION, Store.class);

        List<Document> filters = new ArrayList<>();
        filters.add(new Document("status", "active"));

        if (reqStore.getId() != null && !reqStore.getId().isEmpty()) {
            filters.add(new Document("_id", new ObjectId(reqStore.getId())));
        }
        if (reqStore.getName() != null && !reqStore.getName().isEmpty()) {
            filters.add(new Document("name", new Document("$regex", ".*" + reqStore.getName() + ".*").append("$options", "i")));
        }
        if (reqStore.getCountry() != null && !reqStore.getCountry().isEmpty()) {
            filters.add(new Document("country", reqStore.getCountry()));
        }

        Document query = filters.isEmpty() ? new Document() : new Document("$and", filters);
        int page = reqStore.getPage() != null ? reqStore.getPage() : 1;
        int pageSize = reqStore.getPagesize() != null ? reqStore.getPagesize() : 10;
        int skip = (page - 1) * pageSize;

        return collection.find(query)
                .sort(new Document("audit.dateRegister", -1))
                .skip(skip)
                .limit(pageSize)
                .into(new ArrayList<>());
    }


    @Override
    public Store saveStore(Store store, MyJsonWebToken token) {
        if (store == null || token == null) {
            return null;
        }
        MongoCollection<Store> collection = getCollection(this.COLLECTION, Store.class);

        if (store.getId() == null) {
            AuditEntity audit = new AuditEntity();
            audit.setCodUser(token.getUser().getId());
            audit.setMail(token.getUser().getMail());
            audit.setDateRegister(new Date());
            store.setStatus("active");
            store.setAudit(audit);
            collection.insertOne(store);
        } else {
            Document query = new Document("_id", store.getId());
            Store existingStore = collection.find(query).first();

            if (existingStore != null) {
                store.setStatus("active");
                AuditEntity audit = new AuditEntity();
                audit.setCodUser(token.getUser().getId());
                audit.setMail(token.getUser().getMail());
                audit.setDateUpdate(new Date());
                store.setAudit(audit);
                collection.replaceOne(query, store);
            }
        }

        return store;
    }

    @Override
    public List<RespStore> findByOwnerId(String ownerId) {
        return List.of();
    }

    @Override
    public boolean deleteInactiveStore(MyJsonWebToken token, String id) {
        try {
            if (id == null || id.isEmpty() || token == null) {
                return false;
            }
            MongoCollection<Store> collection = getCollection(this.COLLECTION, Store.class);
            Document filter = new Document("_id", new ObjectId(id));
            Store store = collection.find(filter).first();

            if (store != null) {
                AuditEntity audit = new AuditEntity();
                audit.setCodUser(token.getUser().getId());
                audit.setMail(token.getUser().getMail());
                audit.setDateUpdate(new Date());
                store.setStatus("inactive");
                store.setAudit(audit);
                collection.replaceOne(filter, store);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error deleting Store: " + e.getMessage());
            return false;
        }
    }
}
