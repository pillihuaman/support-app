package pillihuaman.com.pe.support.Service.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqMenu;
import pillihuaman.com.pe.support.RequestResponse.dto.RespMenu;
import pillihuaman.com.pe.support.repository.system.MenuItem;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-11T14:44:23-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Amazon.com Inc.)"
)
public class MenuMapperImpl implements MenuMapper {

    @Override
    public RespMenu toRespMenu(MenuItem menu) {
        if ( menu == null ) {
            return null;
        }

        RespMenu.RespMenuBuilder respMenu = RespMenu.builder();

        respMenu.id( objectIdToString( menu.getId() ) );
        respMenu.title( menu.getTitle() );
        respMenu.icon( menu.getIcon() );
        respMenu.url( menu.getUrl() );
        respMenu.order( menu.getOrder() );
        respMenu.parentId( menu.getParentId() );
        respMenu.systemId( menu.getSystemId() );
        respMenu.pageId( menu.getPageId() );
        respMenu.status( menu.getStatus() );
        List<MenuItem> list = menu.getChildren();
        if ( list != null ) {
            respMenu.children( new ArrayList<MenuItem>( list ) );
        }

        return respMenu.build();
    }

    @Override
    public List<RespMenu> toRespMenuList(List<MenuItem> menuList) {
        if ( menuList == null ) {
            return null;
        }

        List<RespMenu> list = new ArrayList<RespMenu>( menuList.size() );
        for ( MenuItem menuItem : menuList ) {
            list.add( toRespMenu( menuItem ) );
        }

        return list;
    }

    @Override
    public MenuItem toMenuItem(ReqMenu req) {
        if ( req == null ) {
            return null;
        }

        MenuItem.MenuItemBuilder menuItem = MenuItem.builder();

        menuItem.id( stringToObjectId( req.getId() ) );
        menuItem.title( req.getTitle() );
        menuItem.url( req.getUrl() );
        menuItem.order( req.getOrder() );
        menuItem.parentId( req.getParentId() );
        menuItem.systemId( req.getSystemId() );
        menuItem.pageId( req.getPageId() );
        menuItem.status( req.getStatus() );
        menuItem.icon( req.getIcon() );
        List<MenuItem> list = req.getChildren();
        if ( list != null ) {
            menuItem.children( new ArrayList<MenuItem>( list ) );
        }

        return menuItem.build();
    }

    @Override
    public List<MenuItem> toMenuItemList(List<ReqMenu> reqList) {
        if ( reqList == null ) {
            return null;
        }

        List<MenuItem> list = new ArrayList<MenuItem>( reqList.size() );
        for ( ReqMenu reqMenu : reqList ) {
            list.add( toMenuItem( reqMenu ) );
        }

        return list;
    }
}
