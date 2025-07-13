package pillihuaman.com.pe.support.RequestResponse.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SizeStockDto {
    private String size; // E.g., "S", "M", "L", o "UNICA" para productos simples
    private int stock;
}