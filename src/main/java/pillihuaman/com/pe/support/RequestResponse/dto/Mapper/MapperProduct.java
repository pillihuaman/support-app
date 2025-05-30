package pillihuaman.com.pe.support.RequestResponse.dto.Mapper;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import pillihuaman.com.pe.support.RequestResponse.RespProduct;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqProduct;
import pillihuaman.com.pe.support.repository.product.Product;

import java.text.ParseException;
import java.text.SimpleDateFormat;

//@Mapper(componentModel = "spring", uses = { MapperFileMetadata.class })
@Mapper(componentModel = "spring", uses = { MapperFileMetadata.class }, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MapperProduct {
    MapperProduct INSTANCE = Mappers.getMapper(MapperProduct.class);

    // Product → RespProduct
    @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString")
    @Mapping(source = "productCode", target = "productCode")
    @Mapping(source = "barcode", target = "barcode")
    @Mapping(source = "sku", target = "sku")
    @Mapping(source = "upc", target = "upc")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "subcategory", target = "subcategory")
    @Mapping(source = "supplierId", target = "supplierId", qualifiedByName = "objectIdToString")
    @Mapping(source = "manufacturer", target = "manufacturer")
    @Mapping(source = "brand", target = "brand")

    @Mapping(source = "expirationDate", target = "expirationDate", qualifiedByName = "dateToString")
    @Mapping(source = "manufacturingDate", target = "manufacturingDate", qualifiedByName = "dateToString")
    @Mapping(source = "pricing", target = "pricing")
    @Mapping(source = "inventory", target = "inventory")
    @Mapping(source = "media", target = "media") // List<FileMetadata> → List<fileMetadata>
    @Mapping(source = "status", target = "status")
    @Mapping(source = "fileMetadata", target = "fileMetadata")
    RespProduct toRespProduct(Product product);

    // ReqProduct → Product
    @Mapping(source = "id", target = "id", qualifiedByName = "stringToObjectId")
    @Mapping(source = "productCode", target = "productCode")
    @Mapping(source = "barcode", target = "barcode")
    @Mapping(source = "sku", target = "sku")
    @Mapping(source = "upc", target = "upc")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "subcategory", target = "subcategory")
    @Mapping(source = "supplierId", target = "supplierId", qualifiedByName = "stringToObjectId")
    @Mapping(source = "manufacturer", target = "manufacturer")
    @Mapping(source = "brand", target = "brand")
    @Mapping(source = "expirationDate", target = "expirationDate", qualifiedByName = "stringToDate")
    @Mapping(source = "manufacturingDate", target = "manufacturingDate", qualifiedByName = "stringToDate")
    @Mapping(source = "pricing", target = "pricing")
    @Mapping(source = "inventory", target = "inventory")
    @Mapping(source = "media", target = "media")
    @Mapping(source = "status", target = "status")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "audit", ignore = true)
    Product toProduct(ReqProduct reqProduct);

    // --- Custom Methods ---

    @Named("objectIdToString")
    default String objectIdToString(ObjectId id) {
        return id != null ? id.toHexString() : null;
    }

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

    @Named("dateToString")
    default String dateToString(java.util.Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    @Named("stringToDate")
    default java.util.Date stringToDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            return new SimpleDateFormat("dd/MM/yy HH:mm").parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace(); // opcional: para loggear el error
            return null;
        }
    }
}
