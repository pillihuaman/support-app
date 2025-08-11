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
import pillihuaman.com.pe.support.RequestResponse.dto.ReqSupplier;
import pillihuaman.com.pe.support.RequestResponse.dto.RespSupplier;
import pillihuaman.com.pe.support.Service.SupplierService;

import java.util.List;

@RestController
@RequestMapping(path = Constantes.BASE_ENDPOINT + Constantes.ENDPOINT + "/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private HttpServletRequest request;

    // ---------------- SUPPLIER ----------------

    @GetMapping
    public ResponseEntity<RespBase<List<RespSupplier>>> listSuppliers(@RequestParam(required = false) Integer page,
                                                                      @RequestParam(required = false) Integer pagesize,
                                                                      @RequestParam(required = false) String id,
                                                                      @RequestParam(required = false) String name) {

        ReqSupplier req = ReqSupplier.builder()
                .id(id)
                .name(name)
                .page(page)
                .pagesize(pagesize)
                .build();

        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(
                        supplierService.listSuppliers(req)
                )
        );
    }

    @PostMapping
    public ResponseEntity<RespBase<RespSupplier>> saveSupplier(@Valid @RequestBody ReqBase<ReqSupplier> req) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(
                        supplierService.saveSupplier(req.getPayload(), token)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespBase<Boolean>> deleteSupplier(@PathVariable String id) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(
                        supplierService.deleteSupplier(id, token)
                )
        );
    }

    @GetMapping("/search")
    public ResponseEntity<RespBase<List<RespSupplier>>> findSuppliersByName(@RequestParam String name) {
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(
                        supplierService.findSuppliersByName(name)
                )
        );
    }
}
