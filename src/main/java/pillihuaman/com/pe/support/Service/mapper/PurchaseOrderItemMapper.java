package pillihuaman.com.pe.support.Service.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqPurchaseOrderItem;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.RespPurchaseOrderItem;
import pillihuaman.com.pe.support.repository.warehouse.PurchaseOrderItem;

import java.util.List;

@Mapper
public interface PurchaseOrderItemMapper {
    PurchaseOrderItemMapper INSTANCE = Mappers.getMapper(PurchaseOrderItemMapper.class);

    @Mappings({
            @Mapping(source = "id", target = "id", qualifiedByName = "stringToObjectId"),
            @Mapping(source = "purchaseOrderId", target = "purchaseOrder.id", qualifiedByName = "stringToObjectId"),
            @Mapping(source = "productId", target = "product.id", qualifiedByName = "stringToObjectId"),
            @Mapping(source = "quantityOrdered", target = "quantityOrdered"),
            @Mapping(source = "unitCost", target = "unitCost"),
            @Mapping(source = "status", target = "status")
    })
    PurchaseOrderItem toEntity(ReqPurchaseOrderItem req);

    @Mappings({
            @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString"),
            @Mapping(source = "purchaseOrder.id", target = "purchaseOrderId", qualifiedByName = "objectIdToString"),
            @Mapping(source = "product.id", target = "productId", qualifiedByName = "objectIdToString"),
            @Mapping(source = "quantityOrdered", target = "quantityOrdered"),
            @Mapping(source = "unitCost", target = "unitCost"),
            @Mapping(source = "status", target = "status")
    })
    RespPurchaseOrderItem toRespPurchaseOrderItem(PurchaseOrderItem item);

    List<RespPurchaseOrderItem> toRespPurchaseOrderItemList(List<PurchaseOrderItem> items);

    @Named("stringToObjectId")
    default ObjectId stringToObjectId(String id) {
        return (id != null && !id.isEmpty()) ? new ObjectId(id) : null;
    }

    @Named("objectIdToString")
    default String objectIdToString(ObjectId id) {
        return id != null ? id.toHexString() : null;
    }
}
