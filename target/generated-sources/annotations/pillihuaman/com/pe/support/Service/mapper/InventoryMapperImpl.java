package pillihuaman.com.pe.support.Service.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqInventory;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.RespInventory;
import pillihuaman.com.pe.support.repository.product.Product;
import pillihuaman.com.pe.support.repository.warehouse.Inventory;
import pillihuaman.com.pe.support.repository.warehouse.Warehouse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-15T21:45:48-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Amazon.com Inc.)"
)
@Component
public class InventoryMapperImpl implements InventoryMapper {

    @Override
    public RespInventory toRespInventory(Inventory inventory) {
        if ( inventory == null ) {
            return null;
        }

        RespInventory.RespInventoryBuilder respInventory = RespInventory.builder();

        respInventory.id( InventoryMapper.objectIdToString( inventory.getId() ) );
        respInventory.productId( InventoryMapper.objectIdToString( inventoryProductId( inventory ) ) );
        respInventory.warehouseId( InventoryMapper.objectIdToString( inventoryWarehouseId( inventory ) ) );
        respInventory.quantityInStock( inventory.getQuantityInStock() );
        respInventory.reorderLevel( inventory.getReorderLevel() );

        return respInventory.build();
    }

    @Override
    public Inventory toInventoryEntity(ReqInventory req) {
        if ( req == null ) {
            return null;
        }

        Inventory.InventoryBuilder inventory = Inventory.builder();

        inventory.product( reqInventoryToProduct( req ) );
        inventory.warehouse( reqInventoryToWarehouse( req ) );
        inventory.id( InventoryMapper.stringToObjectId( req.getId() ) );
        inventory.quantityInStock( req.getQuantityInStock() );
        inventory.reorderLevel( req.getReorderLevel() );

        return inventory.build();
    }

    @Override
    public List<RespInventory> toRespInventoryList(List<Inventory> inventories) {
        if ( inventories == null ) {
            return null;
        }

        List<RespInventory> list = new ArrayList<RespInventory>( inventories.size() );
        for ( Inventory inventory : inventories ) {
            list.add( toRespInventory( inventory ) );
        }

        return list;
    }

    private ObjectId inventoryProductId(Inventory inventory) {
        if ( inventory == null ) {
            return null;
        }
        Product product = inventory.getProduct();
        if ( product == null ) {
            return null;
        }
        ObjectId id = product.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private ObjectId inventoryWarehouseId(Inventory inventory) {
        if ( inventory == null ) {
            return null;
        }
        Warehouse warehouse = inventory.getWarehouse();
        if ( warehouse == null ) {
            return null;
        }
        ObjectId id = warehouse.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected Product reqInventoryToProduct(ReqInventory reqInventory) {
        if ( reqInventory == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.id( InventoryMapper.stringToObjectId( reqInventory.getProductId() ) );

        return product.build();
    }

    protected Warehouse reqInventoryToWarehouse(ReqInventory reqInventory) {
        if ( reqInventory == null ) {
            return null;
        }

        Warehouse.WarehouseBuilder warehouse = Warehouse.builder();

        warehouse.id( InventoryMapper.stringToObjectId( reqInventory.getWarehouseId() ) );

        return warehouse.build();
    }
}
