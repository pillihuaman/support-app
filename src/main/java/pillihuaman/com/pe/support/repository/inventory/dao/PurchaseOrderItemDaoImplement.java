package pillihuaman.com.pe.support.repository.inventory.dao;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqPurchaseOrderItem;
import pillihuaman.com.pe.support.repository.AzureAbstractMongoRepositoryImpl;
import pillihuaman.com.pe.support.repository.inventory.PurchaseOrderItemDAO;
import pillihuaman.com.pe.support.repository.warehouse.PurchaseOrderItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Repository
public class PurchaseOrderItemDaoImplement
        extends AzureAbstractMongoRepositoryImpl<PurchaseOrderItem>
        implements PurchaseOrderItemDAO {

    public PurchaseOrderItemDaoImplement() {
        DS_WRITE = Constantes.DW;
        COLLECTION = "purchase_order_items";
    }

    @Override
    public Class<PurchaseOrderItem> provideEntityClass() {
        return PurchaseOrderItem.class;
    }

    @Override
    public List<PurchaseOrderItem> listItems(ReqPurchaseOrderItem req) {
        MongoCollection<PurchaseOrderItem> collection = getCollection(COLLECTION, PurchaseOrderItem.class);
        List<Document> filters = new ArrayList<>();

        // Estado activo
        filters.add(new Document("status", true));

        // Filtros condicionales
        if (req.getId() != null && !req.getId().isEmpty()) {
            filters.add(new Document("_id", new ObjectId(req.getId())));
        }

        if (req.getPurchaseOrderId() != null && !req.getPurchaseOrderId().isEmpty()) {
            filters.add(new Document("purchaseOrder.$id", new ObjectId(req.getPurchaseOrderId())));
        }

        // Construir query
        Document query = filters.size() > 1 ? new Document("$and", filters) : filters.get(0);

        return collection.find(query).into(new ArrayList<>());
    }


    @Override
    public PurchaseOrderItem saveItem(PurchaseOrderItem item, MyJsonWebToken token) {
        MongoCollection<PurchaseOrderItem> collection = getCollection(COLLECTION, PurchaseOrderItem.class);

        // --- LÓGICA DE ACTUALIZACIÓN ---
        if (item.getId() != null && !item.getId().toString().isEmpty()) {

            Document filter = new Document("_id", item.getId());
            PurchaseOrderItem existingItem = collection.find(filter).first();
            if (existingItem == null) {
                return null; // O retornar null para indicar que no se encontró
            }

            // 2. Preservar la auditoría original y agregar los datos de actualización
            AuditEntity audit = existingItem.getAudit();
            if (audit == null) { // Medida de seguridad si el dato antiguo no tenía auditoría
                audit = new AuditEntity();
                audit.setCodUser(token.getUser().getId());
                audit.setMail(token.getUser().getMail());
                audit.setDateRegister(new Date()); // Asumir ahora como fecha de registro
            }
            audit.setCodUserUpdate(token.getUser().getId());
            audit.setMailUpdate(token.getUser().getMail());
            audit.setDateUpdate(new Date());
            item.setAudit(audit);

            // 3. Asegurar que el estado y el ID se mantengan consistentes
            item.setStatus(true); // Re-activar si se está guardando de nuevo
            item.setId(existingItem.getId()); // Usar el ID del objeto existente
            // 4. Reemplazar el documento en la base de datos
            collection.replaceOne(filter, item);

        } else {
            // --- LÓGICA DE INSERCIÓN ---
            // 1. Crear la auditoría para el nuevo documento
            AuditEntity audit = new AuditEntity();
            audit.setCodUser(token.getUser().getId());
            audit.setMail(token.getUser().getMail());
            audit.setDateRegister(new Date());
            item.setAudit(audit);
            // 2. Establecer el estado inicial y generar un nuevo ID
            item.setStatus(true);

            // 3. Insertar el nuevo documento
            collection.insertOne(item);
        }

        return item;
    }

    @Override
    public boolean deleteItem(MyJsonWebToken token, String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }

        MongoCollection<PurchaseOrderItem> collection = getCollection(COLLECTION, PurchaseOrderItem.class);
        Document filter = new Document("_id", new ObjectId(id.trim()));
        PurchaseOrderItem item = collection.find(filter).first();

        if (item != null) {
            item.setStatus(false);
            if (item.getAudit() == null) {
                item.setAudit(new AuditEntity());
            }
            item.getAudit().setCodUserUpdate(token.getUser().getId());
            item.getAudit().setMailUpdate(token.getUser().getMail());
            item.getAudit().setDateUpdate(new Date());
            collection.replaceOne(filter, item);
            return true;
        }
        return false;
    }
}
