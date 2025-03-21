package pillihuaman.com.pe.support.Service.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pillihuaman.com.pe.basebd.system.System;
import pillihuaman.com.pe.support.RequestResponse.dto.SystemRequest;
import pillihuaman.com.pe.support.RequestResponse.dto.SystemResponse;

import java.util.List;

@Mapper
public interface SystemMapper {
    SystemMapper INSTANCE = Mappers.getMapper(SystemMapper.class);

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "stringToObjectId"),
            @Mapping(target = "audit", ignore = true),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "version", source = "version"),
            @Mapping(target = "timezone", source = "timezone"),
            @Mapping(target = "contactEmail", source = "contactEmail"),
            @Mapping(target = "supportPhone", source = "supportPhone")
    })
    System toEntity(SystemRequest request);

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "objectIdToString"),
            @Mapping(target = "mainMenu", expression = "java(entity.getMenus() != null && !entity.getMenus().isEmpty() ? entity.getMenus().get(0) : null)"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "version", source = "version"),
            @Mapping(target = "timezone", source = "timezone"),

            @Mapping(target = "contactEmail", source = "contactEmail"),
            @Mapping(target = "supportPhone", source = "supportPhone")
    })
    SystemResponse toResponse(System entity);

    // Mapping for lists of SystemRequest to System
    List<System> toEntityList(List<SystemRequest> requests);

    // Mapping for lists of System to SystemResponse
    List<SystemResponse> toResponseList(List<System> entities);

    @Named("objectIdToString")
    default String objectIdToString(ObjectId id) {
        // Return null if id is null, otherwise return the string representation
        return id != null ? id.toHexString() : null;
    }

    @Named("stringToObjectId")
    default ObjectId stringToObjectId(String id) {
        // Return null if the id is null or empty, otherwise try to convert it to ObjectId
        if (id == null || id.isEmpty()) {
            return null;
        }
        try {
            return new ObjectId(id);
        } catch (IllegalArgumentException e) {
            // Handle invalid ObjectId format by returning null or throwing an exception
            return null;
        }
    }
}
