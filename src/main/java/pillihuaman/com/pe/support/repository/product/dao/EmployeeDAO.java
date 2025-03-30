package pillihuaman.com.pe.support.repository.product.dao;



import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqEmployee;
import pillihuaman.com.pe.support.repository.BaseMongoRepository;
import pillihuaman.com.pe.support.repository.employee.Employee;

import java.util.List;

public interface EmployeeDAO extends BaseMongoRepository<Employee> {

    /**
     * Lists employees based on specific request criteria.
     *
     * @param reqEmployee The request criteria for filtering employees.
     * @return A list of employees matching the criteria.
     */
    List<Employee> listEmployees(ReqEmployee reqEmployee);

    /**
     * Saves or updates an employee entity.
     *
     * @param employee The employee entity to save or update.
     * @param token    The JWT token containing user context.
     * @return The saved or updated employee entity.
     */
    Employee saveEmployee(Employee employee, MyJsonWebToken token);

    /**
     * Finds employees associated with a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of employees linked to the specified user.
     */
    List<Employee> findByUserId(String userId);

    boolean deleteInactiveEmployee(MyJsonWebToken jwt, String id);

}
