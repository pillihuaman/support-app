package pillihuaman.com.pe.support.Service.mapper;


import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pillihuaman.com.pe.support.RequestResponse.RespContact;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqContact;
import pillihuaman.com.pe.support.repository.contact.Contact;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContactMapper {
    ContactMapper INSTANCE = Mappers.getMapper(ContactMapper.class);

    @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString")
    @Mapping(source = "company.id", target = "companyId", qualifiedByName = "objectIdToString")
    @Mapping(source = "address.street", target = "street")
    @Mapping(source = "address.city", target = "city")
    @Mapping(source = "message", target = "message")
    @Mapping(source = "address.region", target = "region")
    @Mapping(source = "address.country", target = "country")
    RespContact toRespContact(Contact entity);

    List<RespContact> toRespContactList(List<Contact> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "address", ignore = true) // Se ensamblará manualmente
    @Mapping(target = "audit", ignore = true)
    @Mapping(target = "status", ignore = true)
    Contact toContactEntity(ReqContact req);

    @Named("objectIdToString")
    default String objectIdToString(ObjectId id) {
        return id != null ? id.toHexString() : null;
    }
}