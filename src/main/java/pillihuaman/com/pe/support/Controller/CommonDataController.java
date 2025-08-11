package pillihuaman.com.pe.support.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.RequestResponse.SaveCommonDataReq;
import pillihuaman.com.pe.support.Service.CommonService;
import pillihuaman.com.pe.support.repository.common.CommonDataDocument;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = Constantes.BASE_ENDPOINT + Constantes.ENDPOINT)
public class CommonDataController {
    @Autowired
    private CommonService commonService;



    @GetMapping("/common/default-data/{id}")
    public ResponseEntity<RespBase<CommonDataDocument>> getDefaultCommonData(@PathVariable(required = false) String id) {
        Optional<CommonDataDocument> result = commonService.findById(id);
        return result.map(data -> ResponseEntity.ok(new RespBase<>(data)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/common")
    public ResponseEntity<RespBase<CommonDataDocument>> saveOrUpdateCommonData(
            @Valid @RequestBody SaveCommonDataReq req) {

        CommonDataDocument savedDocument = commonService.saveOrUpdate(req);
        return ResponseEntity.ok(new RespBase<>(savedDocument));
    }

    @PostMapping("/common/default-data/batch") // Usamos POST para enviar una lista en el body
    public ResponseEntity<RespBase<List<CommonDataDocument>>> getCommonDataByIds(
            @RequestBody List<String> ids) {
        List<CommonDataDocument> result = commonService.findAllByIds(ids);
        if (result != null && !result.isEmpty()) {
            return ResponseEntity.ok(new RespBase<>(result));
        } else {
            // Puedes devolver notFound o simplemente una lista vacía con código 200 OK.
            // Devolver OK con una lista vacía suele ser más práctico para el frontend.
            return ResponseEntity.ok(new RespBase<>(new ArrayList<>()));
        }
    }
    @PostMapping("/common/default-data/configType") // Usamos POST para enviar una lista en el body
    public ResponseEntity<RespBase<List<CommonDataDocument>>> getCommonDataByConfigType(
            @RequestBody String id) {
        List<CommonDataDocument> result = commonService.findByConfigType(id);
        if (result != null && !result.isEmpty()) {
            return ResponseEntity.ok(new RespBase<>(result));
        } else {
            // Puedes devolver notFound o simplemente una lista vacía con código 200 OK.
            // Devolver OK con una lista vacía suele ser más práctico para el frontend.
            return ResponseEntity.ok(new RespBase<>(new ArrayList<>()));
        }
    }
}