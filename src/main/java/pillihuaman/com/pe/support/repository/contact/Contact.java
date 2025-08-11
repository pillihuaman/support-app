package pillihuaman.com.pe.support.repository.contact;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.support.repository.company.Company;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "contacts")
public class Contact {

    @Id
    private ObjectId id;

    @DBRef
    private Company company; // Vínculo con el tenant

    private String type; // PRINCIPAL, SUCURSAL
    private String name; // E.g., "Sede Arequipa"

    private AddressEmbeddable address;
    private List<PhoneEmbeddable> phones;
    private List<EmailEmbeddable> emails;
    private String message;
    private boolean status;
    private AuditEntity audit;
}