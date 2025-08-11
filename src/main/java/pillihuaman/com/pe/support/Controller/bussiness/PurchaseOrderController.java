package pillihuaman.com.pe.support.Controller.bussiness;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.common.ReqBase;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.lib.common.ResponseUtil;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.JwtService;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqPurchaseOrder;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.RespPurchaseOrder;
import pillihuaman.com.pe.support.Service.PurchaseOrderService;

import java.util.List;

@RestController
@RequestMapping(Constantes.BASE_ENDPOINT + Constantes.ENDPOINT + "/purchase-order")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping
    public ResponseEntity<RespBase<List<RespPurchaseOrder>>> listPurchaseOrders(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pagesize,
            @RequestParam(required = false) String supplierId
    ) {
        ReqPurchaseOrder req = ReqPurchaseOrder.builder()
                .supplierId(supplierId)
                .page(page)
                .pagesize(pagesize)
                .build();

        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(
                        purchaseOrderService.listPurchaseOrders(req)
                )
        );
    }

    @PostMapping
    public ResponseEntity<RespBase<RespPurchaseOrder>> savePurchaseOrder(
            @Valid @RequestBody ReqBase<ReqPurchaseOrder> req
    ) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(
                request.getHeader("Authorization")
        );

        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(
                        purchaseOrderService.savePurchaseOrder(req.getPayload(), token)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespBase<Boolean>> deletePurchaseOrder(@PathVariable String id) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(
                request.getHeader("Authorization")
        );

        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(
                        purchaseOrderService.deletePurchaseOrder(id, token)
                )
        );
    }
}
