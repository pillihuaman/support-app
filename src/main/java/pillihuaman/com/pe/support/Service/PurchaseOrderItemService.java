package pillihuaman.com.pe.support.Service;

import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqPurchaseOrderItem;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.RespPurchaseOrderItem;

import java.util.List;

public interface PurchaseOrderItemService {
    List<RespPurchaseOrderItem> listItems(ReqPurchaseOrderItem req);
    RespPurchaseOrderItem saveItem(ReqPurchaseOrderItem req, MyJsonWebToken token);
    boolean deleteItem(String id, MyJsonWebToken token);
}
