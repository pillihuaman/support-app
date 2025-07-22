package pillihuaman.com.pe.support.Controller.bussiness;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.JwtService;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqQuotation;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.RespQuotation;
import pillihuaman.com.pe.support.Service.QuotationService;

import java.util.List;

@RestController
public class QuotationController {

    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private QuotationService quotationService;
    @Autowired
    private JwtService jwtService;

    /**
     * =========================================================================================
     * ENDPOINT DE CREACIÓN (CÓDIGO COMPLETO Y CORREGIDO)
     * =========================================================================================
     * Recibe la entidad ReqQuotation directamente gracias a que Angular envía un Blob con
     * el content-type 'application/json'. Spring Boot maneja la deserialización automáticamente.
     */
    @PostMapping(
            path = {Constantes.BASE_ENDPOINT + Constantes.ENDPOINT + "/quotations"},
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE} // Acepta peticiones con datos y archivos
    )
    public ResponseEntity<RespBase<RespQuotation>> createQuotation(
            // --- CAMBIO CLAVE: El framework convierte la data en un objeto Java ---
            @RequestPart("quotationData") ReqQuotation reqQuotation,
            @RequestPart(value = "logo", required = false) MultipartFile logoFile,
            @RequestPart(value = "referenceImages", required = false) List<MultipartFile> referenceImages
    ) {
        try {
            // La deserialización ya fue hecha por Spring. ¡No se necesita parsear un String!

            MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(httpServletRequest.getHeader("Authorization"));

            // Llamamos al servicio con el objeto DTO ya listo
            RespBase<RespQuotation> response = quotationService.createQuotation(
                    reqQuotation,
                    logoFile,
                    referenceImages,
                    token,
                    httpServletRequest.getHeader("Authorization")
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Manejo de errores internos durante el procesamiento
            RespBase.Status.Error error = RespBase.Status.Error.builder()
                    .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .messages(List.of("Error interno al procesar la cotización: " + e.getMessage()))
                    .build();
            RespBase<RespQuotation> errorResponse = new RespBase<>();
            errorResponse.setStatus(new RespBase.Status(false, error));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


        /**
         * =========================================================================================
         * ENDPOINT DE ACTUALIZACIÓN (NUEVO)
         * =========================================================================================
         */
        @PutMapping(
                path = {Constantes.BASE_ENDPOINT + Constantes.ENDPOINT + "/quotations/{id}"},
                consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
        )
        public ResponseEntity<RespBase<RespQuotation>> updateQuotation(
                @PathVariable String id,
                @RequestPart("quotationData") ReqQuotation reqQuotation,
                @RequestPart(value = "logoFile", required = false) MultipartFile logoFile,
                @RequestPart(value = "referenceImages", required = false) List<MultipartFile> referenceImages,
                @RequestParam(value = "filesToDelete", required = false) List<String> filesToDelete
        ) {
            try {
                MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(httpServletRequest.getHeader("Authorization"));

                RespBase<RespQuotation> response = quotationService.updateQuotation(
                        id,
                        reqQuotation,
                        logoFile,
                        referenceImages,
                        filesToDelete,
                        token,
                        httpServletRequest.getHeader("Authorization")
                );

                return ResponseEntity.ok(response);

            } catch (Exception e) {
                // ... (mismo manejo de errores que en createQuotation)
                RespBase.Status.Error error = RespBase.Status.Error.builder()
                        .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .messages(List.of("Error interno al actualizar la cotización: " + e.getMessage()))
                        .build();
                RespBase<RespQuotation> errorResponse = new RespBase<>();
                errorResponse.setStatus(new RespBase.Status(false, error));
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
        }

    /**
     * Endpoint para obtener una cotización por su ID.
     */
    @GetMapping(path = {Constantes.BASE_ENDPOINT + Constantes.ENDPOINT + "/quotations/{id}"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RespBase<RespQuotation>> getQuotationById(@PathVariable String id) {
        RespBase<RespQuotation> response = quotationService.getQuotationById(id,httpServletRequest.getHeader("Authorization"));
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para listar todas las cotizaciones.
     */
    @GetMapping(path = {Constantes.BASE_ENDPOINT + Constantes.ENDPOINT + "/quotations"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RespBase<List<RespQuotation>>> listAllQuotations() {
        RespBase<List<RespQuotation>> response = quotationService.getAllQuotations(httpServletRequest.getHeader("Authorization"));
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para eliminar una cotización por su ID.
     */
    @DeleteMapping(path = {Constantes.BASE_ENDPOINT + Constantes.ENDPOINT + "/quotations/{id}"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RespBase<String>> deleteQuotation(@PathVariable String id) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(httpServletRequest.getHeader("Authorization"));
        RespBase<String> serviceResponse = quotationService.deleteQuotation(id, token,httpServletRequest.getHeader("Authorization"));

        if (serviceResponse.getStatus().getSuccess()) {
            return ResponseEntity.ok(serviceResponse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(serviceResponse);
        }
    }
}