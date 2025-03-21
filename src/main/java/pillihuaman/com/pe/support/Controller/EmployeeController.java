package pillihuaman.com.pe.support.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pillihuaman.com.pe.lib.request.ReqBase;
import pillihuaman.com.pe.lib.request.ReqEmployee;
import pillihuaman.com.pe.lib.response.RespBase;
import pillihuaman.com.pe.lib.response.RespEmployee;
import pillihuaman.com.pe.lib.security.JwtService;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.Service.EmployeeService;

import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtService jwtService;

    // Método para guardar o actualizar un empleado
    @PostMapping(path = {Constantes.BASE_ENDPOINT + Constantes.ENDPOINT + "/employee"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RespBase<RespEmployee>> saveEmployee(
            @Valid @RequestBody ReqBase<ReqEmployee> request, HttpServletRequest httpServletRequest) {

		/* System.out.println("HTTP Method: " + httpServletRequest.getMethod()); // Método HTTP (GET, POST, etc.)
		System.out.println("Request URI: " + httpServletRequest.getRequestURI()); // URI de la solicitud
		System.out.println("Request URL: " + httpServletRequest.getRequestURL()); // URL completa de la solicitud
		System.out.println("Request Scheme: " + httpServletRequest.getScheme()); // Esquema (http, https)
		System.out.println("Request Remote Address: " + httpServletRequest.getRemoteAddr()); // Dirección IP del cliente
		System.out.println("Request Remote Host: " + httpServletRequest.getRemoteHost()); // Nombre del host del cliente
		System.out.println("Request Remote Port: " + httpServletRequest.getRemotePort()); // Puerto del cliente
		System.out.println("Request Server Name: " + httpServletRequest.getServerName()); // Nombre del servidor
		System.out.println("Request Server Port: " + httpServletRequest.getServerPort()); // Puerto del servidor
		MyJsonWebToken jwt = (MyJsonWebToken) httpServletRequest.getAttribute("Authorization");
		// Imprimir los encabezados de la solicitud
		System.out.println("Request Headers:");
		httpServletRequest.getHeaderNames().asIterator().forEachRemaining(header ->
				System.out.println(header + ": " + httpServletRequest.getHeader(header))
		);

		// Imprimir parámetros de la solicitud (si los hay)
		System.out.println("Request Parameters:");
		httpServletRequest.getParameterMap().forEach((key, value) ->
				System.out.println(key + ": " + String.join(", ", value))
		);

		// Extraer el JWT de las cabeceras (si se pasa como Bearer Token)
		String token = httpServletRequest.getHeader("Authorization");
		System.out.println("Authorization Header: " + token); // Imprime el token JWT*/




        return ResponseEntity.ok(employeeService.saveEmployee(jwtService.parseTokenToMyJsonWebToken(httpServletRequest.getHeader("Authorization")), request));
    }

    // Método para obtener un empleado
    @GetMapping(path = {Constantes.BASE_ENDPOINT + Constantes.ENDPOINT + "/employee"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RespBase<List<RespEmployee>>> getEmployee(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pagesize,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String document) {
        ReqEmployee request = new ReqEmployee();
        request.setPage(page);
        request.setPagesize(pagesize);
        request.setId(id);
        request.setName(name);
        request.setLastName(lastName);
        request.setDocument(document);
        ReqBase<ReqEmployee> requst = new ReqBase<>();
        requst.setData(request);
        RespBase<List<RespEmployee>> response = employeeService.getEmployee(jwtService.parseTokenToMyJsonWebToken(httpServletRequest.getHeader("Authorization")), requst);
        return ResponseEntity.ok(response);
    }

    // Método para listar empleados de un usuario
    @GetMapping(path = {Constantes.BASE_ENDPOINT + "/employee/listEmployees"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RespBase<List<RespEmployee>>> listEmployeesByUser(
            @PathVariable String access,
            @Valid @RequestBody ReqBase<ReqEmployee> request) {
        //RespBase<List<RespEmployee>> response = employeeService.listEmployeesByUser(null, request);
        return ResponseEntity.ok(null);
    }
}
