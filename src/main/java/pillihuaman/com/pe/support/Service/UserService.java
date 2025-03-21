package pillihuaman.com.pe.support.Service;

import pillihuaman.com.pe.lib.request.ReqUser;
import pillihuaman.com.pe.lib.response.RespBase;
import pillihuaman.com.pe.lib.response.RespUser;

import java.util.List;


public interface UserService {
    RespBase<RespUser> getUserByMail(String mail);

    RespBase<RespUser> getUserByUserName(String mail);

    RespBase<RespUser> registerUser(ReqUser request);

    List<RespUser> getUserByRequestUser(ReqUser request);
    RespBase<List<RespUser>>  listByStatus(boolean status);

}
