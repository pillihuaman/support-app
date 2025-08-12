package pillihuaman.com.pe.support.RequestResponse.dto.Mapper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import javax.annotation.processing.Generated;
import pillihuaman.com.pe.support.RequestResponse.RespEmployee;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqEmployee;
import pillihuaman.com.pe.support.repository.employee.Employee;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-12T07:16:34-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Amazon.com Inc.)"
)
public class MapperEmployeeImpl implements MapperEmployee {

    @Override
    public RespEmployee toRespEmployee(Employee employee) {
        if ( employee == null ) {
            return null;
        }

        RespEmployee.RespEmployeeBuilder respEmployee = RespEmployee.builder();

        respEmployee.id( objectIdToString( employee.getId() ) );
        respEmployee.name( employee.getName() );
        respEmployee.lastName( employee.getLastName() );
        if ( employee.getStartDate() != null ) {
            respEmployee.startDate( LocalDateTime.ofInstant( employee.getStartDate().toInstant(), ZoneOffset.UTC ).toLocalDate() );
        }
        if ( employee.getFinishDate() != null ) {
            respEmployee.finishDate( LocalDateTime.ofInstant( employee.getFinishDate().toInstant(), ZoneOffset.UTC ).toLocalDate() );
        }
        respEmployee.document( employee.getDocument() );
        respEmployee.typeDocument( employee.getTypeDocument() );
        respEmployee.salaryHours( employee.getSalaryHours() );

        return respEmployee.build();
    }

    @Override
    public Employee toEmployee(ReqEmployee reqEmployee) {
        if ( reqEmployee == null ) {
            return null;
        }

        Employee.EmployeeBuilder employee = Employee.builder();

        employee.id( stringToObjectId( reqEmployee.getId() ) );
        employee.name( reqEmployee.getName() );
        employee.lastName( reqEmployee.getLastName() );
        employee.startDate( reqEmployee.getStartDate() );
        employee.finishDate( reqEmployee.getFinishDate() );
        employee.document( reqEmployee.getDocument() );
        employee.typeDocument( reqEmployee.getTypeDocument() );
        employee.salaryHours( reqEmployee.getSalaryHours() );
        employee.totalHours( reqEmployee.getTotalHours() );
        employee.department( reqEmployee.getDepartment() );
        employee.salaryMonth( reqEmployee.getSalaryMonth() );

        return employee.build();
    }
}
