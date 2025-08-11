package pillihuaman.com.pe.support.Service.Implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.common.ReqBase;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.support.RequestResponse.RespEmployee;
import pillihuaman.com.pe.support.RequestResponse.dto.Mapper.MapperEmployee;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqEmployee;
import pillihuaman.com.pe.support.Service.EmployeeService;
import pillihuaman.com.pe.support.repository.employee.Employee;
import pillihuaman.com.pe.support.repository.employee.dao.EmployeeDAO;

import java.util.List;

@Component
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeDAO employeeDAO;


    // Método para guardar o actualizar un empleado
    @Override
    public RespBase<RespEmployee> saveEmployee(MyJsonWebToken jwt, ReqBase<ReqEmployee> request) {
        // Obtener la carga útil de la solicitud
        ReqEmployee reqEmployee = request.getPayload();
        Employee em = MapperEmployee.INSTANCE.toEmployee(reqEmployee);
        Employee savedEmployee = null;
            savedEmployee = employeeDAO.saveEmployee(em,
                    jwt);
        // Convertir el Employee guardado a RespEmployee
        RespEmployee respEmployee = MapperEmployee.INSTANCE.toRespEmployee(savedEmployee);


        return new RespBase<>(respEmployee);

    }

    // Método para obtener un empleado específico
    @Override
    public RespBase<List<RespEmployee>> getEmployee(MyJsonWebToken jwt, ReqBase<ReqEmployee> request) {
        // Obtener la carga útil de la solicitud
        //  ReqEmployee reqEmployee = request.getPayload();

        // Consultar el empleado por su ID o algún identificador
        List<Employee> employees = employeeDAO.listEmployees(request.getPayload());

        if (employees.isEmpty()) {
            // Si no se encuentra el empleado, retornar un error o null
            return new RespBase<>(null);
        }

        // Convertir el Employee a RespEmployee

        // RespEmployee respEmployee = MapperEmployee.INSTANCE.toRespEmployee(employees);
        return new RespBase<>(employees.stream()
                .map(MapperEmployee.INSTANCE::toRespEmployee)
                .toList());
    }

    // Método para listar empleados asociados a un usuario
    @Override
    public RespBase<RespEmployee> listEmployeesByUser(MyJsonWebToken jwt, ReqBase<ReqEmployee> request) {
        // Obtener la carga útil de la solicitud
        ReqEmployee reqEmployee = request.getPayload();

        // Listar los empleados por el ID de usuario
        List<Employee> employees = employeeDAO.findByUserId(reqEmployee.getId());

        // Convertir la lista de empleados a RespEmployee
        // List<RespEmployee> respEmployees = MapperEmployee.INSTANCE.employeesToRespEmployees(employees);
        RespBase<List<RespEmployee>> res = new RespBase<>(null);
        // Envolver la lista en un RespBase y retornarla
        return null;
    }

    @Override
    public RespBase<Boolean> deleteEmployee(MyJsonWebToken jwt, String id) {
        return null;
    }
}

