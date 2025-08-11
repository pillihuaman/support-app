package pillihuaman.com.pe.support.Service;


import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqPurchaseOrder;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.RespPurchaseOrder;

import java.util.List;

public interface PurchaseOrderService {

    List<RespPurchaseOrder> listPurchaseOrders(ReqPurchaseOrder req);

    RespPurchaseOrder savePurchaseOrder(ReqPurchaseOrder req, MyJsonWebToken token);

    boolean deletePurchaseOrder(String id, MyJsonWebToken token);
}