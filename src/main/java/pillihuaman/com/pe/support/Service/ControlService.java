package pillihuaman.com.pe.support.Service;

import pillihuaman.com.pe.basebd.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.request.ReqBase;
import pillihuaman.com.pe.lib.request.ReqControl;
import pillihuaman.com.pe.lib.response.RespBase;
import pillihuaman.com.pe.lib.response.RespControl;

import java.util.List;

public interface ControlService {

	RespBase<List<RespControl>> listControl(MyJsonWebToken token, ReqBase<ReqControl> request);

	Object saveControl(String token, ReqBase<ReqControl> request) ;
}



