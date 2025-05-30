package pillihuaman.com.pe.support.Service;

import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqSupplier;
import pillihuaman.com.pe.support.RequestResponse.dto.RespSupplier;


import java.util.List;

public interface SupplierService {

    List<RespSupplier> listSuppliers(ReqSupplier req);

    RespSupplier saveSupplier(ReqSupplier req, MyJsonWebToken token);

    boolean deleteSupplier(String id, MyJsonWebToken token);

    List<RespSupplier> findSuppliersByName(String name);

}
