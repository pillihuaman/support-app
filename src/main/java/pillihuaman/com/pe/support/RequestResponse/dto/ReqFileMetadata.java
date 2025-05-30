package pillihuaman.com.pe.support.RequestResponse.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReqFileMetadata {
    private String id;
    private String filename;
    private String contentType;
    private Long size;
    private String hashCode;
    private String dimension;
    private String userId;
    private Long uploadTimestamp;
    private Boolean status;
    private Integer order;
    private String typeFile;
    private String url;
    private String position;
    private String productId;
    private List<ReqSizeStock> sizeStock;
}
