package pillihuaman.com.pe.support.Controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.common.ReqBase;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.lib.common.ResponseUtil;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.JwtService;
import pillihuaman.com.pe.support.RequestResponse.RespContact;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqContact;
import pillihuaman.com.pe.support.Service.ContactService;

import java.util.List;

@RestController
@RequestMapping(path = Constantes.BASE_ENDPOINT + Constantes.ENDPOINT + "/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;
    private final JwtService jwtService;
    private final HttpServletRequest request;

    @GetMapping
    public ResponseEntity<RespBase<List<RespContact>>> listContacts() {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));
        ReqContact req = ReqContact.builder()
                .tenantid(token.getUser().getTenantId())
                .build();
        List<RespContact> response = contactService.getContactsByTenant(req);
        return ResponseEntity.ok(ResponseUtil.buildSuccessResponse(response));
    }

    @PostMapping
    public ResponseEntity<RespBase<RespContact>> saveContact(@Valid @RequestBody ReqBase<ReqContact> req) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));
        RespContact savedContact = contactService.saveContact(req.getPayload(), token,request.getHeader("Authorization"));
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtil.buildSuccessResponse(savedContact));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespBase<Boolean>> deleteContact(@PathVariable String id) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));
        boolean deleted = contactService.deleteContact(id, token);
        return ResponseEntity.ok(ResponseUtil.buildSuccessResponse(deleted));
    }
}