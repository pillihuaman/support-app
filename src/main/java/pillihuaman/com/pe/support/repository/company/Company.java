package pillihuaman.com.pe.support.repository.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.support.repository.common.Address;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "companies")
public class Company {
    @Id
    private ObjectId id;

    // Identificador fiscal o de registro comercial (RUC, CIF, EIN, etc.)
    @Indexed(unique = true)
    private String taxId;

    private String legalName; // Razón social
    private String tradeName; // Nombre comercial

    private boolean status; // Para borrado lógico o suspensión de la cuenta

    private Address address; // Dirección fiscal/principal de la empresa

    private AuditEntity audit;
}