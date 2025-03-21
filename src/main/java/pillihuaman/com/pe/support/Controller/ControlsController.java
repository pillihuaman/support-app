package pillihuaman.com.pe.support.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pillihuaman.com.pe.basebd.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.request.ReqBase;
import pillihuaman.com.pe.lib.request.ReqControl;
import pillihuaman.com.pe.lib.response.RespBase;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.Service.ControlService;

@RestController
@Tag(name = "Control", description = "")


public class ControlsController {

    @Autowired
    private ControlService controlService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Operation(summary = "Save control by system and user", description = "Save control by system and user", tags = {""}, security = {
            @SecurityRequirement(name = Constantes.BEARER_JWT)})
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constantes.SERVER_200, description = Constantes.OPERACION_EXITOSA),
            @ApiResponse(responseCode = Constantes.SERVER_400, description = Constantes.ERROR_VALIDACION, content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RespBase.class))}),
            @ApiResponse(responseCode = Constantes.SERVER_500, description = Constantes.ERROR_INTERNO, content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RespBase.class))})})
    @PostMapping(path = {Constantes.BASE_ENDPOINT + Constantes.ENDPOINT + "/control/saveControl"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> saveControl(
            @PathVariable String access,
            @Valid @RequestBody ReqBase<ReqControl> request, @RequestHeader("Authorization") String authorizationHeader,
            HttpServletRequest httpServletRequest) {
        String authorizationHeaders = httpServletRequest.getHeader("Authorization");
        return ResponseEntity.ok(controlService.saveControl(authorizationHeaders, request));
    }

    @Operation(summary = "Get list control by system and user", description = "Get list control by system and user", tags = {""}, security = {
            @SecurityRequirement(name = Constantes.BEARER_JWT)})
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constantes.SERVER_200, description = Constantes.OPERACION_EXITOSA),
            @ApiResponse(responseCode = Constantes.SERVER_400, description = Constantes.ERROR_VALIDACION, content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RespBase.class))}),
            @ApiResponse(responseCode = Constantes.SERVER_500, description = Constantes.ERROR_INTERNO, content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RespBase.class))})})
    @GetMapping(path = {Constantes.BASE_ENDPOINT + "/control/listControl"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> listControl(
            @PathVariable String access,
            @Valid @RequestBody ReqBase<ReqControl> request, HttpServletRequest jwts) {
        MyJsonWebToken jwt = (MyJsonWebToken) jwts.getAttribute("jwt");
        return ResponseEntity.ok(controlService.listControl(jwt, request));
    }

}