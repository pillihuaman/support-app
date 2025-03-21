package pillihuaman.com.pe.support.Service.Implement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pillihuaman.com.pe.basebd.user.User;
import pillihuaman.com.pe.basebd.user.dao.implement.UserDaoImplement;
import pillihuaman.com.pe.lib.request.ReqUser;
import pillihuaman.com.pe.lib.response.RespUser;
import pillihuaman.com.pe.support.Service.UserService;

import pillihuaman.com.pe.lib.response.RespBase;
import pillihuaman.com.pe.support.Service.mapper.UserMapper;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDaoImplement userDaoImplement;
    protected final Log log = LogFactory.getLog(getClass());

    @Override
    public RespBase<RespUser> getUserByMail(String mail) {
        // TODO Auto-generated method stub
        RespBase<RespUser> user = new RespBase<RespUser>();
        User u = userDaoImplement.findUserByMail(mail).get(0);
        if (u != null) {
            //user.setPayload(ConvertClass.respUserDtoToUser(u));
        }

        return user;
    }

    @Override
    public RespBase<RespUser> getUserByUserName(String mail) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RespBase<RespUser> registerUser(ReqUser request) {
        RespBase<RespUser> response = new RespBase<RespUser>();
        try {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String salt = "";//PasswordUtils.getSalt(30);
            String mySecurePassword = "";// PasswordUtils.generateSecurePassword(request.getPassword(), salt);
            String codeString = bCryptPasswordEncoder.encode(mySecurePassword);
            System.out.println("salt   " + salt);
            System.out.println("Api Password   " + mySecurePassword);
            System.out.println("Password   " + codeString);
            List<User> list = userDaoImplement.findLastUser();
            response.getStatus().setSuccess(Boolean.TRUE);
            //response.setPayload(new RespUser());
        } catch (Exception e) {

            response.getStatus().setSuccess(Boolean.FALSE);
            throw e;

            // response.getStatus().getError().getMessages().add(e.getMessage());
        }

        return response;
    }

    @Override
    public List<RespUser> getUserByRequestUser(ReqUser request) {
        return null;
    }

    @Override
    public RespBase<List<RespUser>> listByStatus(boolean status) {
        RespBase<List<RespUser>> r = new RespBase<>();
        r.setPayload(UserMapper.INSTANCE.usersToRespUsers(userDaoImplement.listByStatus(status)));
        return r;
    }

}
