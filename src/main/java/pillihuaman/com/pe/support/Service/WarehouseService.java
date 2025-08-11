package pillihuaman.com.pe.support.Service;

import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqWarehouse;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.RespWarehouse;

import java.util.List;

public interface WarehouseService {
    List<RespWarehouse> listWarehouses(ReqWarehouse req);
    RespWarehouse saveWarehouse(ReqWarehouse req, MyJsonWebToken token);
    boolean deleteWarehouse(String id, MyJsonWebToken token);
}