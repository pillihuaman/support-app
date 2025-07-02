package pillihuaman.com.pe.support.repository.tenant.dao.implement;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.domain.Tenant;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqTenant;
import pillihuaman.com.pe.support.RequestResponse.dto.RespTenant;
import pillihuaman.com.pe.support.repository.AzureAbstractMongoRepositoryImpl;
import pillihuaman.com.pe.support.repository.tenant.dao.TenantDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@Repository
public class TenantDaoImplement extends AzureAbstractMongoRepositoryImpl<Tenant> implements TenantDAO {

    public TenantDaoImplement() {
        DS_WRITE = Constantes.DW;
        COLLECTION = Constantes.COLLECTION_TENANT;
    }

    @Override
    public Class<Tenant> provideEntityClass() {
        return Tenant.class;
    }

    @Override
    public List<Tenant> listTenants(ReqTenant reqTenant) {
        MongoCollection<Tenant> collection = getCollection(this.COLLECTION, Tenant.class);

        List<Document> filters = new ArrayList<>();
        filters.add(new Document("active", true));

        if (reqTenant.getId() != null && !reqTenant.getId().isEmpty()) {
            filters.add(new Document("_id", new ObjectId(reqTenant.getId())));
        }
        if (reqTenant.getName() != null && !reqTenant.getName().isEmpty()) {
            filters.add(new Document("name", new Document("$regex", ".*" + reqTenant.getName() + ".*").append("$options", "i")));
        }
        if (reqTenant.getDomain() != null && !reqTenant.getDomain().isEmpty()) {
            filters.add(new Document("domain", new Document("$regex", ".*" + reqTenant.getDomain() + ".*").append("$options", "i")));
        }

        Document query = filters.isEmpty() ? new Document() : new Document("$and", filters);
        return collection.find(query)
                .sort(new Document("audit.dateRegister", -1))
                .into(new ArrayList<>());
    }

    @Override
    public Tenant saveTenant(Tenant tenant, MyJsonWebToken token) {
        if (tenant == null || token == null) return null;

        MongoCollection<Tenant> collection = getCollection(this.COLLECTION, Tenant.class);
        AuditEntity audit = new AuditEntity();
        audit.setCodUser(token.getUser().getId());
        audit.setMail(token.getUser().getMail());

        if (tenant.getId() == null) {
            audit.setDateRegister(new Date());
            tenant.setActive(true);
            tenant.setAudit(audit);
            collection.insertOne(tenant);
        } else {
            audit.setDateUpdate(new Date());
            tenant.setAudit(audit);
            Document filter = new Document("_id", tenant.getId());
            collection.replaceOne(filter, tenant);
        }

        return tenant;
    }


    @Override
    public boolean deleteInactiveTenant(MyJsonWebToken token, String id) {
        if (id == null || id.isEmpty() || token == null) return false;

        MongoCollection<Tenant> collection = getCollection(this.COLLECTION, Tenant.class);
        Document filter = new Document("_id", new ObjectId(id));
        Tenant tenant = collection.find(filter).first();

        if (tenant != null) {
            tenant.setActive(false);

            AuditEntity audit = new AuditEntity();
            audit.setCodUser(token.getUser().getId());
            audit.setMail(token.getUser().getMail());
            audit.setDateUpdate(new Date());
            tenant.setAudit(audit);

            collection.replaceOne(filter, tenant);
            return true;
        }

        return false;
    }

    @Override
    public List<RespTenant> findByOwnerId(String name) {
        if (name == null || name.trim().isEmpty()) return new ArrayList<>();
        MongoCollection<Tenant> collection = getCollection(this.COLLECTION, Tenant.class);
        Document nameFilter = new Document("name", new Document("$regex", ".*" + name + ".*").append("$options", "i"));
        Document activeFilter = new Document("active", true);
        Document query = new Document("$and", List.of(nameFilter, activeFilter));
        List<Tenant> tenants = collection.find(query)
                .sort(new Document("audit.dateRegister", -1))
                .limit(20)
                .into(new ArrayList<>());

        List<RespTenant> response = new ArrayList<>();
        for (Tenant tenant : tenants) {
            response.add(RespTenant.builder()
                    .id(tenant.getId() != null ? tenant.getId().toHexString() : null)
                    .name(tenant.getName())
                    .domain(tenant.getDomain())
                    .active(tenant.isActive())
                    .build());
        }

        return response;
    }
    @Override
    public Optional<Tenant> findByDomain(String domain) {
        MongoCollection<Tenant> collection = getCollection(COLLECTION, Tenant.class);
        Document query = new Document("domain", domain);
        Tenant result = collection.find(query).first();
        return Optional.ofNullable(result);
    }

}
