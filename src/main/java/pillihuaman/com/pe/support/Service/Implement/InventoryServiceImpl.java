package pillihuaman.com.pe.support.Service.Implement;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqInventory;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.RespInventory;
import pillihuaman.com.pe.support.Service.InventoryService;
import pillihuaman.com.pe.support.Service.mapper.InventoryMapper;
import pillihuaman.com.pe.support.repository.inventory.InventoryDAO;
import pillihuaman.com.pe.support.repository.warehouse.Inventory;


import java.util.List;

@Component
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryDAO inventoryDAO;

    private final InventoryMapper mapper = InventoryMapper.INSTANCE;

    @Override
    public List<RespInventory> listInventories(ReqInventory req) {
        return mapper.toRespInventoryList(inventoryDAO.listInventories(req));
    }

    @Override
    public RespInventory saveInventory(ReqInventory req, MyJsonWebToken token) {
        Inventory inventory = mapper.toInventoryEntity(req);
        return mapper.toRespInventory(inventoryDAO.saveInventory(inventory, token));
    }

    @Override
    public boolean deleteInventory(String id, MyJsonWebToken token) {
        return inventoryDAO.deleteInventory(token, id);
    }
}
