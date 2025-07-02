package pillihuaman.com.pe.support.repository.tenant.dao;

import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.domain.Tenant;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqTenant;
import pillihuaman.com.pe.support.RequestResponse.dto.RespTenant;
import pillihuaman.com.pe.support.repository.BaseMongoRepository;


import java.util.List;
import java.util.Optional;


public interface TenantDAO extends BaseMongoRepository<Tenant> {

    List<Tenant> listTenants(ReqTenant reqTenant);

    Tenant saveTenant(Tenant Tenant, MyJsonWebToken token);

    List<RespTenant> findByOwnerId(String ownerId);

    boolean deleteInactiveTenant(MyJsonWebToken token, String id);
    Optional<Tenant> findByDomain(String domain);
}