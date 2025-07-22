package pillihuaman.com.pe.support.RequestResponse.dto.Mapper;

import javax.annotation.processing.Generated;
import pillihuaman.com.pe.support.RequestResponse.RespStore;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqStore;
import pillihuaman.com.pe.support.repository.store.Store;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-20T23:12:40-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class MapperStoreImpl implements MapperStore {

    @Override
    public RespStore toRespStore(Store store) {
        if ( store == null ) {
            return null;
        }

        RespStore respStore = new RespStore();

        respStore.setId( MapperStore.objectIdToString( store.getId() ) );
        respStore.setName( store.getName() );
        respStore.setAddress( store.getAddress() );
        respStore.setCountry( store.getCountry() );
        respStore.setEmail( store.getEmail() );
        respStore.setPhone( store.getPhone() );
        respStore.setStatus( store.getStatus() );
        respStore.setOwnerName( store.getOwnerName() );

        return respStore;
    }

    @Override
    public Store toStore(ReqStore reqStore) {
        if ( reqStore == null ) {
            return null;
        }

        Store.StoreBuilder store = Store.builder();

        store.id( MapperStore.stringToObjectId( reqStore.getId() ) );
        store.name( reqStore.getName() );
        store.address( reqStore.getAddress() );
        store.country( reqStore.getCountry() );
        store.email( reqStore.getEmail() );
        store.phone( reqStore.getPhone() );
        store.status( reqStore.getStatus() );
        store.ownerName( reqStore.getOwnerName() );

        return store.build();
    }
}
