package pillihuaman.com.pe.support.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespSystemEntities {
    private String systemId;
    private String systemName;
    private String systemDescription;
    private String pageId;
    private String pageName;
    private String pageUrl;
    private String menuId;
    private String menuTitle;
    private String menuUrl;
}
