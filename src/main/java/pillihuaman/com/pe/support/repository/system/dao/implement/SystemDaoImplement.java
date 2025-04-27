package pillihuaman.com.pe.support.repository.system.dao.implement;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.Service.mapper.SystemMapper;
import pillihuaman.com.pe.support.dto.ReqSystemEntities;
import pillihuaman.com.pe.support.dto.RespMenuTree;
import pillihuaman.com.pe.support.dto.RespSystemEntities;
import pillihuaman.com.pe.support.repository.AzureAbstractMongoRepositoryImpl;
import pillihuaman.com.pe.support.repository.system.MenuItem;
import pillihuaman.com.pe.support.repository.system.Page;
import pillihuaman.com.pe.support.repository.system.dao.SystemDAO;

import java.util.*;
import java.util.stream.Collectors;

import pillihuaman.com.pe.support.repository.system.System;

@Component
@Repository
public class SystemDaoImplement extends AzureAbstractMongoRepositoryImpl<System> implements SystemDAO {

    public SystemDaoImplement() {
        DS_WRITE = Constantes.DW;
        COLLECTION = Constantes.COLLECTION_SYSTEM;
    }

    @Override
    public Class<System> provideEntityClass() {
        return System.class;
    }

    // ------------------------------
    // 💡 NEW METHODS FOR SYSTEM CRUD
    // ------------------------------

    @Override
    public List<RespSystemEntities> listSystems(ReqSystemEntities req) {
        MongoCollection<System> collection = getCollection(this.COLLECTION, System.class);

        List<Document> filters = new ArrayList<>();

        // Only active systems
        filters.add(new Document("status", true));

        // Filter by exact ID (ObjectId)
        if (req.getId() != null && !req.getId().isEmpty()) {
            filters.add(new Document("_id", new ObjectId(req.getId())));
        }

        // Case-insensitive search for name
        if (req.getName() != null && !req.getName().isEmpty()) {
            String regexPattern = ".*" + req.getName() + ".*";
            filters.add(new Document("name", new Document("$regex", regexPattern).append("$options", "i")));
        }

        Document query = filters.isEmpty() ? new Document() : new Document("$and", filters);

        // Pagination
        int page = (req.getPage() != null && req.getPage() > 0) ? req.getPage() : 1;
        int pageSize = (req.getPagesize() != null && req.getPagesize() > 0) ? req.getPagesize() : 10;
        int skip = (page - 1) * pageSize;

        return SystemMapper.INSTANCE.toRespSystemEntitiesList(collection.find(query)
                .sort(new Document("order", 1)) // Sort by 'order' ascending
                .skip(skip)
                .limit(pageSize)
                .into(new ArrayList<>()));
    }

    public RespSystemEntities saveSystem(ReqSystemEntities systems, MyJsonWebToken token) {
        System system = SystemMapper.INSTANCE.toSystemEntity(systems);
        if (system == null || token == null) {
            return null;
        }

        MongoCollection<System> collection = getCollection(this.COLLECTION, System.class);

        if (system.getId() == null) {
            // New system
            AuditEntity audit = new AuditEntity();
            audit.setCodUser(token.getUser().getId());
            audit.setMail(token.getUser().getMail());
            audit.setDateRegister(new Date());

            system.setStatus(true);  // Mark active
            collection.insertOne(system);
        } else {
            // Existing system
            Document query = new Document("_id", new ObjectId(system.getId().toString()));
            System existingSystem = collection.find(query).first();

            if (existingSystem != null) {
                AuditEntity audit = new AuditEntity();
                audit.setCodUser(token.getUser().getId());
                audit.setMail(token.getUser().getMail());
                audit.setDateUpdate(new Date());

                system.setStatus(true); // ensure still active
                collection.replaceOne(query, system);
            }
        }

        return SystemMapper.INSTANCE.toRespSystemEntities(system);
    }

    public boolean deleteSystemById(String id, MyJsonWebToken token) {
        if (id == null || id.isEmpty() || token == null) {
            return false;
        }
        MongoCollection<System> collection = getCollection(this.COLLECTION, System.class);
        Document query = new Document("_id", new ObjectId(id));
        System system = collection.find(query).first();

        if (system != null) {
            AuditEntity audit = new AuditEntity();
            audit.setCodUser(token.getUser().getId());
            audit.setMail(token.getUser().getMail());
            audit.setDateUpdate(new Date());

            system.setStatus(false); // Soft delete
            collection.replaceOne(query, system);

            return true;
        }

        return false;
    }

    @Override
    public List<RespSystemEntities> searchSystemEntitiesLineal(ReqSystemEntities req) {
        MongoCollection<System> systemCollection = getCollection(Constantes.COLLECTION_SYSTEM, System.class);
        MongoCollection<Page> pageCollection = getCollection(Constantes.COLLECTION_PAGE, Page.class);
        MongoCollection<MenuItem> menuCollection = getCollection(Constantes.COLLECTION_MENU_ITEM, MenuItem.class);

        List<RespSystemEntities> result = new ArrayList<>();
        List<String> systemIds = new ArrayList<>();
        List<System> systems = new ArrayList<>();
        List<Page> pages = new ArrayList<>();
        List<MenuItem> menus = new ArrayList<>();

        boolean noFilters = (req.getId() == null || req.getId().isEmpty()) &&
                (req.getName() == null || req.getName().isEmpty()) &&
                (req.getPageName() == null || req.getPageName().isEmpty()) &&
                (req.getPageUrl() == null || req.getPageUrl().isEmpty()) &&
                (req.getMenuTitle() == null || req.getMenuTitle().isEmpty()) &&
                (req.getMenuUrl() == null || req.getMenuUrl().isEmpty());

        if (noFilters) {
            systems = systemCollection.find(new Document("status", true))
                    .sort(new Document("audit.dateRegister", -1))
                    .limit(50)
                    .into(new ArrayList<>());

            systemIds = systems.stream()
                    .map(s -> s.getId() != null ? s.getId().toString() : null)
                    .filter(Objects::nonNull)
                    .toList();

            pages = pageCollection.find(new Document("systemId", new Document("$in", systemIds))
                    .append("status", true)).into(new ArrayList<>());

            menus = menuCollection.find(new Document("systemId", new Document("$in", systemIds))
                    .append("status", true)).into(new ArrayList<>());
        } else {
            // Filtros por parámetros
            // Código de filtros de búsqueda aquí...
        }

        // Agrupar menús por systemId
        Map<String, List<MenuItem>> menusBySystemId = menus.stream()
                .filter(m -> m.getSystemId() != null && !m.getSystemId().isEmpty())
                .collect(Collectors.groupingBy(MenuItem::getSystemId));

        for (System sys : systems) {
            String sid = sys.getId() != null ? sys.getId().toString() : null;
            if (sid == null) continue;

            // Filtrar páginas por systemId
            List<Page> sysPages = pages.stream()
                    .filter(p -> sid.equals(p.getSystemId()))
                    .toList();

            // Filtrar menús por systemId
            List<MenuItem> sysMenus = menusBySystemId.getOrDefault(sid, new ArrayList<>());

            if (sysPages.isEmpty() && sysMenus.isEmpty()) {
                result.add(RespSystemEntities.builder()
                        .systemId(sid)
                        .systemName(Optional.ofNullable(sys.getName()).orElse(""))
                        .systemDescription(Optional.ofNullable(sys.getDescription()).orElse(""))
                        .build());
                continue;
            }

            if (!sysPages.isEmpty()) {
                for (Page pg : sysPages) {
                    // Filtrar menús por pageId de la página
                    List<MenuItem> menusOfPage = sysMenus.stream()
                            .filter(m -> pg.getId().toString().equals(m.getPageId()))
                            .collect(Collectors.toList());

                    if (menusOfPage.isEmpty()) {
                        result.add(RespSystemEntities.builder()
                                .systemId(sid)
                                .systemName(Optional.ofNullable(sys.getName()).orElse(""))
                                .systemDescription(Optional.ofNullable(sys.getDescription()).orElse(""))
                                .pageId(pg.getId() != null ? pg.getId().toString() : "")
                                .pageName(Optional.ofNullable(pg.getName()).orElse(""))
                                .pageUrl(Optional.ofNullable(pg.getUrl()).orElse(""))
                                .build());
                    } else {
                        for (MenuItem mn : menusOfPage) {
                            result.add(RespSystemEntities.builder()
                                    .systemId(sid)
                                    .systemName(Optional.ofNullable(sys.getName()).orElse(""))
                                    .systemDescription(Optional.ofNullable(sys.getDescription()).orElse(""))
                                    .pageId(pg.getId() != null ? pg.getId().toString() : "")
                                    .pageName(Optional.ofNullable(pg.getName()).orElse(""))
                                    .pageUrl(Optional.ofNullable(pg.getUrl()).orElse(""))
                                    .menuId(mn.getId() != null ? mn.getId().toString() : "")
                                    .menuTitle(Optional.ofNullable(mn.getTitle()).orElse(""))
                                    .menuUrl(Optional.ofNullable(mn.getUrl()).orElse(""))
                                    .build());
                        }
                    }
                }
            } else {
                // Menús sin páginas
                for (MenuItem mn : sysMenus) {
                    result.add(RespSystemEntities.builder()
                            .systemId(sid)
                            .systemName(Optional.ofNullable(sys.getName()).orElse(""))
                            .systemDescription(Optional.ofNullable(sys.getDescription()).orElse(""))
                            .menuId(mn.getId() != null ? mn.getId().toString() : "")
                            .menuTitle(Optional.ofNullable(mn.getTitle()).orElse(""))
                            .menuUrl(Optional.ofNullable(mn.getUrl()).orElse(""))
                            .build());
                }
            }
        }

        return result;
    }


    @Override
    public List<RespMenuTree> listSystemRespMenuTree() {
        MongoCollection<System> systemCollection = getCollection(Constantes.COLLECTION_SYSTEM, System.class);
        MongoCollection<Page> pageCollection = getCollection(Constantes.COLLECTION_PAGE, Page.class);
        MongoCollection<MenuItem> menuCollection = getCollection(Constantes.COLLECTION_MENU_ITEM, MenuItem.class);

        // 1. Leer sistemas activos
        List<System> systems = systemCollection.find(new Document("status", true))
                .sort(new Document("audit.dateRegister", -1))
                .into(new ArrayList<>());

        // 2. Leer páginas activas
        List<Page> pages = pageCollection.find(new Document("status", true))
                .into(new ArrayList<>());

        // 3. Leer menús activos
        List<MenuItem> menus = menuCollection.find(new Document("status", true))
                .into(new ArrayList<>());

        // Mapear sistemas a páginas y menús por sus IDs
        Map<String, List<Page>> pagesBySystemId = pages.stream()
                .filter(p -> p.getSystemId() != null)
                .collect(Collectors.groupingBy(Page::getSystemId));

        Map<String, List<MenuItem>> menusByPageId = menus.stream()
                .filter(m -> m.getPageId() != null && !m.getPageId().isEmpty())
                .collect(Collectors.groupingBy(MenuItem::getPageId));

        Map<String, List<MenuItem>> menusBySystemId = menus.stream()
                .filter(m -> m.getSystemId() != null && (m.getPageId() == null || m.getPageId().isEmpty()))
                .collect(Collectors.groupingBy(MenuItem::getSystemId));

        Map<String, Page> pageById = pages.stream()
                .filter(p -> p.getId() != null)
                .collect(Collectors.toMap(p -> p.getId().toString(), p -> p));

        List<RespMenuTree> systemTreeList = new ArrayList<>();

        for (System system : systems) {
            String systemId = system.getId() != null ? system.getId().toString() : "";

            RespMenuTree systemNode = RespMenuTree.builder()
                    .type("system")
                    .id(systemId)
                    .parentId(null)
                    .linkParent(null)
                    .title(system.getName())
                    .icon(system.getIcon())
                    .link(null)
                    .expanded(true)  // Asumiendo que el sistema estará expandido por defecto
                    .home(false)     // Por defecto, no es el item principal
                    .group(false)    // No es un grupo, sino un item
                    .pathMatch("prefix") // Se usa "prefix" para hacer coincidir parcialmente las URLs
                    .skipLocationChange(false)
                    .children(new ArrayList<>())
                    .build();

            List<Page> systemPages = pagesBySystemId.getOrDefault(systemId, new ArrayList<>());

            for (Page page : systemPages) {
                String pageId = page.getId() != null ? page.getId().toString() : "";
                String pageUrl = page.getUrl(); // La URL que usaremos como linkParent

                RespMenuTree pageNode = RespMenuTree.builder()
                        .type("page")
                        .id(pageId)
                        .parentId(systemId)
                        .linkParent(page.getUrl())
                        .title(page.getName())
                        .icon(page.getIcon())
                        .link(null)  // Solo menús tienen link
                        .expanded(false)  // Por defecto, las páginas no están expandidas
                        .home(false)
                        .group(false)
                        .pathMatch("full") // Las páginas deben tener una coincidencia completa
                        .skipLocationChange(false)
                        .children(new ArrayList<>())
                        .build();

                List<MenuItem> pageMenus = menusByPageId.getOrDefault(pageId, new ArrayList<>());

                for (MenuItem menu : pageMenus) {
                    RespMenuTree menuNode = RespMenuTree.builder()
                            .type("menu")
                            .id(menu.getId() != null ? menu.getId().toString() : "")
                            .parentId(pageId)
                            .linkParent(pageUrl)  // El linkParent es la URL de la página
                            .title(menu.getTitle())
                            .icon(menu.getIcon())
                            .link(menu.getUrl())  // link propio del menú
                            .expanded(false)  // Menú no expandido por defecto
                            .hidden(false)    // Menú visible por defecto
                            .home(false)
                            .group(false)
                            .pathMatch("full")
                            .skipLocationChange(false)
                            .children(new ArrayList<>())
                            .build();
                    pageNode.getChildren().add(menuNode);
                }

                systemNode.getChildren().add(pageNode);
            }

            // Menús que están directamente colgados del sistema
            List<MenuItem> systemMenus = menusBySystemId.getOrDefault(systemId, new ArrayList<>());
            for (MenuItem menu : systemMenus) {
                RespMenuTree menuNode = RespMenuTree.builder()
                        .type("menu")
                        .id(menu.getId() != null ? menu.getId().toString() : "")
                        .parentId(systemId)
                        .linkParent(null)  // No tiene página padre, por eso null
                        .title(menu.getTitle())
                        .icon(menu.getIcon())
                        .link(menu.getUrl())
                        .expanded(false)  // Menú no expandido por defecto
                        .hidden(false)
                        .home(false)
                        .group(false)
                        .pathMatch("full")
                        .skipLocationChange(false)
                        .children(new ArrayList<>())
                        .build();
                systemNode.getChildren().add(menuNode);
            }

            systemTreeList.add(systemNode);
        }

        return systemTreeList;
    }


}
