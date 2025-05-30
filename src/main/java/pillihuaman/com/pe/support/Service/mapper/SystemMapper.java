package pillihuaman.com.pe.support.Service.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqSystemEntities;
import pillihuaman.com.pe.support.RequestResponse.dto.RespSystemEntities;
import pillihuaman.com.pe.support.repository.system.System;

import java.util.List;

@Mapper
public interface SystemMapper {

    SystemMapper INSTANCE = Mappers.getMapper(SystemMapper.class);

    // ====== Entity to Response DTO ======
    @Mappings({
            @Mapping(source = "id", target = "systemId", qualifiedByName = "objectIdToString"),
            @Mapping(source = "name", target = "systemName"),
            @Mapping(source = "description", target = "systemDescription"),
            @Mapping(target = "pageId", ignore = true),
            @Mapping(target = "pageName", ignore = true),
            @Mapping(target = "pageUrl", ignore = true),
            @Mapping(target = "menuId", ignore = true),
            @Mapping(target = "menuTitle", ignore = true),
            @Mapping(target = "menuUrl", ignore = true)
    })
    RespSystemEntities toRespSystemEntities(System system);

    List<RespSystemEntities> toRespSystemEntitiesList(List<System> systems);

    // ====== Request DTO to Entity ======
    @Mappings({
            @Mapping(source = "id", target = "id", qualifiedByName = "stringToObjectId"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "icon", target = "icon"),
            @Mapping(source = "order", target = "order"),
            @Mapping(source = "status", target = "status"),
            @Mapping(target = "audit", ignore = true)
    })
    System toSystemEntity(ReqSystemEntities req);

    List<System> toSystemEntityList(List<ReqSystemEntities> reqList);

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
