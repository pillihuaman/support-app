package pillihuaman.com.pe.support.repository.common.dao;

import pillihuaman.com.pe.support.repository.BaseMongoRepository;
import pillihuaman.com.pe.support.repository.common.CommonDataDocument;
import pillihuaman.com.pe.support.repository.system.System;

import java.util.Optional;

public interface CommonDAO extends BaseMongoRepository<CommonDataDocument> {

    Optional<CommonDataDocument> findById(String id);
}
