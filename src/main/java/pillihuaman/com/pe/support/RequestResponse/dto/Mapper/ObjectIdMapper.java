package pillihuaman.com.pe.support.RequestResponse.dto.Mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Named;

public class ObjectIdMapper {

    @Named("objectIdToString")
    public static String objectIdToString(ObjectId id) {
        return id != null ? id.toHexString() : null;
    }

    @Named("stringToObjectId")
    public static ObjectId stringToObjectId(String id) {
        return (id != null && !id.isEmpty()) ? new ObjectId(id) : null;
    }
}