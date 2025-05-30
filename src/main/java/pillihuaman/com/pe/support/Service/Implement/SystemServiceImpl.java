package pillihuaman.com.pe.support.Service.Implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.Service.SystemService;
import pillihuaman.com.pe.support.RequestResponse.dto.*;
import pillihuaman.com.pe.support.repository.system.System;
import pillihuaman.com.pe.support.repository.system.dao.MenuItemDAO;
import pillihuaman.com.pe.support.repository.system.dao.PageDAO;
import pillihuaman.com.pe.support.repository.system.dao.SystemDAO;

import java.util.List;

@Component
public class SystemServiceImpl implements SystemService {

    @Autowired
    private SystemDAO systemDAO;

    @Autowired
    private PageDAO pageDAO;

    @Autowired
    private MenuItemDAO menuItemDAO;

    // -------------------- SYSTEM --------------------
    @Override
    public List<RespSystemEntities> listSystems(ReqSystemEntities re) {
        return systemDAO.listSystems(re);
    }

    @Override
    public RespSystemEntities saveSystem(ReqSystemEntities system, MyJsonWebToken token) {
        return systemDAO.saveSystem(system, token);
    }

    @Override
    public boolean deleteSystem(String id, MyJsonWebToken token) {
        return systemDAO.deleteSystemById(id, token);
    }

    // -------------------- PAGE --------------------
    @Override
    public List<RespPage> listPages(ReqPage req) {
        return pageDAO.listPages(req);
    }

    @Override
    public RespPage savePage(ReqPage req, MyJsonWebToken token) {
        return pageDAO.savePage(req, token);
    }

    @Override
    public List<RespPage> findPagesBySystem(String systemId) {
        return pageDAO.findBySystemId(systemId);
    }

    @Override
    public boolean deletePage(String id, MyJsonWebToken token) {
        return pageDAO.deletePageById(id, token);
    }

    // -------------------- MENU --------------------
    @Override
    public List<RespMenu> listMenuItems(ReqMenu re) {
        return menuItemDAO.listMenuItems(re);
    }

    @Override
    public RespMenu saveMenuItem(ReqMenu menu, MyJsonWebToken token) {
        return menuItemDAO.saveMenuItem(menu, token);
    }

    @Override
    public List<RespMenu> findMenusBySystem(String systemId) {
        return menuItemDAO.findBySystemId(systemId);
    }

    @Override
    public List<RespMenu> findMenusByParent(String parentId) {
        return menuItemDAO.findByParentId(parentId);
    }

    @Override
    public boolean deleteMenuItem(String id, MyJsonWebToken token) {
        return menuItemDAO.deleteMenuItem(id, token);
    }

    @Override
    public List<RespSystemEntities> searchSystemEntitiesLineal(ReqSystemEntities req) {
        return systemDAO.searchSystemEntitiesLineal(req);
    }

    @Override
    public List<RespMenuTree> listSystemRespMenuTree() {
        return systemDAO.listSystemRespMenuTree();
    }
}
