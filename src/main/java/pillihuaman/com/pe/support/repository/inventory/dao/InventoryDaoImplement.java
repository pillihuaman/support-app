package pillihuaman.com.pe.support.repository.inventory.dao;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqInventory;
import pillihuaman.com.pe.support.repository.AzureAbstractMongoRepositoryImpl;
import pillihuaman.com.pe.support.repository.inventory.InventoryDAO;
import pillihuaman.com.pe.support.repository.warehouse.Inventory;

import java.util.*;

@Component
@Repository
public class InventoryDaoImplement extends AzureAbstractMongoRepositoryImpl<Inventory> implements InventoryDAO {

    public InventoryDaoImplement() {
        DS_WRITE = Constantes.DW;
        COLLECTION = "inventories";
    }

    @Override
    public Class<Inventory> provideEntityClass() {
        return Inventory.class;
    }

    @Override
    public List<Inventory> listInventories(ReqInventory req) {
        if (req == null) {
            return Collections.emptyList();
        }

        MongoCollection<Inventory> collection = getCollection(COLLECTION, Inventory.class);
        List<Document> filters = new ArrayList<>();

        // 🔹 Siempre filtrar por status activo
        filters.add(new Document("status", true));

        if (req.getId() != null && !req.getId().isBlank()) {
            try {
                filters.add(new Document("_id", new ObjectId(req.getId())));
            } catch (IllegalArgumentException e) {
                // ID inválido, lo ignoramos
            }
        }
        if (req.getProductId() != null && !req.getProductId().isBlank()) {
            try {
                filters.add(new Document("product.$id", new ObjectId(req.getProductId())));
            } catch (IllegalArgumentException e) {
                // ProductId inválido, lo ignoramos
            }
        }
        if (req.getWarehouseId() != null && !req.getWarehouseId().isBlank()) {
            try {
                filters.add(new Document("warehouse.$id", new ObjectId(req.getWarehouseId())));
            } catch (IllegalArgumentException e) {
                // WarehouseId inválido, lo ignoramos
            }
        }

        Document query = filters.isEmpty() ? new Document() : new Document("$and", filters);
        int page = (req.getPage() != null && req.getPage() > 0) ? req.getPage() : 1;
        int pageSize = (req.getPagesize() != null && req.getPagesize() > 0) ? req.getPagesize() : 10;
        int skip = (page - 1) * pageSize;

        return collection.find(query).skip(skip).limit(pageSize).into(new ArrayList<>());
    }


    @Override
    public Inventory saveInventory(Inventory inventory, MyJsonWebToken token) {
        if (inventory == null || token == null || token.getUser() == null) {
            return null;
        }

        MongoCollection<Inventory> collection = getCollection(COLLECTION, Inventory.class);

        if (inventory.getId() == null) {
            AuditEntity audit = new AuditEntity();
            audit.setCodUser(token.getUser().getId());
            audit.setMail(token.getUser().getMail() != null ? token.getUser().getMail() : "");
            audit.setDateRegister(new Date());
            inventory.setStatus(true);
            // Aquí podrías setear el audit en inventory si tu clase lo soporta
            collection.insertOne(inventory);
        } else {
            Document query = new Document("_id", inventory.getId());
            collection.replaceOne(query, inventory);
        }

        return inventory;
    }

    @Override
    public boolean deleteInventory(MyJsonWebToken token, String id) {
        if (token == null || token.getUser() == null || id == null || id.isBlank()) {
            return false;
        }

        try {
            MongoCollection<Inventory> collection = getCollection(COLLECTION, Inventory.class);
            Document query = new Document("_id", new ObjectId(id));
            return collection.deleteOne(query).getDeletedCount() > 0;
        } catch (IllegalArgumentException e) {
            // ID inválido para ObjectId
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
