package pillihuaman.com.pe.support.Service.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqPurchaseOrder;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.RespPurchaseOrder;
import pillihuaman.com.pe.support.repository.supplier.Supplier;
import pillihuaman.com.pe.support.repository.warehouse.PurchaseOrder;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-15T21:45:48-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Amazon.com Inc.)"
)
public class PurchaseOrderMapperImpl implements PurchaseOrderMapper {

    @Override
    public PurchaseOrder toPurchaseOrderEntity(ReqPurchaseOrder req) {
        if ( req == null ) {
            return null;
        }

        PurchaseOrder.PurchaseOrderBuilder purchaseOrder = PurchaseOrder.builder();

        purchaseOrder.supplier( reqPurchaseOrderToSupplier( req ) );
        purchaseOrder.id( stringToObjectId( req.getId() ) );
        purchaseOrder.status( stringToBoolean( req.getStatus() ) );
        purchaseOrder.orderDate( stringToDate( req.getOrderDate() ) );

        return purchaseOrder.build();
    }

    @Override
    public RespPurchaseOrder toRespPurchaseOrder(PurchaseOrder order) {
        if ( order == null ) {
            return null;
        }

        RespPurchaseOrder.RespPurchaseOrderBuilder respPurchaseOrder = RespPurchaseOrder.builder();

        respPurchaseOrder.id( objectIdToString( order.getId() ) );
        respPurchaseOrder.supplierName( orderSupplierName( order ) );
        respPurchaseOrder.status( booleanToString( order.isStatus() ) );
        respPurchaseOrder.orderDate( order.getOrderDate() );

        return respPurchaseOrder.build();
    }

    @Override
    public List<RespPurchaseOrder> toRespPurchaseOrderList(List<PurchaseOrder> orders) {
        if ( orders == null ) {
            return null;
        }

        List<RespPurchaseOrder> list = new ArrayList<RespPurchaseOrder>( orders.size() );
        for ( PurchaseOrder purchaseOrder : orders ) {
            list.add( toRespPurchaseOrder( purchaseOrder ) );
        }

        return list;
    }

    protected Supplier reqPurchaseOrderToSupplier(ReqPurchaseOrder reqPurchaseOrder) {
        if ( reqPurchaseOrder == null ) {
            return null;
        }

        Supplier supplier = new Supplier();

        supplier.setId( stringToObjectId( reqPurchaseOrder.getSupplierId() ) );

        return supplier;
    }

    private String orderSupplierName(PurchaseOrder purchaseOrder) {
        if ( purchaseOrder == null ) {
            return null;
        }
        Supplier supplier = purchaseOrder.getSupplier();
        if ( supplier == null ) {
            return null;
        }
        String name = supplier.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
