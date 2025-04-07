package pillihuaman.com.pe.support.repository.product;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.support.repository.product.dao.SizeStock;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "product")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private ObjectId id;
    private String productCode;
    private String barcode;
    private String name;
    private String description;
    private String category;
    private ObjectId supplierId;
    private Double costPrice;
    private Double sellingPrice;
    private String unitMeasure;
    private Date createdAt;
    private Date updatedAt;
    private boolean status;
    private  String batch;
    private List<SizeStock> sizes;
    private AuditEntity audit;
}

// Supplier as an embedded class
