package pillihuaman.com.pe.support.repository.product;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pillihuaman.com.pe.lib.common.AuditEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "stock")
public class Stock implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private ObjectId id;
    private ObjectId productId;  // Reference to Product
    private ObjectId storeId;
    private String productCode;
    private String productName;
    private String category;
    private Double costPrice;
    private Double sellingPrice;
    private Integer minimumStock;
    private Integer maximumStock;
    private String warehouseLocation;
    private Map<String, Integer> currentStock; // Key: Size (S, M, L, XL) | Value: Stock count
    private List<Transaction> transactions;
    private List<StockMovement> movements;
    private AuditEntity audit;
}

// StockMovement as an embedded class
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class StockMovement {
    private String type;  // "in" or "out"
    private Integer quantity;
    private Date date;
    private String reference;  // Invoice, Order, etc.
}
