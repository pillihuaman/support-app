package pillihuaman.com.pe.support.repository.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileMetadata {
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
    private List<SizeStock> sizeStock;
}
