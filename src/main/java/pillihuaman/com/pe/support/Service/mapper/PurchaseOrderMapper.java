package pillihuaman.com.pe.support.Service.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqPurchaseOrder;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.RespPurchaseOrder;
import pillihuaman.com.pe.support.repository.warehouse.PurchaseOrder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Mapper
public interface PurchaseOrderMapper {
    PurchaseOrderMapper INSTANCE = Mappers.getMapper(PurchaseOrderMapper.class);

    // ======= REQUEST -> ENTITY =======
    @Mappings({
            @Mapping(source = "id", target = "id", qualifiedByName = "stringToObjectId"),
            @Mapping(source = "supplierId", target = "supplier.id", qualifiedByName = "stringToObjectId"),
            @Mapping(source = "status", target = "status", qualifiedByName = "stringToBoolean"),
            @Mapping(source = "orderDate", target = "orderDate", qualifiedByName = "stringToDate")
    })
    PurchaseOrder toPurchaseOrderEntity(ReqPurchaseOrder req);

    // ======= ENTITY -> RESPONSE =======
    @Mappings({
            @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString"),
            @Mapping(source = "supplier.name", target = "supplierName"),
            @Mapping(source = "status", target = "status", qualifiedByName = "booleanToString")
    })
    RespPurchaseOrder toRespPurchaseOrder(PurchaseOrder order);

    List<RespPurchaseOrder> toRespPurchaseOrderList(List<PurchaseOrder> orders);

    // ======= CONVERSORES =======
    @Named("stringToObjectId")
    default ObjectId stringToObjectId(String id) {
        return (id != null && !id.isBlank()) ? new ObjectId(id) : null;
    }

    @Named("objectIdToString")
    default String objectIdToString(ObjectId id) {
        return id != null ? id.toHexString() : null;
    }

    @Named("stringToBoolean")
    default boolean stringToBoolean(String status) {
        return status != null && (status.equalsIgnoreCase("true") || status.equalsIgnoreCase("active"));
    }

    @Named("booleanToString")
    default String booleanToString(boolean status) {
        return status ? "true" : "false";
    }

    @Named("stringToDate")
    default Date stringToDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) return null;
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("Formato de fecha inválido. Se esperaba yyyy-MM-dd: " + dateStr);
        }
    }
}
