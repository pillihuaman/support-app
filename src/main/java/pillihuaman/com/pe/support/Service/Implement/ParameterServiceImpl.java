package pillihuaman.com.pe.support.Service.Implement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.basebd.parameter.Parameter;
import pillihuaman.com.pe.basebd.parameter.dao.implement.ParameterDaoImplement;
import pillihuaman.com.pe.basebd.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.request.ReqParameter;
import pillihuaman.com.pe.lib.response.RespBase;
import pillihuaman.com.pe.lib.response.ResponseParameter;
import pillihuaman.com.pe.support.Help.MaestrosUtilidades;
import pillihuaman.com.pe.support.Service.ParameterService;


import java.util.ArrayList;
import java.util.List;

@Component
public class ParameterServiceImpl implements ParameterService {
    @Autowired
    private ParameterDaoImplement parameterDaoImplement;


    protected final Log log = LogFactory.getLog(getClass());


    @Override
    public ResponseEntity<Object> SaveParameter(MyJsonWebToken token, ReqParameter request) {
        RespBase<ResponseParameter> response = new RespBase<ResponseParameter>();

        ModelMapper modelMapper = new ModelMapper();
        Parameter para = modelMapper.map(request, Parameter.class);
        String name = "";
        if (!MaestrosUtilidades.isEmpty(request)) {
            if (!MaestrosUtilidades.isEmpty(request.getName())) {
                name = request.getName().toUpperCase().trim();
                List<Parameter> ls = parameterDaoImplement.getParameterByName(name);
                if (ls.isEmpty()) {
                    para.setName(name);
                    parameterDaoImplement.saveParemeter(para, token);
                    String successMessage = "Resource created successfully";
                } else {
                    String errorMessage = "Parameter with name '" + name + "' already exists.";
                    return null;
                }
            }
        }

        return null;
    }

    @Override
    public RespBase<List<ResponseParameter>> getParameterbyIdCode(MyJsonWebToken token, ReqParameter request) {
        RespBase<List<ResponseParameter>> lst = new RespBase<>();
        List<ResponseParameter> lstres = new ArrayList<>();
        try {

            ModelMapper modelMapper = new ModelMapper();
            Parameter destination = modelMapper.map(request, Parameter.class);

            List<Parameter> re = parameterDaoImplement.getParameterByIdCode(destination);

            parameterDaoImplement.getParameterByIdCode(destination).stream().forEach(
                    item -> lstres.add(modelMapper.map(item, ResponseParameter.class)));
            lst.setPayload(lstres);
            return lst;
        } catch (Exception e) {
            int d = 1 / 0;
            log.error(e + " ParameterServiceImpl ");
            throw new RuntimeException(e);
        }

    }
}
