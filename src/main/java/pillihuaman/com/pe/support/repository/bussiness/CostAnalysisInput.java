package pillihuaman.com.pe.support.repository.bussiness;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CostAnalysisInput {
    private OrderType orderType;
    private double fabricPricePerKilo;
    private double sewingCostForSet;
    private int numberOfEmbroideries;
    // Getters y Setters...
}