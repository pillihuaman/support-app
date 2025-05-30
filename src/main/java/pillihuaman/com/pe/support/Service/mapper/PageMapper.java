package pillihuaman.com.pe.support.Service.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqPage;
import pillihuaman.com.pe.support.RequestResponse.dto.RespPage;
import pillihuaman.com.pe.support.repository.system.Page;

import java.util.List;

    @Mapper
    public interface PageMapper {

        PageMapper INSTANCE = Mappers.getMapper(PageMapper.class);

        // ====== Entity to Response DTO ======
        @Mappings({
                @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString"),
                @Mapping(source = "name", target = "name"),
                @Mapping(source = "url", target = "url"),
                @Mapping(source = "component", target = "component"),
                @Mapping(source = "systemId", target = "systemId"),
                @Mapping(source = "permissions", target = "permissions"),
                @Mapping(source = "status", target = "status"),
                @Mapping(target = "page", ignore = true),
                @Mapping(target = "pagesize", ignore = true)
        })
        RespPage toRespPage(Page page);

        List<RespPage> toRespPageList(List<Page> pages);

        // ====== Request DTO to Entity ======
        @Mappings({
                @Mapping(source = "id", target = "id", qualifiedByName = "stringToObjectId"),
                @Mapping(source = "name", target = "name"),
                @Mapping(source = "url", target = "url"),
                @Mapping(source = "component", target = "component"),
                @Mapping(source = "systemId", target = "systemId"),
                @Mapping(source = "icon", target = "icon"),
                @Mapping(source = "permissions", target = "permissions"),
                @Mapping(source = "status", target = "status"),
                @Mapping(target = "audit", ignore = true)
        })
        Page toPageEntity(ReqPage req);

        List<Page> toPageEntityList(List<ReqPage> reqList);

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


