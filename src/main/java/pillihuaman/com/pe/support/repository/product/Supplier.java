package pillihuaman.com.pe.support.repository.product;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pillihuaman.com.pe.lib.common.AuditEntity;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "supplier")  // Collection name in MongoDB
public class Supplier implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private ObjectId id;
    private String name;
    private String contact;
    private String email;
    private String phone;
    private String address;
    private String country;
    private String city;
    private String taxId;  // RUC, VAT, or similar identifier
    private boolean status;
    private AuditEntity audit;
}
