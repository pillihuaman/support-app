package pillihuaman.com.pe.support.Service.Implement;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqPurchaseOrder;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.RespPurchaseOrder;
import pillihuaman.com.pe.support.Service.PurchaseOrderService;
import pillihuaman.com.pe.support.Service.mapper.PurchaseOrderMapper;
import pillihuaman.com.pe.support.repository.inventory.PurchaseOrderDAO;
import pillihuaman.com.pe.support.repository.warehouse.PurchaseOrder;


import java.util.List;

@Component
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    @Autowired
    private PurchaseOrderDAO purchaseOrderDAO;

    private final PurchaseOrderMapper mapper = PurchaseOrderMapper.INSTANCE;

    @Override
    public List<RespPurchaseOrder> listPurchaseOrders(ReqPurchaseOrder req) {
        return mapper.toRespPurchaseOrderList(purchaseOrderDAO.listPurchaseOrders(req));
    }

    @Override
    public RespPurchaseOrder savePurchaseOrder(ReqPurchaseOrder req, MyJsonWebToken token) {
        PurchaseOrder entity = mapper.toPurchaseOrderEntity(req);
        return mapper.toRespPurchaseOrder(purchaseOrderDAO.savePurchaseOrder(entity, token));
    }

    @Override
    public boolean deletePurchaseOrder(String id, MyJsonWebToken token) {
        return purchaseOrderDAO.deletePurchaseOrder( id,token);
    }
}
