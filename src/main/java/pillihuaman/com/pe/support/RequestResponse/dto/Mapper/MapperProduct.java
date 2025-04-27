package pillihuaman.com.pe.support.RequestResponse.dto.Mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pillihuaman.com.pe.support.RequestResponse.RespProduct;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqProduct;
import pillihuaman.com.pe.support.repository.product.Product;

@Mapper
public interface MapperProduct {
    MapperProduct INSTANCE = Mappers.getMapper(MapperProduct.class);

    // Map from Product to RespProduct
    @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "barcode", target = "barcode")
    RespProduct toRespProduct(Product product);

    // Map from ReqProduct to Product
    @Mapping(source = "id", target = "id", qualifiedByName = "stringToObjectId") // Fixed id mapping
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "barcode", target = "barcode")
    @Mapping(target = "supplierId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "audit", ignore = true)
    Product toProduct(ReqProduct reqProduct);

    // Custom conversion from ObjectId to String
    @Named("objectIdToString")
    default String objectIdToString(ObjectId id) {
        return id != null ? id.toString() : null;
    }

    // Custom conversion from String to ObjectId
    @Named("stringToObjectId")
    default ObjectId stringToObjectId(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        try {
            return new ObjectId(id);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
