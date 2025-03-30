package pillihuaman.com.pe.support.repository.employee.dao.implement;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqEmployee;
import pillihuaman.com.pe.support.repository.AzureAbstractMongoRepositoryImpl;
import pillihuaman.com.pe.support.repository.employee.Employee;
import pillihuaman.com.pe.support.repository.employee.dao.EmployeeDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Repository
public class EmployeeDaoImplement extends AzureAbstractMongoRepositoryImpl<Employee> implements EmployeeDAO {
    EmployeeDaoImplement() {
        DS_WRITE = Constantes.DW;
        // DS_READ = Constants.DR;
        COLLECTION = Constantes.COLLECTION_EMPLOYEE;
    }


    @Override
    public Class<Employee> provideEntityClass() {
        return Employee.class;
    }

    @Override
    public List<Employee> listEmployees(ReqEmployee reqEmployee) {
        MongoCollection<Employee> collection = getCollection(this.COLLECTION, Employee.class);

        List<Document> filters = new ArrayList<>();

        // Only find active employees
        filters.add(new Document("status", true));

        // Exact match for ObjectId (if provided)
        if (reqEmployee.getId() != null && !reqEmployee.getId().isEmpty()) {
            filters.add(new Document("_id", new ObjectId(reqEmployee.getId())));
        }

        // Case-insensitive substring search for Name
        if (reqEmployee.getName() != null && !reqEmployee.getName().isEmpty()) {
            String regexPattern = ".*" + reqEmployee.getName() + ".*";  // Find `mala+ 4` anywhere in the string
            filters.add(new Document("name", new Document("$regex", regexPattern).append("$options", "i")));
        }

        // Case-insensitive substring search for Last Name
        if (reqEmployee.getLastName() != null && !reqEmployee.getLastName().isEmpty()) {
            String regexPattern = ".*" + reqEmployee.getLastName() + ".*";
            filters.add(new Document("lastName", new Document("$regex", regexPattern).append("$options", "i")));
        }

        // Case-insensitive substring search for Document
        if (reqEmployee.getDocument() != null && !reqEmployee.getDocument().isEmpty()) {
            String regexPattern = ".*" + reqEmployee.getDocument() + ".*";
            filters.add(new Document("document", new Document("$regex", regexPattern).append("$options", "i")));
        }

        // Combine filters using $and (if there are multiple conditions)
        Document query = filters.isEmpty() ? new Document() : new Document("$and", filters);

        // Pagination settings
        int page = (reqEmployee.getPage() != null && reqEmployee.getPage() > 0) ? reqEmployee.getPage() : 1;
        int pageSize = (reqEmployee.getPagesize() != null && reqEmployee.getPagesize() > 0) ? reqEmployee.getPagesize() : 10;
        int skip = (page - 1) * pageSize;

        // Execute query with pagination
        return collection.find(query)
                .sort(new Document("audit.dateRegister", -1)) // Sort by `dateRegister` descending
                .skip(skip)
                .limit(pageSize)
                .into(new ArrayList<>());
    }





    @Override
    public List<Employee> findByUserId(String userId) {
        MongoCollection<Employee> collection = getCollection(this.COLLECTION, Employee.class);

        // Query for active employees only
        Document query = new Document("user.id", userId)
                .append("status", true);

        return collection.find(query)
                .into(new ArrayList<>());
    }


    @Override
    public Employee saveEmployee(Employee employee, MyJsonWebToken token) {

        if (employee == null || token == null) {
            return null;
        }
        // Obtener la colección de MongoDB
        MongoCollection<Employee> collection = getCollection(this.COLLECTION, Employee.class);

        if (employee.getId() == null) {
            // Configurar datos de auditoría
            AuditEntity audit = new AuditEntity();
            audit.setCodUser(token.getUser().getId());
            audit.setMail(token.getUser().getMail());
            audit.setDateRegister(new Date());
            employee.setStatus(true);
            employee.setAudit(audit);
            // Insertar nuevo registro
            collection.insertOne(employee);
        } else {
            // Find existing document
            Document query = new Document("_id", employee.getId());
            Employee existingEmployee = collection.find(query).first();

            if (existingEmployee != null) {
                // Update audit details
                employee.setStatus(true);
                AuditEntity audit = new AuditEntity();
                audit.setCodUser(token.getUser().getId());
                audit.setMail(token.getUser().getMail());
                audit.setDateUpdate(new Date());
                employee.setAudit(audit);

                // Update the document
                collection.replaceOne(query, employee);
                System.out.println("Documento actualizado con ID: " + employee.getId());
            } else {
                System.out.println("No se encontró el documento con ID: " + employee.getId());
            }
        }

        return employee;
    }



    @Override
    public boolean deleteInactiveEmployee(MyJsonWebToken token, String id) {
        try {
            if (id == null || id.isEmpty() || token == null) {
                return false;
            }

            MongoCollection<Employee> collection = getCollection(this.COLLECTION, Employee.class);

            // Find the employee
            Document filter = new Document("_id", new ObjectId(id));
            Employee employee = collection.find(filter).first();

            if (employee != null) {
                // Update audit details before deletion
                AuditEntity audit = new AuditEntity();
                audit.setCodUser(token.getUser().getId());
                audit.setMail(token.getUser().getMail());
                audit.setDateUpdate(new Date());
                employee.setStatus(false);
                employee.setAudit(audit);

                // Save updated audit info before deletion (optional, depends on business logic)
                collection.replaceOne(filter, employee);

                // Delete employee
                long deletedCount = collection.deleteOne(filter).getDeletedCount();
                return deletedCount > 0;
            }

            return false;
        } catch (Exception e) {
            System.err.println("Error deleting employee: " + e.getMessage());
            return false;
        }
    }

}
