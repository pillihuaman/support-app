package pillihuaman.com.pe.support.repository.system;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pillihuaman.com.pe.lib.common.AuditEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "systems")
public class System {
    @Id
    private ObjectId id; // Example: "support"
    private String name;
    private String description;
    private String icon;
    private Integer order;
    private Boolean status;
    private AuditEntity audit;
}