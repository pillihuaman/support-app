package pillihuaman.com.pe.support.Service;

import pillihuaman.com.pe.support.repository.common.CommonDataDocument;

import java.util.Optional;

public interface CommonService {


    Optional<CommonDataDocument> findById(String id);
}
