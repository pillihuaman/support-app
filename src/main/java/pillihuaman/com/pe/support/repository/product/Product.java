package pillihuaman.com.pe.support.repository.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.support.repository.product.dao.SizeStock;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "product")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private ObjectId id;

    // Identifiers
    private String productCode;
    private String barcode;
    private String sku;                  // Stock Keeping Unit
    private String upc;                  // Universal Product Code

    // Basic Info
    private String name;
    private String description;
    private String category;
    private String subcategory;

    // Supplier & Manufacturer
    private ObjectId supplierId;
    private String manufacturer;
    private String brand;

    // Size options (remains outside embedded structure)
    private List<FileMetadata> fileMetadata;

    // Batching & Production
    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private Date expirationDate;
    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private Date manufacturingDate;

    // Embedded Components
    private ProductPricing pricing;
    private ProductInventory inventory;
    private ProductMedia media;

    // Status & Audit
    private boolean status;
    private Date createdAt;
    private Date updatedAt;
    private AuditEntity audit;
}
