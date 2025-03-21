package pillihuaman.com.pe.support.Controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pillihuaman.com.pe.basebd.common.MyJsonWebToken;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.Service.UserService;

@RestController
//@RequestMapping("/user/")

public class UserController {
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private UserService userService;
	
/*
	@PostMapping(path = { Constantes.BASE_ENDPOINT +Constantes.ENDPOINT+ "/user/register" }, produces = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<RespBase<RespUser>> saveUser(
			@Valid @RequestBody ReqBase<ReqUser> request){
		
		MyJsonWebToken jwt = (MyJsonWebToken) httpServletRequest.getAttribute("jwt");
		RespBase<RespUser> response = userService.registerUser(request.getData());
		return ResponseEntity.ok(response);
	}*/

    @GetMapping(path = {Constantes.BASE_ENDPOINT + Constantes.ENDPOINT + "/user/listByStatus"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> listByStatus(@PathParam(value = "enable") boolean enable) {
        MyJsonWebToken jwt = (MyJsonWebToken) httpServletRequest.getAttribute("jwt");
        return ResponseEntity.ok(userService.listByStatus(enable));
    }


}