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
@Document(collection = "menus")
public class MenuItem {
    @Id
    private ObjectId id;
    private String title;
    private String icon;
    private String url;
    private Integer order;
    private String parentId; // For nested menus
    private String systemId; // FK to SystemModule
    private String pageId;   // FK to Page
    private Boolean status;
    private List<MenuItem> children;
    private AuditEntity audit;
}
