package pillihuaman.com.pe.support.RequestResponse.dto.Mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pillihuaman.com.pe.support.RequestResponse.RespStore;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqStore;
import pillihuaman.com.pe.support.repository.store.Store;

@Mapper
public interface MapperStore {
    MapperStore INSTANCE = Mappers.getMapper(MapperStore.class);
    // Mapea de Store (entidad) a RespStore (DTO de respuesta)
    @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString")
    RespStore toRespStore(Store store);

    // Mapea de ReqStore (DTO de solicitud) a Store (entidad)
    @Mapping(source = "id", target = "id", qualifiedByName = "stringToObjectId")
    @Mapping(target = "audit", ignore = true) // Ignorar audit si no es necesario
    Store toStore(ReqStore reqStore);

    // Métodos auxiliares para conversión de ObjectId a String y viceversa
    @Named("objectIdToString")
    static String objectIdToString(ObjectId id) {
        return id != null ? id.toHexString() : null;
    }

    @Named("stringToObjectId")
    static ObjectId stringToObjectId(String id) {
        return (id != null && !id.isEmpty()) ? new ObjectId(id) : null;
    }


}
