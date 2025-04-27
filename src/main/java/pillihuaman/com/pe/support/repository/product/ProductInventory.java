package pillihuaman.com.pe.support.repository.product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInventory {
    private String unitMeasure;
    private Integer minStock;
    private Integer maxStock;
    private boolean isFeatured;
    private boolean isNewArrival;
    private String batch;
    private Double weight;  // in kg
    private Double height;  // in cm
    private Double width;
    private Double length;
}
