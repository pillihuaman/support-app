package pillihuaman.com.pe.support.Service;

import pillihuaman.com.pe.basebd.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.request.ReqBase;
import pillihuaman.com.pe.lib.response.RespBase;
import pillihuaman.com.pe.support.RequestResponse.dto.SystemRequest;
import pillihuaman.com.pe.support.RequestResponse.dto.SystemResponse;

import java.util.List;

public interface SystemService {
    RespBase<List<SystemResponse>> listSystem(int page, int pageSize, SystemRequest systemRequest);

    RespBase<Object> saveSystem(SystemRequest req, MyJsonWebToken jwt);
    RespBase<Object> systemById(Object id);
    boolean deleteSystem(Object id,MyJsonWebToken jwt);

}



