package pillihuaman.com.pe.support.Controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pillihuaman.com.pe.lib.request.ReqProduct;
import pillihuaman.com.pe.lib.response.RespProduct;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.Service.ProductService;
import pillihuaman.com.pe.basebd.common.MyJsonWebToken;
import jakarta.validation.Valid;
import pillihuaman.com.pe.lib.request.ReqBase;
import pillihuaman.com.pe.lib.response.RespBase;

@RestController

@RequestMapping("stock/")

public class StockController {
	@Autowired
	private HttpServletRequest httpServletRequest;
	@Autowired
	private ProductService productService;

	@PostMapping(path = { Constantes.BASE_ENDPOINT + "saveStock" }, produces = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<RespBase<RespProduct>> saveStock(
			@PathVariable String access,
			@Valid @RequestBody ReqBase<ReqProduct> request){
		
		MyJsonWebToken jwt = (MyJsonWebToken) httpServletRequest.getAttribute("jwt");
		RespBase<RespProduct> response = productService.SaveProduct( jwt,request);
		return ResponseEntity.ok(response);
	}
	




	    
}