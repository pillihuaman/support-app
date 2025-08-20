package pillihuaman.com.pe.support.Service.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqSupplier;
import pillihuaman.com.pe.support.RequestResponse.dto.RespSupplier;
import pillihuaman.com.pe.support.repository.supplier.Contact;
import pillihuaman.com.pe.support.repository.supplier.Supplier;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-20T07:50:39-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Amazon.com Inc.)"
)
public class SupplierMapperImpl implements SupplierMapper {

    @Override
    public RespSupplier toRespSupplier(Supplier supplier) {
        if ( supplier == null ) {
            return null;
        }

        RespSupplier.RespSupplierBuilder respSupplier = RespSupplier.builder();

        respSupplier.id( objectIdToString( supplier.getId() ) );
        respSupplier.name( supplier.getName() );
        respSupplier.country( supplier.getCountry() );
        respSupplier.phone( supplier.getPhone() );
        respSupplier.email( supplier.getEmail() );
        if ( supplier.getStatus() != null ) {
            respSupplier.status( String.valueOf( supplier.getStatus() ) );
        }
        respSupplier.address( supplier.getAddress() );
        List<Contact> list = supplier.getContacts();
        if ( list != null ) {
            respSupplier.contacts( new ArrayList<Contact>( list ) );
        }

        return respSupplier.build();
    }

    @Override
    public List<RespSupplier> toRespSupplierList(List<Supplier> suppliers) {
        if ( suppliers == null ) {
            return null;
        }

        List<RespSupplier> list = new ArrayList<RespSupplier>( suppliers.size() );
        for ( Supplier supplier : suppliers ) {
            list.add( toRespSupplier( supplier ) );
        }

        return list;
    }

    @Override
    public Supplier toSupplierEntity(ReqSupplier req) {
        if ( req == null ) {
            return null;
        }

        Supplier supplier = new Supplier();

        supplier.setId( stringToObjectId( req.getId() ) );
        supplier.setName( req.getName() );
        supplier.setCountry( req.getCountry() );
        supplier.setPhone( req.getPhone() );
        supplier.setEmail( req.getEmail() );
        supplier.setStatus( req.getStatus() );
        supplier.setAddress( req.getAddress() );
        List<Contact> list = req.getContacts();
        if ( list != null ) {
            supplier.setContacts( new ArrayList<Contact>( list ) );
        }

        return supplier;
    }

    @Override
    public List<Supplier> toSupplierEntityList(List<ReqSupplier> reqList) {
        if ( reqList == null ) {
            return null;
        }

        List<Supplier> list = new ArrayList<Supplier>( reqList.size() );
        for ( ReqSupplier reqSupplier : reqList ) {
            list.add( toSupplierEntity( reqSupplier ) );
        }

        return list;
    }
}
