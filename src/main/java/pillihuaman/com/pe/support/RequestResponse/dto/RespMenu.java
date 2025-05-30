package pillihuaman.com.pe.support.RequestResponse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pillihuaman.com.pe.support.repository.system.MenuItem;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespMenu {
    private String id;
    private String title;
    private String icon;
    private String url;
    private Integer order;
    private String parentId; // For nested menus
    private String systemId; // FK to SystemModule
    private String pageId;   // FK to Page
    private Boolean status;
    private List<MenuItem> children;
    private Integer page;
    private Integer pagesize;
}
