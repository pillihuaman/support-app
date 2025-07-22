package pillihuaman.com.pe.support.Service;

import pillihuaman.com.pe.support.RequestResponse.SaveCommonDataReq;
import pillihuaman.com.pe.support.repository.common.CommonDataDocument;

import java.util.Optional;

public interface CommonService {

    CommonDataDocument saveOrUpdate(SaveCommonDataReq req);
    Optional<CommonDataDocument> findById(String id);
}
