package pillihuaman.com.pe.support.Service.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqSupplier;
import pillihuaman.com.pe.support.RequestResponse.dto.RespSupplier;
import pillihuaman.com.pe.support.repository.supplier.Supplier;

import java.util.List;

@Mapper
public interface SupplierMapper {

    SupplierMapper INSTANCE = Mappers.getMapper(SupplierMapper.class);

    // ====== Entity to Response DTO ======
    @Mappings({
            @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "country", target = "country"),
            @Mapping(source = "phone", target = "phone"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "status", target = "status")
    })
    RespSupplier toRespSupplier(Supplier supplier);

    List<RespSupplier> toRespSupplierList(List<Supplier> suppliers);

    // ====== Request DTO to Entity ======
    @Mappings({
            @Mapping(source = "id", target = "id", qualifiedByName = "stringToObjectId"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "country", target = "country"),
            @Mapping(source = "phone", target = "phone"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "status", target = "status"),
            @Mapping(target = "audit", ignore = true)
    })
    Supplier toSupplierEntity(ReqSupplier req);

    List<Supplier> toSupplierEntityList(List<ReqSupplier> reqList);

    // ====== Helpers ======
    @Named("objectIdToString")
    default String objectIdToString(ObjectId id) {
        return id != null ? id.toHexString() : null;
    }

    @Named("stringToObjectId")
    default ObjectId stringToObjectId(String id) {
        return (id != null && !id.isEmpty()) ? new ObjectId(id) : null;
    }

}
