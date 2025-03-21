package pillihuaman.com.pe.support.Service.Implement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.basebd.control.Control;
import pillihuaman.com.pe.basebd.control.dao.ControlDAO;
import pillihuaman.com.pe.basebd.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.exception.CustomRegistrationException;
import pillihuaman.com.pe.lib.request.ReqBase;
import pillihuaman.com.pe.lib.request.ReqControl;
import pillihuaman.com.pe.lib.response.RespBase;
import pillihuaman.com.pe.lib.response.RespControl;
import pillihuaman.com.pe.support.Service.ControlService;
import pillihuaman.com.pe.support.Service.mapper.ControlMapper;
import pillihuaman.com.pe.support.foreing.ExternalApiService;

import java.util.List;

@Component
public class ControlServiceImpl implements ControlService {
    @Autowired
    private ControlDAO controlDAO;

    protected final Log log = LogFactory.getLog(getClass());
    @Autowired
    private  ExternalApiService externalApiService;

    @Override
    public RespBase<List<RespControl>> listControl(MyJsonWebToken token, ReqBase<ReqControl> request) {
        ReqControl r = request.getData();
        ModelMapper modelMapper = new ModelMapper();
        Control para = modelMapper.map(r, Control.class);
        controlDAO.saveControl(para, token);
        return null;
    }

    @Override
    public Object saveControl(String token, ReqBase<ReqControl> request) {
        try {

            return RespBase.builder().payload(controlDAO.saveControl(ControlMapper.INSTANCE.toControl(request.getData()), null)).trace(RespBase.Trace.builder().traceId("1").build()).status(RespBase.Status.builder().success(true).error(null).build()).build();

        } catch (Exception ex) {
            throw new CustomRegistrationException("failed " + ex.getMessage());
        }
    }
}
