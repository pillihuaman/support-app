package pillihuaman.com.pe.support.Controller.bussiness;

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
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqInventory;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.RespInventory;
import pillihuaman.com.pe.support.Service.InventoryService;

import java.util.List;

@RestController
@RequestMapping(path = Constantes.BASE_ENDPOINT + Constantes.ENDPOINT + "/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping
    public ResponseEntity<RespBase<List<RespInventory>>> listInventories(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pagesize,
            @RequestParam(required = false) String productId,
            @RequestParam(required = false) String warehouseId) {

        ReqInventory req = ReqInventory.builder()
                .productId(productId)
                .warehouseId(warehouseId)
                .page(page)
                .pagesize(pagesize)
                .build();

        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(inventoryService.listInventories(req))
        );
    }

    @PostMapping
    public ResponseEntity<RespBase<RespInventory>> saveInventory(
            @Valid @RequestBody ReqBase<ReqInventory> req) {

        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));

        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(inventoryService.saveInventory(req.getPayload(), token))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespBase<Boolean>> deleteInventory(@PathVariable String id) {

        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));

        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(inventoryService.deleteInventory(id, token))
        );
    }
}
