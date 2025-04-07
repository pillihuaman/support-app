package pillihuaman.com.pe.support.Service;

import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.common.ReqBase;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.support.RequestResponse.RespStore;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqStore;

import java.util.List;

public interface StoreService {

	RespBase<RespStore> saveStore(MyJsonWebToken jwt, ReqBase<ReqStore> request);

	RespBase<List<RespStore>> getStores(MyJsonWebToken jwt, ReqBase<ReqStore> request);

	RespBase<List<RespStore>> listStoresByOwner(MyJsonWebToken jwt, String ownerId);

	RespBase<Boolean> deleteStore(MyJsonWebToken jwt, String id);
}
