package pillihuaman.com.pe.support.Service.Implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqTenant;
import pillihuaman.com.pe.support.RequestResponse.dto.RespTenant;
import pillihuaman.com.pe.support.Service.TenantService;
import pillihuaman.com.pe.support.Service.mapper.TenantMapper;
import pillihuaman.com.pe.lib.domain.Tenant;
import pillihuaman.com.pe.support.repository.tenant.dao.TenantDAO;

import java.util.List;

@Service
public class TenantServiceImpl implements TenantService {

    @Autowired
    private TenantDAO tenantDAO;

    private final TenantMapper mapper = TenantMapper.INSTANCE;

    @Override
    public List<RespTenant> listTenants(ReqTenant req) {
        return mapper.toRespTenantList(tenantDAO.listTenants(req));
    }

    @Override
    public RespTenant saveTenant(ReqTenant req, MyJsonWebToken token) {
        Tenant tenant = mapper.toTenantEntity(req);
        Tenant saved = tenantDAO.saveTenant(tenant, token);
        return mapper.toRespTenant(saved);
    }

    @Override
    public boolean deleteTenant(String id, MyJsonWebToken token) {
        return tenantDAO.deleteInactiveTenant(token, id);
    }

    @Override
    public List<RespTenant> findTenantsByName(String name) {
        return tenantDAO.findByOwnerId(name);
    }
}
