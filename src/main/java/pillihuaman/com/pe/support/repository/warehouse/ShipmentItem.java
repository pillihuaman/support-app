package pillihuaman.com.pe.support.repository.warehouse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import pillihuaman.com.pe.support.repository.product.Product;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "shipment_items")
public class ShipmentItem {
    @Id
    private ObjectId id;

    @DBRef
    private Shipment shipment; // Referencia Principal

    @DBRef
    private Product product; // Referencia Principal

    private Integer quantityShipped;
}