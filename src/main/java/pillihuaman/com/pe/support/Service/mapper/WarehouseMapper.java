// Archivo: WarehouseMapper.java
package pillihuaman.com.pe.support.Service.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqWarehouse;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.RespWarehouse;
import pillihuaman.com.pe.support.repository.warehouse.Warehouse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {
    WarehouseMapper INSTANCE = Mappers.getMapper(WarehouseMapper.class);

    @Mappings({
            // --- CORRECCIÓN 1: RESOLVER CONFLICTO DE 'ID' ---
            @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString"), // ID del Warehouse
            @Mapping(source = "company.id", target = "companyId", qualifiedByName = "objectIdToString"), // ID de la Empresa
            @Mapping(source = "company.legalName", target = "companyName"),

            // --- CORRECCIÓN 2: APLICAR CONVERSOR 'objectIdToString' ---
            @Mapping(source = "address.id", target = "addressId", qualifiedByName = "objectIdToString"),
            @Mapping(source = "address.street", target = "street"),
            @Mapping(source = "address.addressLine2", target = "addressLine2"),
            @Mapping(source = "address.city", target = "city"),
            @Mapping(source = "address.state", target = "state"),
            @Mapping(source = "address.postalCode", target = "postalCode"),
            @Mapping(source = "address.country", target = "country"),
            @Mapping(source = "address.coordinates", target = "coordinates"),
            @Mapping(source = "address.description", target = "addressDescription"),

            @Mapping(source = "contactInfo.mainPhoneNumber", target = "mainPhoneNumber"),
            @Mapping(source = "contactInfo.mainEmail", target = "mainEmail"),

            @Mapping(source = "manager.id", target = "managerId", qualifiedByName = "objectIdToString"),
    })
    RespWarehouse toRespWarehouse(Warehouse entity);

    List<RespWarehouse> toRespWarehouseList(List<Warehouse> entities);

    @Mappings({
            @Mapping(source = "id", target = "id", qualifiedByName = "stringToObjectId"),
            @Mapping(source = "warehouseCode", target = "warehouseCode"),
            @Mapping(source = "type", target = "type"),
            @Mapping(source = "operationalStatus", target = "operationalStatus"),
            @Mapping(source = "capacity", target = "capacity"),
            @Mapping(source = "capacityUnit", target = "capacityUnit"),
            @Mapping(source = "dockDoors", target = "dockDoors"),
            // Ignoramos explícitamente los que ensamblaremos manualmente
            @Mapping(target = "company", ignore = true),
            @Mapping(target = "address", ignore = true),
            @Mapping(target = "manager", ignore = true),
            @Mapping(target = "contactInfo", ignore = true),
            @Mapping(target = "audit", ignore = true),
            @Mapping(target = "status", ignore = true)
    })
    Warehouse toWarehouseEntity(ReqWarehouse req);
    // --- MÉTODOS DE CONVERSIÓN NOMBRADOS ---

    @Named("stringToObjectId")
    default ObjectId stringToObjectId(String id) {
        return (id != null && !id.trim().isEmpty()) ? new ObjectId(id) : null;
    }

    // --- CORRECCIÓN 3: AÑADIR EL MÉTODO DE CONVERSIÓN FALTANTE ---
    @Named("objectIdToString")
    default String objectIdToString(ObjectId id) {
        return id != null ? id.toHexString() : null;
    }
}