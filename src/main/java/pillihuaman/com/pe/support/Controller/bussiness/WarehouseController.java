// Archivo: WarehouseController.java
package pillihuaman.com.pe.support.Controller.bussiness;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.common.ReqBase;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.lib.common.ResponseUtil;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.JwtService;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqWarehouse;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.RespWarehouse;
import pillihuaman.com.pe.support.Service.WarehouseService;

import java.util.List;

@RestController
@RequestMapping(path = Constantes.BASE_ENDPOINT + Constantes.ENDPOINT + "/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;
    private final JwtService jwtService;
    private final HttpServletRequest request;

    @GetMapping
    public ResponseEntity<RespBase<List<RespWarehouse>>> listWarehouses(
            @RequestParam(required = false) String warehouseId) {

        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));

        ReqWarehouse req = ReqWarehouse.builder()
                .id(warehouseId)
                .build();

        List<RespWarehouse> response = warehouseService.listWarehouses(req);

        return ResponseEntity.ok(ResponseUtil.buildSuccessResponse(response));
    }

    @PostMapping
    public ResponseEntity<RespBase<RespWarehouse>> saveWarehouse(@Valid @RequestBody ReqBase<ReqWarehouse> req) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));

        // AHORA ESTA LLAMADA FUNCIONA:
        RespWarehouse savedWarehouse = warehouseService.saveWarehouse(req.getPayload(), token);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseUtil.buildSuccessResponse(savedWarehouse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespBase<Boolean>> deleteWarehouse(@PathVariable String id) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));

        // AHORA ESTA LLAMADA FUNCIONA:
        boolean deleted = warehouseService.deleteWarehouse(id, token);

        return ResponseEntity.ok(ResponseUtil.buildSuccessResponse(deleted));
    }

    @GetMapping("/filter")
    public ResponseEntity<RespBase<List<RespWarehouse>>> filterWarehouses(
            @RequestParam(required = false) String warehouseId,
            @RequestParam(required = false) String warehouseCode,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String operationalStatus,
            @RequestParam(required = false) Integer capacity) {

        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));

        ReqWarehouse req = ReqWarehouse.builder()

              .id(warehouseId)
                .warehouseCode(warehouseCode)
                .city(city)
                .type(type)
                .operationalStatus(operationalStatus)
                .capacity(capacity)
                .build();

        List<RespWarehouse> response = warehouseService.listWarehouses(req);

        return ResponseEntity.ok(ResponseUtil.buildSuccessResponse(response));
    }
}