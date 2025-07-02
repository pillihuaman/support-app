package pillihuaman.com.pe.support.repository.common.dao;

import pillihuaman.com.pe.lib.domain.Tenant;
import pillihuaman.com.pe.support.repository.BaseMongoRepository;
import pillihuaman.com.pe.support.repository.common.CommonDataDocument;

import java.util.Optional;

public interface CommonDAO extends BaseMongoRepository<CommonDataDocument> {

    Optional<CommonDataDocument> findById(String id);

    interface TenantRepository {
        Class<Tenant> provideEntityClass();
        Optional<Tenant> findByDomain(String domain);
    }
}
