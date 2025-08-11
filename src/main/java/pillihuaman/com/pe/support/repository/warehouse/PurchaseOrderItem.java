package pillihuaman.com.pe.support.repository.warehouse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.support.repository.product.Product;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "purchase_order_items")
public class PurchaseOrderItem {
    @Id
    private ObjectId id;

    @DBRef
    private PurchaseOrder purchaseOrder; // Referencia Principal

    @DBRef
    private Product product; // Referencia Principal

    private Integer quantityOrdered;
    private Double unitCost;

    private AuditEntity audit;
    private boolean status;
}