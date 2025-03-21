package pillihuaman.com.pe.support.Service;


import org.springframework.http.ResponseEntity;
import pillihuaman.com.pe.basebd.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.request.ReqParameter;
import pillihuaman.com.pe.lib.response.RespBase;
import pillihuaman.com.pe.lib.response.ResponseParameter;


import java.util.List;

public interface ParameterService {
    ResponseEntity<Object> SaveParameter(MyJsonWebToken token, ReqParameter request);

    RespBase<List<ResponseParameter>> getParameterbyIdCode(MyJsonWebToken token, ReqParameter request);
}



