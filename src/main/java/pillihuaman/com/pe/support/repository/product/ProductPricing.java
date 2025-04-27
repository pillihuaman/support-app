package pillihuaman.com.pe.support.repository.product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPricing {
    private Double costPrice;
    private Double sellingPrice;
    private Double discount;
    private String currency;
}