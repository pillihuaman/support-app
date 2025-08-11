package pillihuaman.com.pe.support.Service;

import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqInventory;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.RespInventory;


import java.util.List;

public interface InventoryService {
    List<RespInventory> listInventories(ReqInventory req);
    RespInventory saveInventory(ReqInventory req, MyJsonWebToken token);
    boolean deleteInventory(String id, MyJsonWebToken token);
}