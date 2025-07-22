package pillihuaman.com.pe.support.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.RequestResponse.SaveCommonDataReq;
import pillihuaman.com.pe.support.Service.CommonService;
import pillihuaman.com.pe.support.repository.common.CommonDataDocument;
import pillihuaman.com.pe.support.repository.common.dao.CommonDAO;

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
}