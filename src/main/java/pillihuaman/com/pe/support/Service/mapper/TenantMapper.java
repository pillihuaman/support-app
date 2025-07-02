package pillihuaman.com.pe.support.Service.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import pillihuaman.com.pe.lib.domain.Tenant;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqTenant;
import pillihuaman.com.pe.support.RequestResponse.dto.RespTenant;

import java.util.List;

@Mapper
public interface TenantMapper {

    TenantMapper INSTANCE = Mappers.getMapper(TenantMapper.class);

    @Mappings({
            @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString")
    })
    RespTenant toRespTenant(Tenant tenant);

    List<RespTenant> toRespTenantList(List<Tenant> tenants);

    @Mappings({
            @Mapping(source = "id", target = "id", qualifiedByName = "stringToObjectId")
    })
    Tenant toTenantEntity(ReqTenant req);

    List<Tenant> toTenantEntityList(List<ReqTenant> reqList);

    @Named("objectIdToString")
    default String objectIdToString(ObjectId id) {
        return id != null ? id.toHexString() : null;
    }

    @Named("stringToObjectId")
    default ObjectId stringToObjectId(String id) {
        return (id != null && !id.isEmpty()) ? new ObjectId(id) : null;
    }
}
