package pillihuaman.com.pe.support.repository.warehouse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "shipments")
public class Shipment {
    @Id
    private ObjectId id;

    @DBRef
    private Warehouse warehouse; // Referencia Principal

    private Date shipmentDate;
    private String status;
}