package pillihuaman.com.pe.support.repository.supplier.dao.implement;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqSupplier;
import pillihuaman.com.pe.support.RequestResponse.dto.RespSupplier;
import pillihuaman.com.pe.support.repository.AzureAbstractMongoRepositoryImpl;
import pillihuaman.com.pe.support.repository.supplier.Supplier;
import pillihuaman.com.pe.support.repository.supplier.dao.SupplierDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Repository
public class SupplierDaoImplement extends AzureAbstractMongoRepositoryImpl<Supplier> implements SupplierDAO {

    SupplierDaoImplement() {
        DS_WRITE = Constantes.DW;
        COLLECTION = Constantes.COLLECTION_SUPPLIER;
    }

    @Override
    public Class<Supplier> provideEntityClass() {
        return Supplier.class;
    }

    @Override
    public List<Supplier> listSuppliers(ReqSupplier reqSupplier) {
        MongoCollection<Supplier> collection = getCollection(this.COLLECTION, Supplier.class);

        List<Document> filters = new ArrayList<>();
        filters.add(new Document("status", true));

        if (reqSupplier.getId() != null && !reqSupplier.getId().isEmpty()) {
            filters.add(new Document("_id", new ObjectId(reqSupplier.getId())));
        }
        if (reqSupplier.getName() != null && !reqSupplier.getName().isEmpty()) {
            filters.add(new Document("name", new Document("$regex", ".*" + reqSupplier.getName() + ".*").append("$options", "i")));
        }
        if (reqSupplier.getCountry() != null && !reqSupplier.getCountry().isEmpty()) {
            filters.add(new Document("country", reqSupplier.getCountry()));
        }

        Document query = filters.isEmpty() ? new Document() : new Document("$and", filters);
        int page = reqSupplier.getPage() != null ? reqSupplier.getPage() : 1;
        int pageSize = reqSupplier.getPagesize() != null ? reqSupplier.getPagesize() : 10;
        int skip = (page - 1) * pageSize;

        return collection.find(query)
                .sort(new Document("audit.dateRegister", -1))
                .skip(skip)
                .limit(pageSize)
                .into(new ArrayList<>());
    }

    @Override
    public Supplier saveSupplier(Supplier supplier, MyJsonWebToken token) {
        if (supplier == null || token == null) {
            return null;
        }
        MongoCollection<Supplier> collection = getCollection(this.COLLECTION, Supplier.class);

        if (supplier.getId() == null) {
            AuditEntity audit = new AuditEntity();
            audit.setCodUser(token.getUser().getId());
            audit.setMail(token.getUser().getMail());
            audit.setDateRegister(new Date());
            supplier.setStatus(true);
            supplier.setAudit(audit);
            collection.insertOne(supplier);
        } else {
            Document query = new Document("_id", supplier.getId());
            Supplier existingSupplier = collection.find(query).first();

            if (existingSupplier != null) {
                supplier.setStatus(true);
                AuditEntity audit = new AuditEntity();
                audit.setCodUser(token.getUser().getId());
                audit.setMail(token.getUser().getMail());
                audit.setDateUpdate(new Date());
                supplier.setAudit(audit);
                collection.replaceOne(query, supplier);
            }
        }

        return supplier;
    }

    @Override
    public List<RespSupplier> findByOwnerId(String ownerId) {
        return List.of(); // Puedes implementar si quieres buscar suppliers por propietario
    }

    @Override
    public boolean deleteInactiveSupplier(MyJsonWebToken token, String id) {
        try {
            if (id == null || id.isEmpty() || token == null) {
                return false;
            }
            MongoCollection<Supplier> collection = getCollection(this.COLLECTION, Supplier.class);
            Document filter = new Document("_id", new ObjectId(id));
            Supplier supplier = collection.find(filter).first();

            if (supplier != null) {
                AuditEntity audit = new AuditEntity();
                audit.setCodUser(token.getUser().getId());
                audit.setMail(token.getUser().getMail());
                audit.setDateUpdate(new Date());
                supplier.setStatus(false);
                supplier.setAudit(audit);
                collection.replaceOne(filter, supplier);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error deleting Supplier: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Supplier> findSuppliersByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }
        MongoCollection<Supplier> collection = getCollection(this.COLLECTION, Supplier.class);
        Document regexFilter = new Document("name", new Document("$regex", ".*" + name + ".*").append("$options", "i"));
        Document statusFilter = new Document("status", true); // Solo activos
        Document query = new Document("$and", List.of(statusFilter, regexFilter));

        return collection.find(query)
                .sort(new Document("audit.dateRegister", -1))
                .limit(20) // Puedes ajustar el límite si quieres
                .into(new ArrayList<>());
    }

}
