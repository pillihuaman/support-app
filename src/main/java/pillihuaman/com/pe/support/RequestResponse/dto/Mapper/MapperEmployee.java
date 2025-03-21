package pillihuaman.com.pe.support.RequestResponse.dto.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.bson.types.ObjectId;
import pillihuaman.com.pe.basebd.employee.Employee;
import pillihuaman.com.pe.lib.request.ReqEmployee;
import pillihuaman.com.pe.lib.response.RespEmployee;

@Mapper
public interface MapperEmployee {
    MapperEmployee INSTANCE = Mappers.getMapper(MapperEmployee.class);

    // De Employee a RespEmployee
    @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString") // Custom conversion from ObjectId to String
    @Mapping(source = "name", target = "name")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "finishDate", target = "finishDate")
    @Mapping(source = "totalHours", target = "totalHours")
    @Mapping(source = "department", target = "department")
    @Mapping(source = "salaryMonth", target = "salaryMonth")
    @Mapping(source = "salaryHours", target = "salaryHours")
    @Mapping(source = "typeDocument", target = "typeDocument")
    @Mapping(source = "document", target = "document")
    RespEmployee toRespEmployee(Employee employee);

    // De ReqEmployee a Employee
    @Mapping(source = "id", target = "id", qualifiedByName = "stringToObjectId") // Custom conversion from String to ObjectId
    @Mapping(source = "name", target = "name")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "finishDate", target = "finishDate")
    @Mapping(source = "totalHours", target = "totalHours")
    @Mapping(source = "department", target = "department")
    @Mapping(source = "salaryMonth", target = "salaryMonth")
    @Mapping(source = "salaryHours", target = "salaryHours")
    @Mapping(source = "typeDocument", target = "typeDocument")
    @Mapping(source = "document", target = "document")
    @Mapping(target = "audit", ignore = true) // Ignorar audit
    Employee toEmployee(ReqEmployee reqEmployee);

    // Custom conversion method to convert ObjectId to String
    @org.mapstruct.Named("objectIdToString")
    default String objectIdToString(ObjectId id) {
        // Return null if id is null, otherwise return the string representation
        return id != null ? id.toString() : null;
    }

    // Custom conversion method to convert String to ObjectId
    @org.mapstruct.Named("stringToObjectId")
    default ObjectId stringToObjectId(String id) {
        // Return null if the id is null or empty, otherwise try to convert it to ObjectId
        if (id == null || id.isEmpty()) {
            return null;
        }
        try {
            return new ObjectId(id);
        } catch (IllegalArgumentException e) {
            // Handle invalid ObjectId format by returning null or throwing an exception
            return null;
        }
    }

    // De Employee a ReqEmployee
    @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "finishDate", target = "finishDate")
    @Mapping(source = "totalHours", target = "totalHours")
    @Mapping(source = "department", target = "department")
    @Mapping(source = "salaryMonth", target = "salaryMonth")
    @Mapping(source = "salaryHours", target = "salaryHours")
    @Mapping(source = "typeDocument", target = "typeDocument")
    @Mapping(source = "document", target = "document")
    RespEmployee toResEmployee(Employee employee);

    // De RespEmployee a Employee (si es necesario)
    @Mapping(source = "id", target = "id", qualifiedByName = "stringToObjectId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "finishDate", target = "finishDate")
    @Mapping(source = "totalHours", target = "totalHours")
    @Mapping(source = "department", target = "department")
    @Mapping(source = "salaryMonth", target = "salaryMonth")
    @Mapping(source = "salaryHours", target = "salaryHours")
    @Mapping(source = "typeDocument", target = "typeDocument")
    @Mapping(source = "document", target = "document")
    @Mapping(target = "audit", ignore = true) // Ignorar audit
    Employee toEmployee(RespEmployee respEmployee);
}
