package pillihuaman.com.pe.support.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.common.ReqBase;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.lib.common.ResponseUtil;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.JwtService;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqTenant;
import pillihuaman.com.pe.support.RequestResponse.dto.RespTenant;
import pillihuaman.com.pe.support.Service.TenantService;

import java.util.List;

@RestController
@RequestMapping(path = Constantes.BASE_ENDPOINT + Constantes.ENDPOINT + "/tenant")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping
    public ResponseEntity<RespBase<List<RespTenant>>> listTenants(@RequestParam(required = false) String id,
                                                                  @RequestParam(required = false) String name,
                                                                  @RequestParam(required = false) String domain) {
        ReqTenant req = ReqTenant.builder()
                .id(id)
                .name(name)
                .domain(domain)
                .active(true)
                .build();
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(tenantService.listTenants(req))
        );
    }

    @PostMapping
    public ResponseEntity<RespBase<RespTenant>> saveTenant(@Valid @RequestBody ReqBase<ReqTenant> req) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(tenantService.saveTenant(req.getPayload(), token))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespBase<Boolean>> deleteTenant(@PathVariable String id) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(tenantService.deleteTenant(id, token))
        );
    }

    @GetMapping("/search")
    public ResponseEntity<RespBase<List<RespTenant>>> findTenantsByName(@RequestParam String name) {
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(tenantService.findTenantsByName(name))
        );
    }
}
