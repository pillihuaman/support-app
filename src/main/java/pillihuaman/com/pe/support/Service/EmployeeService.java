package pillihuaman.com.pe.support.Service;

import pillihuaman.com.pe.basebd.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.request.ReqBase;
import pillihuaman.com.pe.lib.request.ReqEmployee;
import pillihuaman.com.pe.lib.response.RespBase;
import pillihuaman.com.pe.lib.response.RespEmployee;

import java.util.List;

public interface EmployeeService {

	// Método para guardar o actualizar un empleado
	RespBase<RespEmployee> saveEmployee(MyJsonWebToken jwt, ReqBase<ReqEmployee> request);

	// Método para obtener un empleado específico
	RespBase<List<RespEmployee>> getEmployee(MyJsonWebToken jwt, ReqBase<ReqEmployee> request);

	// Método para listar empleados asociados a un usuario
	RespBase<RespEmployee> listEmployeesByUser(MyJsonWebToken jwt, ReqBase<ReqEmployee> request);
}
