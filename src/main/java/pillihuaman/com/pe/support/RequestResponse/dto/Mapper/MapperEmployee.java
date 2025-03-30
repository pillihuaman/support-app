package pillihuaman.com.pe.support.RequestResponse.dto.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.bson.types.ObjectId;
import pillihuaman.com.pe.support.RequestResponse.RespEmployee;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqEmployee;
import pillihuaman.com.pe.support.repository.employee.Employee;

@Mapper
public interface MapperEmployee {
    MapperEmployee INSTANCE = Mappers.getMapper(MapperEmployee.class);

    // De Employee a RespEmployee
    @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "finishDate", target = "finishDate")
    @Mapping(source = "document", target = "document")
    @Mapping(source = "typeDocument", target = "typeDocument")
    @Mapping(source = "salaryHours", target = "salaryHours")
    RespEmployee toRespEmployee(Employee employee);

    // De ReqEmployee a Employee
    @Mapping(source = "id", target = "id", qualifiedByName = "stringToObjectId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "finishDate", target = "finishDate")
    @Mapping(source = "document", target = "document")
    @Mapping(source = "typeDocument", target = "typeDocument")
    @Mapping(source = "salaryHours", target = "salaryHours")
    @Mapping(target = "audit", ignore = true) // Ignorar audit
    Employee toEmployee(ReqEmployee reqEmployee);


    // Conversión personalizada de ObjectId a String
    @Named("objectIdToString")
    default String objectIdToString(ObjectId id) {
        return id != null ? id.toString() : null;
    }

    // Conversión personalizada de String a ObjectId
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
