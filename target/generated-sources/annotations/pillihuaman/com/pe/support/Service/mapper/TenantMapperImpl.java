package pillihuaman.com.pe.support.Service.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import pillihuaman.com.pe.lib.domain.Tenant;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqTenant;
import pillihuaman.com.pe.support.RequestResponse.dto.RespTenant;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-28T10:35:07-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Amazon.com Inc.)"
)
public class TenantMapperImpl implements TenantMapper {

    @Override
    public RespTenant toRespTenant(Tenant tenant) {
        if ( tenant == null ) {
            return null;
        }

        RespTenant.RespTenantBuilder respTenant = RespTenant.builder();

        respTenant.id( objectIdToString( tenant.getId() ) );
        respTenant.name( tenant.getName() );
        respTenant.domain( tenant.getDomain() );
        respTenant.active( tenant.isActive() );

        return respTenant.build();
    }

    @Override
    public List<RespTenant> toRespTenantList(List<Tenant> tenants) {
        if ( tenants == null ) {
            return null;
        }

        List<RespTenant> list = new ArrayList<RespTenant>( tenants.size() );
        for ( Tenant tenant : tenants ) {
            list.add( toRespTenant( tenant ) );
        }

        return list;
    }

    @Override
    public Tenant toTenantEntity(ReqTenant req) {
        if ( req == null ) {
            return null;
        }

        Tenant.TenantBuilder tenant = Tenant.builder();

        tenant.id( stringToObjectId( req.getId() ) );
        tenant.name( req.getName() );
        tenant.domain( req.getDomain() );
        tenant.active( req.isActive() );

        return tenant.build();
    }

    @Override
    public List<Tenant> toTenantEntityList(List<ReqTenant> reqList) {
        if ( reqList == null ) {
            return null;
        }

        List<Tenant> list = new ArrayList<Tenant>( reqList.size() );
        for ( ReqTenant reqTenant : reqList ) {
            list.add( toTenantEntity( reqTenant ) );
        }

        return list;
    }
}
