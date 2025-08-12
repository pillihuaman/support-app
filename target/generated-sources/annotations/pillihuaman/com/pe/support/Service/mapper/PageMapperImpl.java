package pillihuaman.com.pe.support.Service.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqPage;
import pillihuaman.com.pe.support.RequestResponse.dto.RespPage;
import pillihuaman.com.pe.support.repository.system.Page;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-12T07:16:34-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Amazon.com Inc.)"
)
public class PageMapperImpl implements PageMapper {

    @Override
    public RespPage toRespPage(Page page) {
        if ( page == null ) {
            return null;
        }

        RespPage.RespPageBuilder respPage = RespPage.builder();

        respPage.id( objectIdToString( page.getId() ) );
        respPage.name( page.getName() );
        respPage.url( page.getUrl() );
        respPage.component( page.getComponent() );
        respPage.systemId( page.getSystemId() );
        List<String> list = page.getPermissions();
        if ( list != null ) {
            respPage.permissions( new ArrayList<String>( list ) );
        }
        respPage.status( page.getStatus() );
        respPage.icon( page.getIcon() );

        return respPage.build();
    }

    @Override
    public List<RespPage> toRespPageList(List<Page> pages) {
        if ( pages == null ) {
            return null;
        }

        List<RespPage> list = new ArrayList<RespPage>( pages.size() );
        for ( Page page : pages ) {
            list.add( toRespPage( page ) );
        }

        return list;
    }

    @Override
    public Page toPageEntity(ReqPage req) {
        if ( req == null ) {
            return null;
        }

        Page.PageBuilder page = Page.builder();

        page.id( stringToObjectId( req.getId() ) );
        page.name( req.getName() );
        page.url( req.getUrl() );
        page.component( req.getComponent() );
        page.systemId( req.getSystemId() );
        page.icon( req.getIcon() );
        List<String> list = req.getPermissions();
        if ( list != null ) {
            page.permissions( new ArrayList<String>( list ) );
        }
        page.status( req.getStatus() );

        return page.build();
    }

    @Override
    public List<Page> toPageEntityList(List<ReqPage> reqList) {
        if ( reqList == null ) {
            return null;
        }

        List<Page> list = new ArrayList<Page>( reqList.size() );
        for ( ReqPage reqPage : reqList ) {
            list.add( toPageEntity( reqPage ) );
        }

        return list;
    }
}
