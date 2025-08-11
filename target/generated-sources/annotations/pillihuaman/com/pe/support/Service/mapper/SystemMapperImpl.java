package pillihuaman.com.pe.support.Service.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqSystemEntities;
import pillihuaman.com.pe.support.RequestResponse.dto.RespSystemEntities;
import pillihuaman.com.pe.support.repository.system.System;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-11T14:44:23-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Amazon.com Inc.)"
)
public class SystemMapperImpl implements SystemMapper {

    @Override
    public RespSystemEntities toRespSystemEntities(System system) {
        if ( system == null ) {
            return null;
        }

        RespSystemEntities.RespSystemEntitiesBuilder respSystemEntities = RespSystemEntities.builder();

        respSystemEntities.systemId( objectIdToString( system.getId() ) );
        respSystemEntities.systemName( system.getName() );
        respSystemEntities.systemDescription( system.getDescription() );

        return respSystemEntities.build();
    }

    @Override
    public List<RespSystemEntities> toRespSystemEntitiesList(List<System> systems) {
        if ( systems == null ) {
            return null;
        }

        List<RespSystemEntities> list = new ArrayList<RespSystemEntities>( systems.size() );
        for ( System system : systems ) {
            list.add( toRespSystemEntities( system ) );
        }

        return list;
    }

    @Override
    public System toSystemEntity(ReqSystemEntities req) {
        if ( req == null ) {
            return null;
        }

        System.SystemBuilder system = System.builder();

        system.id( stringToObjectId( req.getId() ) );
        system.name( req.getName() );
        system.description( req.getDescription() );
        system.icon( req.getIcon() );
        system.order( req.getOrder() );
        system.status( req.isStatus() );

        return system.build();
    }

    @Override
    public List<System> toSystemEntityList(List<ReqSystemEntities> reqList) {
        if ( reqList == null ) {
            return null;
        }

        List<System> list = new ArrayList<System>( reqList.size() );
        for ( ReqSystemEntities reqSystemEntities : reqList ) {
            list.add( toSystemEntity( reqSystemEntities ) );
        }

        return list;
    }
}
