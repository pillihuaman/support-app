package pillihuaman.com.pe.support.Service.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pillihuaman.com.pe.support.dto.ReqMenu;
import pillihuaman.com.pe.support.dto.RespMenu;
import pillihuaman.com.pe.support.repository.system.MenuItem;

import java.util.List;

@Mapper
public interface MenuMapper {

    MenuMapper INSTANCE = Mappers.getMapper(MenuMapper.class);

    // ====== Entity to Response DTO ======
    @Mappings({
            @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString"),
            @Mapping(source = "title", target = "title"),
            @Mapping(source = "icon", target = "icon"),
            @Mapping(source = "url", target = "url"),
            @Mapping(source = "order", target = "order"),
            @Mapping(source = "parentId", target = "parentId"),
            @Mapping(source = "systemId", target = "systemId"),
            @Mapping(source = "pageId", target = "pageId"),
            @Mapping(source = "status", target = "status"),
            @Mapping(source = "children", target = "children"),
            @Mapping(target = "page", ignore = true),
            @Mapping(target = "pagesize", ignore = true)
    })
    RespMenu toRespMenu(MenuItem menu);

    List<RespMenu> toRespMenuList(List<MenuItem> menuList);

    // ====== Request DTO to Entity ======
    @Mappings({
            @Mapping(source = "id", target = "id", qualifiedByName = "stringToObjectId"),
            @Mapping(source = "title", target = "title"),
            @Mapping(source = "url", target = "url"),
            @Mapping(source = "order", target = "order"),
            @Mapping(source = "parentId", target = "parentId"),
            @Mapping(source = "systemId", target = "systemId"),
            @Mapping(source = "pageId", target = "pageId"),
            @Mapping(source = "status", target = "status"),
            @Mapping(source = "icon", target = "icon"),
            @Mapping(source = "children", target = "children"),
            @Mapping(target = "audit", ignore = true)
    })
    MenuItem toMenuItem(ReqMenu req);

    List<MenuItem> toMenuItemList(List<ReqMenu> reqList);

    // ====== Helpers ======
    @Named("objectIdToString")
    default String objectIdToString(ObjectId id) {
        return id != null ? id.toHexString() : null;
    }

    @Named("stringToObjectId")
    default ObjectId stringToObjectId(String id) {
        return (id != null && !id.isEmpty()) ? new ObjectId(id) : null;
    }
}
