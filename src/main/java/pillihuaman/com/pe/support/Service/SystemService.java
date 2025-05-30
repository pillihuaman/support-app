package pillihuaman.com.pe.support.Service;

import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.RequestResponse.dto.*;

import java.util.List;

public interface SystemService {
    // Systems
    List<RespSystemEntities> listSystems(ReqSystemEntities red);

    RespSystemEntities saveSystem(ReqSystemEntities system, MyJsonWebToken token);

    boolean deleteSystem(String id, MyJsonWebToken token);

    // Pages
    List<RespPage> listPages(ReqPage req);

    RespPage savePage(ReqPage req, MyJsonWebToken token);

    List<RespPage> findPagesBySystem(String systemId);

    boolean deletePage(String id, MyJsonWebToken token);

    // Menus
    List<RespMenu> listMenuItems(ReqMenu re);

    RespMenu saveMenuItem(ReqMenu menu, MyJsonWebToken token);

    List<RespMenu> findMenusBySystem(String systemId);

    List<RespMenu> findMenusByParent(String parentId);

    boolean deleteMenuItem(String id, MyJsonWebToken token);

    List<RespSystemEntities> searchSystemEntitiesLineal(ReqSystemEntities req);

    List<RespMenuTree> listSystemRespMenuTree();
}
