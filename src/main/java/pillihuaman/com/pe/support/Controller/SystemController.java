package pillihuaman.com.pe.support.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.common.ReqBase;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.lib.common.ResponseUtil;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.JwtService;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqMenu;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqPage;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqSystemEntities;
import pillihuaman.com.pe.support.RequestResponse.dto.RespMenu;
import pillihuaman.com.pe.support.RequestResponse.dto.RespMenuTree;
import pillihuaman.com.pe.support.RequestResponse.dto.RespPage;
import pillihuaman.com.pe.support.RequestResponse.dto.RespSystemEntities;
import pillihuaman.com.pe.support.Service.SystemService;

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
    public ResponseEntity<RespBase<List<RespSystemEntities>>> listSystems(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pagesize,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String name) {

        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(
                        systemService.listSystems(
                                ReqSystemEntities.builder()
                                        .id(id)
                                        .name(name)
                                        .page(page)
                                        .pagesize(pagesize)
                                        .build()
                        )
                )
        );
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

        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(
                        systemService.searchSystemEntitiesLineal(req)
                )
        );
    }

    @PostMapping("/system")
    public ResponseEntity<RespBase<RespSystemEntities>> saveSystem(@Valid @RequestBody ReqBase<ReqSystemEntities> req) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(
                        systemService.saveSystem(req.getPayload(), token)
                )
        );
    }

    @DeleteMapping("/system/{id}")
    public ResponseEntity<RespBase<Boolean>> deleteSystem(@PathVariable String id) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(
                        systemService.deleteSystem(id, token)
                )
        );
    }

    // ---------------- PAGE ----------------

    @PostMapping("/page")
    public ResponseEntity<RespBase<RespPage>> savePage(@Valid @RequestBody ReqBase<ReqPage> req) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(
                        systemService.savePage(req.getPayload(), token)
                )
        );
    }

    @GetMapping("/page")
    public ResponseEntity<RespBase<List<RespPage>>> listPages(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pagesize,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String document) {

        ReqPage req = new ReqPage(); // Puedes mapear si es necesario
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(
                        systemService.listPages(req)
                )
        );
    }

    @GetMapping("/page/bySystem")
    public ResponseEntity<RespBase<List<RespPage>>> findPagesBySystem(@RequestParam String systemId) {
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(
                        systemService.findPagesBySystem(systemId)
                )
        );
    }

    @DeleteMapping("/page/{id}")
    public ResponseEntity<RespBase<Boolean>> deletePage(@PathVariable String id) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(
                        systemService.deletePage(id, token)
                )
        );
    }

    // ---------------- MENU ITEM ----------------

    @PostMapping("/menu")
    public ResponseEntity<RespBase<RespMenu>> saveMenuItem(@Valid @RequestBody ReqBase<ReqMenu> req) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(
                        systemService.saveMenuItem(req.getPayload(), token)
                )
        );
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

        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(
                        systemService.listMenuItems(req)
                )
        );
    }

    @GetMapping("/menu/bySystem")
    public ResponseEntity<RespBase<List<RespMenu>>> findMenusBySystem(@RequestParam String systemId) {
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(
                        systemService.findMenusBySystem(systemId)
                )
        );
    }

    @GetMapping("/menu/byParent")
    public ResponseEntity<RespBase<List<RespMenu>>> findMenusByParent(@RequestParam String parentId) {
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(
                        systemService.findMenusByParent(parentId)
                )
        );
    }

    @DeleteMapping("/menu/{id}")
    public ResponseEntity<RespBase<Boolean>> deleteMenu(@PathVariable String id) {
        MyJsonWebToken token = jwtService.parseTokenToMyJsonWebToken(request.getHeader("Authorization"));
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(
                        systemService.deleteMenuItem(id, token)
                )
        );
    }

    @GetMapping("/system/menu-tree")
    public ResponseEntity<RespBase<List<RespMenuTree>>> listSystemMenuTree() {
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(
                        systemService.listSystemRespMenuTree()
                )
        );
    }
}
