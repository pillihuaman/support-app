package pillihuaman.com.pe.support.Service.Implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.Service.SupplierService;
import pillihuaman.com.pe.support.Service.mapper.SupplierMapper;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqSupplier;
import pillihuaman.com.pe.support.RequestResponse.dto.RespSupplier;
import pillihuaman.com.pe.support.repository.supplier.Supplier;
import pillihuaman.com.pe.support.repository.supplier.dao.SupplierDAO;

import java.util.List;

@Component
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierDAO supplierDAO;

    private final SupplierMapper mapper = SupplierMapper.INSTANCE;

    @Override
    public List<RespSupplier> listSuppliers(ReqSupplier req) {
        List<Supplier> suppliers = supplierDAO.listSuppliers(req);
        return mapper.toRespSupplierList(suppliers);
    }

    @Override
    public RespSupplier saveSupplier(ReqSupplier req, MyJsonWebToken token) {
        Supplier supplier = mapper.toSupplierEntity(req);
        Supplier saved = supplierDAO.saveSupplier(supplier, token);
        return mapper.toRespSupplier(saved);
    }

    @Override
    public boolean deleteSupplier(String id, MyJsonWebToken token) {
        return supplierDAO.deleteInactiveSupplier(token, id);
    }

    @Override
    public List<RespSupplier> findSuppliersByName(String name) {
        List<Supplier> suppliers = supplierDAO.findSuppliersByName(name);
        return mapper.toRespSupplierList(suppliers);
    }
}
