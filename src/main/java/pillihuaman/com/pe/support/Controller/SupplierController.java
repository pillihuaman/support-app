package pillihuaman.com.pe.support.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.common.ReqBase;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.JwtService;
import pillihuaman.com.pe.support.Service.SupplierService;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqSupplier;
import pillihuaman.com.pe.support.RequestResponse.dto.RespSupplier;

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

        List<RespSupplier> suppliers = supplierService.listSuppliers(req);
        return ResponseEntity.ok(new RespBase<>(suppliers));
    }

    @PostMapping
    public ResponseEntity<RespBase<RespSupplier>> saveSupplier(@Valid @RequestBody ReqBase<ReqSupplier> req) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));
        RespSupplier saved = supplierService.saveSupplier(req.getData(), token);
        return ResponseEntity.ok(new RespBase<>(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespBase<Boolean>> deleteSupplier(@PathVariable String id) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));
        boolean deleted = supplierService.deleteSupplier(id, token);
        return ResponseEntity.ok(new RespBase<>(deleted));
    }

    @GetMapping("/search")
    public ResponseEntity<RespBase<List<RespSupplier>>> findSuppliersByName(@RequestParam String name) {
        List<RespSupplier> suppliers = supplierService.findSuppliersByName(name);
        return ResponseEntity.ok(new RespBase<>(suppliers));
    }
}
