package pillihuaman.com.pe.support.repository.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pillihuaman.com.pe.lib.common.AuditEntity;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@Document(collection = "employee")
@Builder
@NoArgsConstructor
@Data
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private ObjectId id;  // MongoDB will automatically generate the ObjectId
    private String name;
    private String lastName;
    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private Date startDate;
    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private Date finishDate;
    private Integer totalHours;
    private String department;
    private Double salaryMonth;
    private Double salaryHours;
    private String typeDocument;
    private String document;
    private boolean status;
    private AuditEntity audit;
}
