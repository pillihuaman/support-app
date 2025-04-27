package pillihuaman.com.pe.support.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pillihuaman.com.pe.lib.common.AuditEntity;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqPage {
    private String id;
    private String name;
    private Integer page;
    private String url;
    private String component;
    private String systemId; // FK to SystemModule
    private List<String> permissions; // ["view", "edit"]
    private Boolean status;
    private String icon;
    private Integer pagesize;
}