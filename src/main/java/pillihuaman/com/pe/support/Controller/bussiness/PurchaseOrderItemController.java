package pillihuaman.com.pe.support.Controller.bussiness;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.common.ReqBase;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.lib.common.ResponseUtil;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.JwtService;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqPurchaseOrderItem;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.RespPurchaseOrderItem;
import pillihuaman.com.pe.support.Service.PurchaseOrderItemService;

import java.util.List;

@RestController
@RequestMapping(path = Constantes.BASE_ENDPOINT + Constantes.ENDPOINT + "/purchase-order-item")
public class PurchaseOrderItemController {

    @Autowired
    private PurchaseOrderItemService itemService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping
    public ResponseEntity<RespBase<List<RespPurchaseOrderItem>>> listItems(
            @RequestParam(required = false) String purchaseOrderId
    ) {
        ReqPurchaseOrderItem req = ReqPurchaseOrderItem.builder()
                .purchaseOrderId(purchaseOrderId)
                .build();

        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(
                        itemService.listItems(req)
                )
        );
    }

    @PostMapping
    public ResponseEntity<RespBase<RespPurchaseOrderItem>> saveItem(
            @RequestBody ReqBase<ReqPurchaseOrderItem> req
    ) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(
                request.getHeader("Authorization")
        );

        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(
                        itemService.saveItem(req.getPayload(), token)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespBase<Boolean>> deleteItem(@PathVariable String id) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(
                request.getHeader("Authorization")
        );

        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(
                        itemService.deleteItem(id, token)
                )
        );
    }
}
