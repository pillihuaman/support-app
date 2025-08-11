package pillihuaman.com.pe.support.repository.inventory;

import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqPurchaseOrderItem;
import pillihuaman.com.pe.support.repository.BaseMongoRepository;
import pillihuaman.com.pe.support.repository.warehouse.PurchaseOrderItem;

import java.util.List;

public interface PurchaseOrderItemDAO extends BaseMongoRepository<PurchaseOrderItem> {
    List<PurchaseOrderItem> listItems(ReqPurchaseOrderItem req);
    PurchaseOrderItem saveItem(PurchaseOrderItem item, MyJsonWebToken token);
    boolean deleteItem(MyJsonWebToken token, String id);
}
