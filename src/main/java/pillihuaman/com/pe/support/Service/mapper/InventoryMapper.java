package pillihuaman.com.pe.support.Service.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqInventory;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.RespInventory;
import pillihuaman.com.pe.support.repository.warehouse.Inventory;

import java.util.List;


@Mapper(componentModel = "spring")
public interface InventoryMapper {
    InventoryMapper INSTANCE = Mappers.getMapper(InventoryMapper.class);
    // ====== ENTITY → DTO ======
    @Mappings({
            @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString"), // ID del inventario (Mongo _id)
            @Mapping(source = "product.id", target = "productId", qualifiedByName = "objectIdToString"),
            @Mapping(source = "warehouse.id", target = "warehouseId", qualifiedByName = "objectIdToString"),
            @Mapping(source = "quantityInStock", target = "quantityInStock"),
            @Mapping(source = "reorderLevel", target = "reorderLevel")
    })
    RespInventory toRespInventory(Inventory inventory);

    // ====== DTO → ENTITY ======
    @Mappings({
            @Mapping(source = "id", target = "id", qualifiedByName = "stringToObjectId"), // ID del inventario
            @Mapping(source = "productId", target = "product.id", qualifiedByName = "stringToObjectId"),
            @Mapping(source = "warehouseId", target = "warehouse.id", qualifiedByName = "stringToObjectId"),
            @Mapping(source = "quantityInStock", target = "quantityInStock"),
            @Mapping(source = "reorderLevel", target = "reorderLevel")
    })
    Inventory toInventoryEntity(ReqInventory req);

    // ====== CONVERTIDORES ======
    @Named("objectIdToString")
    public static String objectIdToString(ObjectId id) {
        return id != null ? id.toHexString() : null;
    }

    @Named("stringToObjectId")
    public static ObjectId stringToObjectId(String id) {
        return (id != null && !id.isBlank()) ? new ObjectId(id) : null;
    }

    List<RespInventory> toRespInventoryList(List<Inventory> inventories);
}
