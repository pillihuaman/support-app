package pillihuaman.com.pe.support.repository.bussiness;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CostBreakdown {
    private double unitFabricCost;
    private double unitSublimationCost;
    // ... (todos los demás campos de desglose de costos)
    private double totalUnitProductionCost;
    // Getters y Setters...
}