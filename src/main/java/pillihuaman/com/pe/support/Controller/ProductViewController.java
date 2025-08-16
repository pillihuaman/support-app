package pillihuaman.com.pe.support.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.common.ReqBase;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.RequestResponse.dto.RespImagenProductRank;
import pillihuaman.com.pe.support.Service.ProductViewService;
import pillihuaman.com.pe.support.JwtService;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqProductView;
import pillihuaman.com.pe.support.RequestResponse.dto.RespProductView;

import java.util.List;

@RestController
@RequestMapping(Constantes.BASE_ENDPOINT + Constantes.ENDPOINT + "/product-view")
public class ProductViewController {

    @Autowired
    private ProductViewService productViewService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RespBase<RespProductView>> saveView(@Valid @RequestBody ReqBase<ReqProductView> request) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(httpServletRequest.getHeader("Authorization"));
        return ResponseEntity.ok(productViewService.saveView(token, request));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RespBase<List<RespImagenProductRank>>> getAllViews() {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(httpServletRequest.getHeader("Authorization"));
        return ResponseEntity.ok(productViewService.getViews());
    }

        @GetMapping(path = "/by-productById/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<RespBase<RespImagenProductRank>> getProductByProductId(@PathVariable String productId) {
            MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(httpServletRequest.getHeader("Authorization"));
            return ResponseEntity.ok(productViewService.getViewsByIdProduct(productId));
        }



    @GetMapping(path = "/by-product", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RespBase<List<RespProductView>>> getViewsByProduct(@RequestParam String userId) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(httpServletRequest.getHeader("Authorization"));
        return ResponseEntity.ok(productViewService.getViewsByUserId(token, userId));
    }


    @GetMapping(path = "/by-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RespBase<List<RespProductView>>> getViewsByUser(@RequestParam String userId) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(httpServletRequest.getHeader("Authorization"));
        return ResponseEntity.ok(productViewService.getViewsByUserId(token, userId));
    }

    @GetMapping(path = "/top", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RespBase<List<RespProductView>>> getTopViewedProducts(@RequestParam(defaultValue = "10") int limit) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(httpServletRequest.getHeader("Authorization"));
        return ResponseEntity.ok(productViewService.getTopViewedProducts(token, limit));
    }
}
