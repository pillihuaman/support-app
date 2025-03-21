package pillihuaman.com.pe.support.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pillihuaman.com.pe.lib.request.ReqBase;
import pillihuaman.com.pe.lib.response.RespBase;
import pillihuaman.com.pe.lib.security.JwtService;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.RequestResponse.dto.SystemRequest;
import pillihuaman.com.pe.support.RequestResponse.dto.SystemResponse;
import pillihuaman.com.pe.support.Service.SystemService;

import java.util.List;

@RestController
@Tag(name = "system", description = "")
public class SystemController {
    protected final Log log = LogFactory.getLog(getClass());
    @Autowired
    private SystemService systemService;

    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private JwtService jwtService;
    private final String urlBaseController = "/system";

    @Operation(summary = "Save and Update system", description = "Save and Update system", tags = {""}, security = {
            @SecurityRequirement(name = Constantes.BEARER_JWT)})
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constantes.SERVER_200, description = Constantes.OPERACION_EXITOSA),
            @ApiResponse(responseCode = Constantes.SERVER_400, description = Constantes.ERROR_VALIDACION, content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RespBase.class))}),
            @ApiResponse(responseCode = Constantes.SERVER_500, description = Constantes.ERROR_INTERNO, content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RespBase.class))})})
    @PostMapping(path = {Constantes.BASE_ENDPOINT + Constantes.ENDPOINT + urlBaseController}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> saveSystem(
            @RequestBody SystemRequest request,
            @RequestHeader("Authorization") String authorizationHeader) {
        String authorizationHeaders = httpServletRequest.getHeader("Authorization");
        for(int i=0; i<=2000;i++) {
            request.setName("nombre n"+i+"");
            systemService.saveSystem(request, jwtService.parseTokenToMyJsonWebToken(httpServletRequest.getHeader("Authorization")));
        }
        return ResponseEntity.ok(null);
    }


    @Operation(summary = "Delete system", description = "Delete the current system configuration", tags = {""}, security = {
            @SecurityRequirement(name = Constantes.BEARER_JWT)})
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constantes.SERVER_200, description = Constantes.OPERACION_EXITOSA),
            @ApiResponse(responseCode = Constantes.SERVER_500, description = Constantes.ERROR_INTERNO, content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RespBase.class))})})
    @DeleteMapping(path = {Constantes.BASE_ENDPOINT + Constantes.ENDPOINT + urlBaseController + "/delete/id/{systemId}"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Boolean> deleteSystem(@PathVariable String systemId) {

        return ResponseEntity.ok(systemService.deleteSystem(systemId, jwtService.parseTokenToMyJsonWebToken(httpServletRequest.getHeader("Authorization"))));
    }

    @Operation(summary = "Get system by ID", description = "Retrieve system configuration by ID", tags = {""}, security = {
            @SecurityRequirement(name = Constantes.BEARER_JWT)})
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constantes.SERVER_200, description = Constantes.OPERACION_EXITOSA, content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Object.class))}),
            @ApiResponse(responseCode = Constantes.SERVER_400, description = Constantes.ERROR_VALIDACION, content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RespBase.class))}),
            @ApiResponse(responseCode = Constantes.SERVER_500, description = Constantes.ERROR_INTERNO, content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RespBase.class))})})
    @GetMapping(path = {Constantes.BASE_ENDPOINT + Constantes.ENDPOINT + urlBaseController + "/id/{systemId}"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getSystemById(@PathVariable String systemId) {
        return ResponseEntity.ok(systemService.systemById(systemId));
    }

    @Operation(
            summary = "List systems",
            description = "Retrieve a paginated list of systems with optional filters",
            tags = {""},
            security = {@SecurityRequirement(name = Constantes.BEARER_JWT)}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constantes.SERVER_200, description = Constantes.OPERACION_EXITOSA, content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RespBase.class))
            }),
            @ApiResponse(responseCode = Constantes.SERVER_400, description = Constantes.ERROR_VALIDACION, content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RespBase.class))
            }),
            @ApiResponse(responseCode = Constantes.SERVER_500, description = Constantes.ERROR_INTERNO, content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RespBase.class))
            })
    })
    @GetMapping(
            path = {Constantes.BASE_ENDPOINT + Constantes.ENDPOINT + urlBaseController + "/list"},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<RespBase<List<SystemResponse>>> listSystems(
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("name") String name,
            @RequestParam("version") String version,
            @RequestParam("timezone") String timezone,
            @RequestBody(required = false) SystemRequest systemRequest,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        RespBase<List<SystemResponse>> systems=new RespBase<>();
        //  RespBase<Object> response = new RespBase<>();
        try {
            log.debug("List systems request received with page: {}, pageSize: {}, filters: {}");

            // Procesar token JWT
            jwtService.parseTokenToMyJsonWebToken(authorizationHeader);
            SystemRequest request = new SystemRequest(
                    name,
                    version,
                    timezone

            );

           systems = systemService.listSystem(page, pageSize, null);

            // Configurar respuesta
            //response.setPayload(systems);
            //response.getStatus().setSuccess(true);

            return ResponseEntity.ok(systems);
        } catch (Exception e) {
            log.error("Error in SystemController.listSystems: ", e);

            // Configurar error en respuesta
            RespBase.Status.Error error = new RespBase.Status.Error();
            error.setCode("ERR_LIST_SYSTEMS");
            error.setHttpCode("500");
            error.setMessages(List.of(e.getMessage()));

            RespBase.Status status = new RespBase.Status();
            status.setSuccess(false);
            status.setError(error);
            systems= new RespBase<>();
            systems.setStatus(status);

            return ResponseEntity.internalServerError().body(systems);
        }
    }


}
