package pillihuaman.com.pe.support.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqSystemEntities {
    private String id;
    private String name;
    private Integer page;
    private boolean status;
    private String description;
    private String icon;
    private Integer order;

    private Integer pagesize;
    private String pageName;
    private String pageUrl;
    private String menuTitle;
    private String menuUrl;
    private Integer pageIndex;
    private Integer pageSize;


}