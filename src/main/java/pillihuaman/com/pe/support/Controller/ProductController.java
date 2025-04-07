package pillihuaman.com.pe.support.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.common.ReqBase;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.JwtService;
import pillihuaman.com.pe.support.RequestResponse.RespProduct;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqProduct;
import pillihuaman.com.pe.support.Service.ProductService;
import java.util.List;
@RestController

public class ProductController {

    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private ProductService productService;
    @Autowired
    private JwtService jwtService;

    // Método para guardar o actualizar un product
    @PostMapping(path = {Constantes.BASE_ENDPOINT + Constantes.ENDPOINT + "/product"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RespBase<RespProduct>> saveproduct(
            @Valid @RequestBody ReqBase<ReqProduct> request, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(productService.saveProduct(jwtService.parseTokenToMyJsonWebToken(httpServletRequest.getHeader("Authorization")), request));
    }

    // Método para obtener un product
    @GetMapping(path = {Constantes.BASE_ENDPOINT + Constantes.ENDPOINT + "/product"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RespBase<List<RespProduct>>> getproduct(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pagesize,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String barcode,
            @RequestParam(required = false) String category) {
        ReqProduct request = new ReqProduct();
        request.setPage(page);
        request.setPagesize(pagesize);
        request.setId(id);
        request.setName(name);
        request.setBarcode(barcode);
        request.setCategory(category);
        ReqBase<ReqProduct> requst = new ReqBase<>();
        requst.setData(request);
        RespBase<List<RespProduct>> response = productService.getProduct(
                jwtService.parseTokenToMyJsonWebToken(httpServletRequest.getHeader("Authorization")),
                requst);
        return ResponseEntity.ok(response);
    }

    // Método para listar products de un usuario
    @GetMapping(path = {Constantes.BASE_ENDPOINT  + Constantes.ENDPOINT+"/product/listproducts"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RespBase<List<RespProduct>>> listproductsByUser(
            @PathVariable String access,
            @Valid @RequestBody ReqBase<ReqProduct> request) {
        //RespBase<List<RespProduct>> response = ProductService.listproductsByUser(null, request);
        return ResponseEntity.ok(null);
    }


    @DeleteMapping(path = {Constantes.BASE_ENDPOINT + Constantes.ENDPOINT + "/product/{id}"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RespBase<String>> deleteproduct(@PathVariable String id) {
        RespBase<String> response = new RespBase<>();
        try {
            MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(httpServletRequest.getHeader("Authorization"));

            RespBase<Boolean> deletedResponse = productService.deleteProduct(token, id);

            // Extract boolean value
            boolean isDeleted = deletedResponse.getData() != null && deletedResponse.getData();

            if (isDeleted) {
                response.setData("product deleted successfully");
                return ResponseEntity.ok(response);
            } else {
                response.setData("product not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.setData("Error deleting product: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}
