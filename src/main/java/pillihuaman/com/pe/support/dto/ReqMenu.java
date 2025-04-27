package pillihuaman.com.pe.support.dto;

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
public class ReqMenu {
    private String id;
    private String name;
    private Integer page;
    private Integer pagesize;
    private String title;
    private String icon;
    private String url;
    private Integer order;
    private String parentId; // For nested menus
    private String systemId; // FK to SystemModule
    private String pageId;   // FK to Page
    private Boolean status;
    private List<MenuItem> children;



}