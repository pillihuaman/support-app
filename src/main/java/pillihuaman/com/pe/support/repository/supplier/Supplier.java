package pillihuaman.com.pe.support.repository.supplier;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pillihuaman.com.pe.lib.common.AuditEntity;

import java.util.List;

@Data
@Document(collection = "supplier")
public class Supplier {
    @Id
    private ObjectId id;
    private String name;
    private String address;
    private String email;
    private String phone;
    private String country;
    private List<Contact> contacts;
    private Boolean status;
    private AuditEntity audit;


}
