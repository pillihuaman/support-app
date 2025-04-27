package pillihuaman.com.pe.support.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.common.ReqBase;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.JwtService;
import pillihuaman.com.pe.support.Service.SystemService;
import pillihuaman.com.pe.support.dto.*;
import pillihuaman.com.pe.support.repository.system.MenuItem;
import pillihuaman.com.pe.support.repository.system.Page;
import pillihuaman.com.pe.support.repository.system.System;

import java.util.List;

@RestController
@RequestMapping(path = Constantes.BASE_ENDPOINT + Constantes.ENDPOINT)
public class SystemController {

    @Autowired
    private SystemService systemService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private HttpServletRequest request;

    // ---------------- SYSTEM ----------------

    @GetMapping("/system")
    public ResponseEntity<RespBase<List<RespSystemEntities>>> listSystems(@RequestParam(required = false) Integer page,
                                                                          @RequestParam(required = false) Integer pagesize,
                                                                          @RequestParam(required = false) String id,
                                                                          @RequestParam(required = false) String name) {

        List<RespSystemEntities> systems = systemService.listSystems(ReqSystemEntities.builder().id(id).name(name).page(page).pagesize(pagesize).build());
        return ResponseEntity.ok(new RespBase<>(systems));
    }

    @GetMapping("/system/general")
    public ResponseEntity<RespBase<List<RespSystemEntities>>> searchSystemEntitiesLineal(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String pageName,
            @RequestParam(required = false) String pageUrl,
            @RequestParam(required = false) String menuTitle,
            @RequestParam(required = false) String menuUrl,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pagesize) {

        ReqSystemEntities req = ReqSystemEntities.builder()
                .id(id)
                .name(name)
                .pageName(pageName)
                .pageUrl(pageUrl)
                .menuTitle(menuTitle)
                .menuUrl(menuUrl)
                .page(page)
                .pagesize(pagesize)
                .build();

        List<RespSystemEntities> systems = systemService.searchSystemEntitiesLineal(req);
        return ResponseEntity.ok(new RespBase<>(systems));
    }


    @PostMapping("/system")
    public ResponseEntity<RespBase<RespSystemEntities>> saveSystem(@Valid @RequestBody ReqBase<ReqSystemEntities> req) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));
        RespSystemEntities saved = systemService.saveSystem(req.getData(), token);
        return ResponseEntity.ok(new RespBase<>(saved));
    }

    @DeleteMapping("/system/{id}")
    public ResponseEntity<RespBase<Boolean>> deleteSystem(@PathVariable String id) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));
        boolean deleted = systemService.deleteSystem(id, token);
        return ResponseEntity.ok(new RespBase<>(deleted));
    }

    // ---------------- PAGE ----------------

    @PostMapping("/page")
    public ResponseEntity<RespBase<RespPage>> savePage(@Valid @RequestBody ReqBase<ReqPage> req) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));
        RespPage saved = systemService.savePage(req.getData(), token);
        return ResponseEntity.ok(new RespBase<>(saved));
    }

    @GetMapping("/page")
    public ResponseEntity<RespBase<List<RespPage>>> listPages(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pagesize,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String document) {
        ReqPage req = new ReqPage();
        List<RespPage> pages = systemService.listPages(req);
        return ResponseEntity.ok(new RespBase<>(pages));
    }

    @GetMapping("/page/bySystem")
    public ResponseEntity<RespBase<List<RespPage>>> findPagesBySystem(@RequestParam String systemId) {
        List<RespPage> pages = systemService.findPagesBySystem(systemId);
        return ResponseEntity.ok(new RespBase<>(pages));
    }

    @DeleteMapping("/page/{id}")
    public ResponseEntity<RespBase<Boolean>> deletePage(@PathVariable String id) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));
        boolean deleted = systemService.deletePage(id, token);
        return ResponseEntity.ok(new RespBase<>(deleted));
    }

    // ---------------- MENU ITEM ----------------

    @PostMapping("/menu")
    public ResponseEntity<RespBase<RespMenu>> saveMenuItem(@Valid @RequestBody ReqBase<ReqMenu> req) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));
        RespMenu saved = systemService.saveMenuItem(req.getData(), token);
        return ResponseEntity.ok(new RespBase<>(saved));
    }

    @GetMapping("/menu")
    public ResponseEntity<RespBase<List<RespMenu>>> listMenus(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pagesize,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String icon,
            @RequestParam(required = false) String url,
            @RequestParam(required = false) Integer order,
            @RequestParam(required = false) String parentId,
            @RequestParam(required = false) String systemId,
            @RequestParam(required = false) String pageId,
            @RequestParam(required = false) Boolean status
    ) {

        ReqMenu req = ReqMenu.builder()
                .id(id)
                .name(name)
                .page(page)
                .pagesize(pagesize)
                .title(title)
                .icon(icon)
                .url(url)
                .order(order)
                .parentId(parentId)
                .systemId(systemId)
                .pageId(pageId)
                .status(status)
                .build();

        List<RespMenu> menus = systemService.listMenuItems(req);
        return ResponseEntity.ok(new RespBase<>(menus));
    }

    @GetMapping("/menu/bySystem")
    public ResponseEntity<RespBase<List<RespMenu>>> findMenusBySystem(@RequestParam String systemId) {
        List<RespMenu> menus = systemService.findMenusBySystem(systemId);
        return ResponseEntity.ok(new RespBase<>(menus));
    }

    @GetMapping("/menu/byParent")
    public ResponseEntity<RespBase<List<RespMenu>>> findMenusByParent(@RequestParam String parentId) {
        List<RespMenu> menus = systemService.findMenusByParent(parentId);
        return ResponseEntity.ok(new RespBase<>(menus));
    }

    @DeleteMapping("/menu/{id}")
    public ResponseEntity<RespBase<Boolean>> deleteMenu(@PathVariable String id) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));
        boolean deleted = systemService.deleteMenuItem(id, token);
        return ResponseEntity.ok(new RespBase<>(deleted));
    }

    @GetMapping("/system/menu-tree")
    public ResponseEntity<RespBase<List<RespMenuTree>>> listSystemMenuTree() {
        List<RespMenuTree> systemMenuTree = systemService.listSystemRespMenuTree();
        return ResponseEntity.ok(new RespBase<>(systemMenuTree));
    }
}
