package pillihuaman.com.pe.support.repository.product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductMedia {
    private List<String> imageUrls;
    private String thumbnailUrl;
    private List<String> tags;
    private String seoTitle;
    private String seoDescription;
}
