package pillihuaman.com.pe.support.Service.Implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqPurchaseOrderItem;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.RespPurchaseOrderItem;
import pillihuaman.com.pe.support.Service.PurchaseOrderItemService;
import pillihuaman.com.pe.support.Service.mapper.PurchaseOrderItemMapper;
import pillihuaman.com.pe.support.repository.inventory.PurchaseOrderItemDAO;

import java.util.List;

@Component
public class PurchaseOrderItemServiceImpl implements PurchaseOrderItemService {

    @Autowired
    private PurchaseOrderItemDAO itemDAO;

    private final PurchaseOrderItemMapper mapper = PurchaseOrderItemMapper.INSTANCE;

    @Override
    public List<RespPurchaseOrderItem> listItems(ReqPurchaseOrderItem req) {
        return mapper.toRespPurchaseOrderItemList(itemDAO.listItems(req));
    }

    @Override
    public RespPurchaseOrderItem saveItem(ReqPurchaseOrderItem req, MyJsonWebToken token) {
        return mapper.toRespPurchaseOrderItem(itemDAO.saveItem(mapper.toEntity(req), token));
    }

    @Override
    public boolean deleteItem(String id, MyJsonWebToken token) {
        return itemDAO.deleteItem(token, id);
    }
}
