package pillihuaman.com.pe.support.repository.inventory;

import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqInventory;
import pillihuaman.com.pe.support.repository.BaseMongoRepository;
import pillihuaman.com.pe.support.repository.warehouse.Inventory;

import java.util.List;

public interface InventoryDAO extends BaseMongoRepository<Inventory> {
    List<Inventory> listInventories(ReqInventory req);
    Inventory saveInventory(Inventory inventory, MyJsonWebToken token);
    boolean deleteInventory(MyJsonWebToken token, String id);
}