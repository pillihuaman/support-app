package pillihuaman.com.pe.support.repository.supplier.dao;

import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqSupplier;
import pillihuaman.com.pe.support.RequestResponse.dto.RespSupplier;
import pillihuaman.com.pe.support.repository.BaseMongoRepository;
import pillihuaman.com.pe.support.repository.supplier.Supplier;

import java.util.List;


public interface SupplierDAO extends BaseMongoRepository<Supplier> {

    List<Supplier> listSuppliers(ReqSupplier reqSupplier);

    Supplier saveSupplier(Supplier supplier, MyJsonWebToken token);

    List<RespSupplier> findByOwnerId(String ownerId);

    boolean deleteInactiveSupplier(MyJsonWebToken token, String id);
    List<Supplier> findSuppliersByName(String name);
}