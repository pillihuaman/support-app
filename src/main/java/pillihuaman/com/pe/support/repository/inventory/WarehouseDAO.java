package pillihuaman.com.pe.support.repository.inventory;

import pillihuaman.com.pe.lib.common.MyJsonWebToken;

import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqWarehouse;
import pillihuaman.com.pe.support.repository.BaseMongoRepository;
import pillihuaman.com.pe.support.repository.warehouse.Warehouse;

import java.util.List;

public interface WarehouseDAO extends BaseMongoRepository<Warehouse> {
    List<Warehouse> listWarehouses(ReqWarehouse req);
    Warehouse saveWarehouse(Warehouse warehouse, MyJsonWebToken token);
    boolean deleteWarehouse(String id, MyJsonWebToken token);
}