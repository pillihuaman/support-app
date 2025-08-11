package pillihuaman.com.pe.support.repository.inventory;

import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqPurchaseOrder;
import pillihuaman.com.pe.support.repository.BaseMongoRepository;
import pillihuaman.com.pe.support.repository.warehouse.PurchaseOrder;

import java.util.List;

public interface PurchaseOrderDAO extends BaseMongoRepository<PurchaseOrder> {
    List<PurchaseOrder> listPurchaseOrders(ReqPurchaseOrder req);
    PurchaseOrder savePurchaseOrder(PurchaseOrder po, MyJsonWebToken token);
    boolean deletePurchaseOrder(String id, MyJsonWebToken token);
}