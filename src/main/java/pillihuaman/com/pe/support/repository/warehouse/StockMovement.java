package pillihuaman.com.pe.support.repository.warehouse;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.support.repository.product.Product;

import java.util.Date;

@Document("stock_movements")
public class StockMovement {
    @Id
    private ObjectId id;

    @DBRef
    private Product product;

    @DBRef
    private Stock stock;

    private String movementType; // IN, OUT, etc.
    private Integer quantity;
    private Date movementDate;
    private String description;
    private AuditEntity audit;
}
