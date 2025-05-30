package pillihuaman.com.pe.support.repository.product;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "product_view")
public class ProductView {
    @Id
    private ObjectId id;

    private ObjectId productId; // referencia al producto
    private ObjectId fileId;    // opcional: si es una imagen específica

    private String userId;      // opcional: para tracking de usuarios

    private Date viewedAt;
}
