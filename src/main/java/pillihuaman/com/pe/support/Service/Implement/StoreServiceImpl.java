package pillihuaman.com.pe.support.Service.Implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.common.ReqBase;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.support.RequestResponse.RespStore;
import pillihuaman.com.pe.support.RequestResponse.dto.Mapper.MapperStore;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqStore;
import pillihuaman.com.pe.support.Service.StoreService;
import pillihuaman.com.pe.support.repository.store.Store;
import pillihuaman.com.pe.support.repository.store.dao.StoreDAO;

import java.util.List;

@Component
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreDAO storeDAO;

    @Override
    public RespBase<RespStore> saveStore(MyJsonWebToken jwt, ReqBase<ReqStore> request) {
        ReqStore reqStore = request.getData();
        Store store = MapperStore.INSTANCE.toStore(reqStore);
        Store savedStore = storeDAO.saveStore(store, jwt);
        RespStore respStore = MapperStore.INSTANCE.toRespStore(savedStore);
        return new RespBase<>(respStore);
    }

    @Override
    public RespBase<List<RespStore>> getStores(MyJsonWebToken jwt, ReqBase<ReqStore> request) {
        List<Store> stores = storeDAO.listStores(request.getData());
        if (stores.isEmpty()) {
            return new RespBase<>(null);
        }
        return new RespBase<>(stores.stream()
                .map(MapperStore.INSTANCE::toRespStore)
                .toList());
    }

    @Override
    public RespBase<List<RespStore>> listStoresByOwner(MyJsonWebToken jwt, String ownerId) {

        return new RespBase<>(null);
    }

    @Override
    public RespBase<Boolean> deleteStore(MyJsonWebToken jwt, String id) {
        boolean deleted = storeDAO.deleteInactiveStore(jwt, id);
        return new RespBase<>(deleted);
    }
}
