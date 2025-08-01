package pillihuaman.com.pe.support.repository.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public  class QuantityBasedPrice {
    private String size;
    private String description;
    private Integer minQuantity;
    private BigDecimal unitPrice;
}
