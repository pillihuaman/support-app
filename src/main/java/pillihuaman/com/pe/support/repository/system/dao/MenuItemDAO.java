package pillihuaman.com.pe.support.repository.system.dao;

import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqEmployee;
import pillihuaman.com.pe.support.dto.ReqMenu;
import pillihuaman.com.pe.support.dto.RespMenu;
import pillihuaman.com.pe.support.repository.BaseMongoRepository;
import pillihuaman.com.pe.support.repository.system.MenuItem;

import java.util.List;

public interface MenuItemDAO extends BaseMongoRepository<MenuItem> {
    List<RespMenu> listMenuItems(ReqMenu re);

    List<RespMenu> findBySystemId(String systemId);

    List<RespMenu> findByParentId(String parentId);

    RespMenu saveMenuItem(ReqMenu re, MyJsonWebToken token);

    boolean deleteMenuItem(String id, MyJsonWebToken token);
}
