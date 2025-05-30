package pillihuaman.com.pe.support.repository.system.dao.implement;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.Service.mapper.MenuMapper;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqMenu;
import pillihuaman.com.pe.support.RequestResponse.dto.RespMenu;
import pillihuaman.com.pe.support.repository.AzureAbstractMongoRepositoryImpl;
import pillihuaman.com.pe.support.repository.system.MenuItem;
import pillihuaman.com.pe.support.repository.system.dao.MenuItemDAO;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Repository
public class MenuItemDaoImplement extends AzureAbstractMongoRepositoryImpl<MenuItem> implements MenuItemDAO {

    public MenuItemDaoImplement() {
        DS_WRITE = Constantes.DW;
        COLLECTION = Constantes.COLLECTION_MENU_ITEM;
    }

    @Override
    public Class<MenuItem> provideEntityClass() {
        return MenuItem.class;
    }

    @Override
    public List<RespMenu> listMenuItems(ReqMenu re) {
        MongoCollection<MenuItem> collection = getCollection(this.COLLECTION, MenuItem.class);
        List<Document> filters = new ArrayList<>();
        filters.add(new Document("status", true));
        Document query = new Document("$and", filters);
        return MenuMapper.INSTANCE.toRespMenuList(collection.find(query)
                .sort(new Document("order", 1))
                .into(new ArrayList<>()))
                ;
    }

    @Override
    public List<RespMenu> findBySystemId(String systemId) {
        MongoCollection<MenuItem> collection = getCollection(this.COLLECTION, MenuItem.class);
        Document query = new Document("systemId", systemId)
                .append("status", true);
        return MenuMapper.INSTANCE.toRespMenuList(collection.find(query).into(new ArrayList<>()));
    }

    @Override
    public List<RespMenu> findByParentId(String parentId) {
        MongoCollection<MenuItem> collection = getCollection(this.COLLECTION, MenuItem.class);
        Document query = new Document("parentId", parentId)
                .append("status", true);
        return MenuMapper.INSTANCE.toRespMenuList(collection.find(query).into(new ArrayList<>()));
    }

    @Override
    public RespMenu saveMenuItem(ReqMenu re, MyJsonWebToken token) {
        MenuItem menu = MenuMapper.INSTANCE.toMenuItem(re);
        if (menu == null || token == null) return null;

        MongoCollection<MenuItem> collection = getCollection(this.COLLECTION, MenuItem.class);

        if (menu.getId() == null || menu.getId().toString().isEmpty()) {
            AuditEntity audit = new AuditEntity();
            audit.setCodUser(token.getUser().getId());
            audit.setMail(token.getUser().getMail());
            audit.setDateRegister(new Date());
            menu.setAudit(audit);
            menu.setStatus(true);
            collection.insertOne(menu);
        } else {
            Document query = new Document("_id", new ObjectId(menu.getId().toString()));
            MenuItem existing = collection.find(query).first();

            if (existing != null) {
                AuditEntity audit = new AuditEntity();
                audit.setCodUser(token.getUser().getId());
                audit.setMail(token.getUser().getMail());
                audit.setDateUpdate(new Date());
                menu.setAudit(audit);
                menu.setStatus(true);
                collection.replaceOne(query, menu);
            }
        }

        return MenuMapper.INSTANCE.toRespMenu(menu);
    }

    @Override
    public boolean deleteMenuItem(String id, MyJsonWebToken token) {
        if (id == null || id.isEmpty() || token == null) return false;
        MongoCollection<MenuItem> collection = getCollection(this.COLLECTION, MenuItem.class);
        Document query = new Document("_id", new ObjectId(id));
        MenuItem menu = collection.find(query).first();
        if (menu != null) {
            AuditEntity audit = new AuditEntity();
            audit.setCodUser(token.getUser().getId());
            audit.setMail(token.getUser().getMail());
            audit.setDateUpdate(new Date());
            menu.setAudit(audit);
            menu.setStatus(false);
            collection.replaceOne(query, menu);
            return true;
        }
        return false;
    }
}
