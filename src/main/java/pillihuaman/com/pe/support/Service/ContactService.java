package pillihuaman.com.pe.support.Service;


import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.RequestResponse.RespContact;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqContact;

import java.util.List;

public interface ContactService {
    List<RespContact> getContactsByTenant(ReqContact req);
    RespContact saveContact(ReqContact req, MyJsonWebToken token,String tokenStrin);
    boolean deleteContact(String id, MyJsonWebToken token);
}