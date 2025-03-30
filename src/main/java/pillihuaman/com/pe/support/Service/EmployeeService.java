package pillihuaman.com.pe.support.Service;



import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.common.ReqBase;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.support.RequestResponse.RespEmployee;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqEmployee;
import java.util.List;

public interface EmployeeService {

	// Método para guardar o actualizar un empleado
	RespBase<RespEmployee> saveEmployee(MyJsonWebToken jwt, ReqBase<ReqEmployee> request);

	// Método para obtener un empleado específico
	RespBase<List<RespEmployee>> getEmployee(MyJsonWebToken jwt, ReqBase<ReqEmployee> request);

	// Método para listar empleados asociados a un usuario
	RespBase<RespEmployee> listEmployeesByUser(MyJsonWebToken jwt, ReqBase<ReqEmployee> request);
	RespBase<Boolean> deleteEmployee(MyJsonWebToken jwt,  String id);

}
