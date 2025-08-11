package pillihuaman.com.pe.support.repository.inventory.dao;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqPurchaseOrder;
import pillihuaman.com.pe.support.repository.AzureAbstractMongoRepositoryImpl;
import pillihuaman.com.pe.support.repository.inventory.PurchaseOrderDAO;
import pillihuaman.com.pe.support.repository.warehouse.PurchaseOrder;

import java.util.*;

@Component
public class PurchaseOrderDaoImplement extends AzureAbstractMongoRepositoryImpl<PurchaseOrder> implements PurchaseOrderDAO {

    PurchaseOrderDaoImplement() {
        DS_WRITE = Constantes.DW;
        COLLECTION = Constantes.COLLECTION_PURCHASE_ORDER;
    }

    @Override
    public Class<PurchaseOrder> provideEntityClass() {
        return PurchaseOrder.class;
    }

    @Override
    public List<PurchaseOrder> listPurchaseOrders(ReqPurchaseOrder req) {
        MongoCollection<PurchaseOrder> collection = getCollection(COLLECTION, PurchaseOrder.class);
        List<Document> filters = new ArrayList<>();
        filters.add(new Document("status", new Document("$ne", false)));

        if (req.getId() != null) {
            filters.add(new Document("_id", new ObjectId(req.getId())));
        }
        if (!req.getSupplierId().isEmpty()) {
            filters.add(new Document("supplier.$id", new ObjectId(req.getSupplierId())));
        }

        Document query = new Document("$and", filters);
        return collection.find(query).into(new ArrayList<>());
    }

    @Override
    public PurchaseOrder savePurchaseOrder(PurchaseOrder po, MyJsonWebToken token) {
        MongoCollection<PurchaseOrder> collection = getCollection(COLLECTION, PurchaseOrder.class);
        AuditEntity audit = new AuditEntity();
        po.setStatus(true);
        if (po.getId() == null) {
            audit.setCodUser(token.getUser().getId());
            audit.setMail(token.getUser().getMail());
            audit.setDateRegister(new Date());
            po.setAudit(audit);
            collection.insertOne(po);
        } else {
            Document query = new Document("_id", po.getId());
            PurchaseOrder existing = collection.find(query).first();
            if (existing != null && existing.getAudit() != null) {
                audit = existing.getAudit();
                audit.setCodUserUpdate(token.getUser().getId());
                audit.setMailUpdate(token.getUser().getMail());
                audit.setDateUpdate(new Date());
            }
            po.setAudit(audit);
            collection.replaceOne(query, po);
        }
        return po;
    }

    @Override
    public boolean deletePurchaseOrder(String id, MyJsonWebToken token) {
        MongoCollection<PurchaseOrder> collection = getCollection(COLLECTION, PurchaseOrder.class);
        Document query = new Document("_id", new ObjectId(id));
        PurchaseOrder po = collection.find(query).first();

        if (po != null) {
            AuditEntity audit = po.getAudit();
            audit.setCodUserUpdate(token.getUser().getId());
            audit.setMailUpdate(token.getUser().getMail());
            audit.setDateUpdate(new Date());
            po.setStatus(false);
            po.setAudit(audit);
            collection.replaceOne(query, po);
            return true;
        }
        return false;
    }
}