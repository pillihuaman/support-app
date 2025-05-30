package pillihuaman.com.pe.support.repository.system.dao.implement;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.Service.mapper.PageMapper;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqPage;
import pillihuaman.com.pe.support.RequestResponse.dto.RespPage;
import pillihuaman.com.pe.support.repository.AzureAbstractMongoRepositoryImpl;
import pillihuaman.com.pe.support.repository.system.Page;
import pillihuaman.com.pe.support.repository.system.dao.PageDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Repository
public class PageDaoImplement extends AzureAbstractMongoRepositoryImpl<Page> implements PageDAO {

    public PageDaoImplement() {
        DS_WRITE = Constantes.DW;
        COLLECTION = Constantes.COLLECTION_PAGE;
    }

    @Override
    public Class<Page> provideEntityClass() {
        return Page.class;
    }

    @Override
    public List<RespPage> listPages(ReqPage req) {
        Page page = PageMapper.INSTANCE.toPageEntity(req);
        MongoCollection<Page> collection = getCollection(this.COLLECTION, Page.class);
        List<Document> filters = new ArrayList<>();
        filters.add(new Document("status", true));
        Document query = new Document("$and", filters);
        List<Page> lis = collection.find(query)
                .sort(new Document("audit.dateRegister", -1))
                .into(new ArrayList<>());

        return PageMapper.INSTANCE.toRespPageList(lis);
    }

    @Override
    public RespPage savePage(ReqPage req, MyJsonWebToken token) {
        Page page = PageMapper.INSTANCE.toPageEntity(req);
        if (page == null || token == null) return null;

        MongoCollection<Page> collection = getCollection(this.COLLECTION, Page.class);

        if (page.getId() == null || page.getId().toString().isEmpty()) {
            AuditEntity audit = new AuditEntity();
            audit.setCodUser(token.getUser().getId());
            audit.setMail(token.getUser().getMail());
            audit.setDateRegister(new Date());
            page.setAudit(audit);
            page.setStatus(true);
            collection.insertOne(page);
        } else {
            Document query = new Document("_id", new ObjectId(page.getId().toString()));
            Page existing = collection.find(query).first();

            if (existing != null) {
                AuditEntity audit = new AuditEntity();
                audit.setCodUser(token.getUser().getId());
                audit.setMail(token.getUser().getMail());
                audit.setDateUpdate(new Date());
                page.setAudit(audit);
                page.setStatus(true);
                collection.replaceOne(query, page);
            }
        }

        return PageMapper.INSTANCE.toRespPage(page);
    }

    @Override
    public List<RespPage> findBySystemId(String systemId) {
        MongoCollection<Page> collection = getCollection(this.COLLECTION, Page.class);
        Document query = new Document("systemId", systemId)
                .append("status", true);
        List<Page> lis = collection.find(query).into(new ArrayList<>());
        return PageMapper.INSTANCE.toRespPageList(lis);
    }

    @Override
    public boolean deletePageById(String id, MyJsonWebToken token) {
        if (id == null || id.isEmpty() || token == null) return false;

        MongoCollection<Page> collection = getCollection(this.COLLECTION, Page.class);
        Document query = new Document("_id", new ObjectId(id));
        Page page = collection.find(query).first();

        if (page != null) {
            AuditEntity audit = new AuditEntity();
            audit.setCodUser(token.getUser().getId());
            audit.setMail(token.getUser().getMail());
            audit.setDateUpdate(new Date());
            page.setAudit(audit);
            page.setStatus(false);

            collection.replaceOne(query, page);
            return true;
        }

        return false;
    }
}
