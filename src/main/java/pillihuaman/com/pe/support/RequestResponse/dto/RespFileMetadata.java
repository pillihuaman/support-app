package pillihuaman.com.pe.support.RequestResponse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespFileMetadata {
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
    private String s3Key;
    private List<RespSizeStock> sizeStock;
}
