package pillihuaman.com.pe.support.repository.warehouse;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.support.repository.product.Product;

import java.util.Date;

@Document("stocks")
public class Stock {
    @Id
    private ObjectId id;

    @DBRef
    private Product product;

    private Integer quantity;
    private String location;
    private Integer minStock;
    private Integer maxStock;
    private Date expirationDate;
    private String batch;
    private Date entryDate;
    private AuditEntity audit;
}
