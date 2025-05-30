package pillihuaman.com.pe.support.repository.control.dao;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqControl;
import pillihuaman.com.pe.support.repository.BaseMongoRepository;
import pillihuaman.com.pe.support.repository.control.Control;

import java.util.List;

public interface ControlDAO extends BaseMongoRepository<Control> {

	List<Control> listControl(ReqControl reqControl);
	Control saveControl(Control reqControl, MyJsonWebToken to);

}
