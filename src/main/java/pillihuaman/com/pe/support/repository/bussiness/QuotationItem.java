package pillihuaman.com.pe.support.repository.bussiness;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotationItem {
    private String playerName;    // Corresponde a 'nombre'
    private Integer shirtNumber;  // Corresponde a 'numeroCamisa'
    private String size;          // Corresponde a 'talla'
    private int quantity;         // Corresponde a 'cantidad'
    private boolean isFullSet;    // Corresponde a 'esConjuntoCompleto'
}