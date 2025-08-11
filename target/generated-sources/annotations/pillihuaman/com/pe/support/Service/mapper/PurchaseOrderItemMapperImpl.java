package pillihuaman.com.pe.support.Service.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.bson.types.ObjectId;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqPurchaseOrderItem;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.RespPurchaseOrderItem;
import pillihuaman.com.pe.support.repository.product.Product;
import pillihuaman.com.pe.support.repository.warehouse.PurchaseOrder;
import pillihuaman.com.pe.support.repository.warehouse.PurchaseOrderItem;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-11T14:44:24-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Amazon.com Inc.)"
)
public class PurchaseOrderItemMapperImpl implements PurchaseOrderItemMapper {

    @Override
    public PurchaseOrderItem toEntity(ReqPurchaseOrderItem req) {
        if ( req == null ) {
            return null;
        }

        PurchaseOrderItem.PurchaseOrderItemBuilder purchaseOrderItem = PurchaseOrderItem.builder();

        purchaseOrderItem.purchaseOrder( reqPurchaseOrderItemToPurchaseOrder( req ) );
        purchaseOrderItem.product( reqPurchaseOrderItemToProduct( req ) );
        purchaseOrderItem.id( stringToObjectId( req.getId() ) );
        purchaseOrderItem.quantityOrdered( req.getQuantityOrdered() );
        purchaseOrderItem.unitCost( req.getUnitCost() );
        if ( req.getStatus() != null ) {
            purchaseOrderItem.status( Boolean.parseBoolean( req.getStatus() ) );
        }

        return purchaseOrderItem.build();
    }

    @Override
    public RespPurchaseOrderItem toRespPurchaseOrderItem(PurchaseOrderItem item) {
        if ( item == null ) {
            return null;
        }

        RespPurchaseOrderItem.RespPurchaseOrderItemBuilder respPurchaseOrderItem = RespPurchaseOrderItem.builder();

        respPurchaseOrderItem.id( objectIdToString( item.getId() ) );
        respPurchaseOrderItem.purchaseOrderId( objectIdToString( itemPurchaseOrderId( item ) ) );
        respPurchaseOrderItem.productId( objectIdToString( itemProductId( item ) ) );
        respPurchaseOrderItem.quantityOrdered( item.getQuantityOrdered() );
        respPurchaseOrderItem.unitCost( item.getUnitCost() );
        respPurchaseOrderItem.status( String.valueOf( item.isStatus() ) );

        return respPurchaseOrderItem.build();
    }

    @Override
    public List<RespPurchaseOrderItem> toRespPurchaseOrderItemList(List<PurchaseOrderItem> items) {
        if ( items == null ) {
            return null;
        }

        List<RespPurchaseOrderItem> list = new ArrayList<RespPurchaseOrderItem>( items.size() );
        for ( PurchaseOrderItem purchaseOrderItem : items ) {
            list.add( toRespPurchaseOrderItem( purchaseOrderItem ) );
        }

        return list;
    }

    protected PurchaseOrder reqPurchaseOrderItemToPurchaseOrder(ReqPurchaseOrderItem reqPurchaseOrderItem) {
        if ( reqPurchaseOrderItem == null ) {
            return null;
        }

        PurchaseOrder.PurchaseOrderBuilder purchaseOrder = PurchaseOrder.builder();

        purchaseOrder.id( stringToObjectId( reqPurchaseOrderItem.getPurchaseOrderId() ) );

        return purchaseOrder.build();
    }

    protected Product reqPurchaseOrderItemToProduct(ReqPurchaseOrderItem reqPurchaseOrderItem) {
        if ( reqPurchaseOrderItem == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.id( stringToObjectId( reqPurchaseOrderItem.getProductId() ) );

        return product.build();
    }

    private ObjectId itemPurchaseOrderId(PurchaseOrderItem purchaseOrderItem) {
        if ( purchaseOrderItem == null ) {
            return null;
        }
        PurchaseOrder purchaseOrder = purchaseOrderItem.getPurchaseOrder();
        if ( purchaseOrder == null ) {
            return null;
        }
        ObjectId id = purchaseOrder.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private ObjectId itemProductId(PurchaseOrderItem purchaseOrderItem) {
        if ( purchaseOrderItem == null ) {
            return null;
        }
        Product product = purchaseOrderItem.getProduct();
        if ( product == null ) {
            return null;
        }
        ObjectId id = product.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
