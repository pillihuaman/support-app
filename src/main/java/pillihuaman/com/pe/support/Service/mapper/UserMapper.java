package pillihuaman.com.pe.support.Service.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pillihuaman.com.pe.basebd.user.User;
import pillihuaman.com.pe.lib.response.RespUser;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "id", target = "idUser", qualifiedByName = "objectIdToString")
    @Mapping(source = "alias", target = "alias")
    @Mapping(source = "email", target = "mail")
    @Mapping(source = "username", target = "userName")
    @Mapping(source = "enabled", target = "enabled")
    @Mapping(target = "idSystem", ignore = true)
    RespUser toRespUser(User user);

    List<RespUser> usersToRespUsers(List<User> users);

    @Named("objectIdToString")
    default String objectIdToString(ObjectId objectId) {
        return objectId != null ? objectId.toHexString() : null;
    }
}