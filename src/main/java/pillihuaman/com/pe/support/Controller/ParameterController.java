package pillihuaman.com.pe.support.Controller;


import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pillihuaman.com.pe.lib.request.ReqParameter;
import pillihuaman.com.pe.lib.response.ResponseParameter;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.Service.ParameterService;
import pillihuaman.com.pe.basebd.common.MyJsonWebToken;
import jakarta.validation.Valid;
import pillihuaman.com.pe.lib.request.ReqBase;
import pillihuaman.com.pe.lib.response.RespBase;

import java.util.List;

@RestController
//@RequestMapping("parameter/")

public class ParameterController {
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private ParameterService parameterService;
    @Autowired(required = false)
    protected final Log log = LogFactory.getLog(getClass());

    @PostMapping(path = {Constantes.BASE_ENDPOINT + Constantes.ENDPOINT + "/saveParameter"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> saveParameter(
            @PathVariable String access,
            @RequestBody ReqBase<ReqParameter> request ,HttpServletRequest jwts) throws Exception {

        MyJsonWebToken jwt = (MyJsonWebToken) httpServletRequest.getAttribute("jwt");
        ResponseEntity<Object> response = parameterService.SaveParameter(jwt, request.getData());
        return new ResponseEntity<>(response, response.getStatusCode());

    }

    @GetMapping(path = {Constantes.BASE_ENDPOINT + "/getParameterbyIdCode/idCode"}, produces = {MediaType.APPLICATION_JSON_VALUE})

    public ResponseEntity<RespBase<List<ResponseParameter>>> getParameterbyIdCode(
            @PathVariable String access,
            @Valid @RequestParam("idCode") String idCode) throws Exception {
        ReqBase<ReqParameter> request = new ReqBase<>();
        ReqParameter re = new ReqParameter();
        re.setIdCode(idCode);
        request.setData(re);
        MyJsonWebToken jwt = (MyJsonWebToken) httpServletRequest.getAttribute("jwt");

        RespBase<List<ResponseParameter>> response = parameterService.getParameterbyIdCode(jwt, request.getData());
        return ResponseEntity.ok(response);
    }

}