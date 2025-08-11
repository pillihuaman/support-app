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
import pillihuaman.com.pe.support.RequestResponse.RespStore;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqStore;
import pillihuaman.com.pe.support.Service.StoreService;

import java.util.List;

@RestController
@RequestMapping(Constantes.BASE_ENDPOINT + Constantes.ENDPOINT + "/store")
public class StoreController {

    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private StoreService storeService;
    @Autowired
    private JwtService jwtService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RespBase<RespStore>> saveStore(@Valid @RequestBody ReqBase<ReqStore> request) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(httpServletRequest.getHeader("Authorization"));
        return ResponseEntity.ok(storeService.saveStore(token, request));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RespBase<List<RespStore>>> getStores(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pagesize,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address) {

        ReqStore request = new ReqStore();
        request.setPage(page);
        request.setPagesize(pagesize);
        request.setId(id);
        request.setName(name);
        request.setAddress(address);

        ReqBase<ReqStore> reqBase = new ReqBase<>();
        reqBase.setPayload(request);

        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(httpServletRequest.getHeader("Authorization"));
        return ResponseEntity.ok(storeService.getStores(token, reqBase));
    }

    @GetMapping("/list")
    public ResponseEntity<RespBase<List<RespStore>>> listStoresByOwner(
            @RequestParam String ownerId) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(httpServletRequest.getHeader("Authorization"));
        return ResponseEntity.ok(storeService.listStoresByOwner(token, ownerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespBase<String>> deleteStore(@PathVariable String id) {
        RespBase<String> response = new RespBase<>();
        try {
            MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(httpServletRequest.getHeader("Authorization"));

            RespBase<Boolean> deletedResponse = storeService.deleteStore(token, id);
            boolean isDeleted = deletedResponse.getPayload() != null && deletedResponse.getPayload();

            if (isDeleted) {
                response.setPayload("Store deleted successfully");
                return ResponseEntity.ok(response);
            } else {
                response.setPayload("Store not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.setPayload("Error deleting store: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
