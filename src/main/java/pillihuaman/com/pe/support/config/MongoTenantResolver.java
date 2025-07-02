package pillihuaman.com.pe.support.config;


import org.springframework.stereotype.Component;
import pillihuaman.com.pe.lib.domain.Tenant;
import pillihuaman.com.pe.lib.domain.TenantResolver;
import pillihuaman.com.pe.support.repository.tenant.dao.TenantDAO;


@Component
public class MongoTenantResolver implements TenantResolver {

    private final TenantDAO tenantRepository;

    public MongoTenantResolver(TenantDAO tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public Tenant resolveByHost(String host) {
        return tenantRepository.findByDomain(host)
                .map(entity -> Tenant.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .domain(entity.getDomain())
                        .active(entity.isActive())
                        .build()
                ).orElse(null);
    }
}
