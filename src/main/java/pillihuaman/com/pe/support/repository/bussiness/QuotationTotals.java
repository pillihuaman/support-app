package pillihuaman.com.pe.support.repository.bussiness;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotationTotals {
    private int totalGarments;            // cantidadTotalPrendas
    private BigDecimal garmentsSubtotal;  // subtotalPrendas
    private BigDecimal designTotal;       // totalDiseno
    private BigDecimal grandTotal;        // granTotal

    // Precios base al momento de la cotización para referencia futura
    private BigDecimal fullSetPrice;      // precioConjuntoCompleto
    private BigDecimal poloOnlyPrice;     // precioSoloPolo
    private BigDecimal designCostPerGarment; // costoDisenoPorPrenda
    // --- Estructura de Precios con Impuestos ---
    private BigDecimal subtotal;          // El "Valor de Venta" antes de impuestos
    private BigDecimal igvAmount;         // El monto calculado del 18% de IGV
}