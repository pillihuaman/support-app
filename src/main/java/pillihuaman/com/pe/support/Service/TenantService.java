package pillihuaman.com.pe.support.Service;

import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqTenant;
import pillihuaman.com.pe.support.RequestResponse.dto.RespTenant;

import java.util.List;

public interface TenantService {
    List<RespTenant> listTenants(ReqTenant req);
    RespTenant saveTenant(ReqTenant req, MyJsonWebToken token);
    boolean deleteTenant(String id, MyJsonWebToken token);
    List<RespTenant> findTenantsByName(String name);
}
