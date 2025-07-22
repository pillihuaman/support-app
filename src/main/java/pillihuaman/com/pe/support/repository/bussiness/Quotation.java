package pillihuaman.com.pe.support.repository.bussiness;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pillihuaman.com.pe.lib.common.AuditEntity;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "quotations")
public class Quotation implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private ObjectId id;
    private CustomerInfo customerInfo;
    private List<QuotationItem> items;
    private DesignDetails designDetails;
    private QuotationTotals totals;
    private AuditEntity audit;
    private String status; // Ej: PENDING, APPROVED, IN_PRODUCTION, COMPLETED
    private boolean aceptaTerminos;
}