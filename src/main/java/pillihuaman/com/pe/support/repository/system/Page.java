package pillihuaman.com.pe.support.repository.system;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pillihuaman.com.pe.lib.common.AuditEntity;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "pages")
public class Page {
    @Id
    private ObjectId id; // Example: "product-detail"
    private String name;
    private String url;
    private String component;
    private String systemId; // FK to SystemModule
    private List<String> permissions; // ["view", "edit"]
    private Boolean status;
    private String icon;
    private AuditEntity audit;
}
