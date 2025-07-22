package pillihuaman.com.pe.support.repository.bussiness;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotationResult {
    private CostBreakdown costBreakdown;
    private double unitSalePrice;
    private double totalQuotePrice;
    private int quantity;
    private String details;
    // Getters y Setters y un buen método toString() para imprimir
}