package pillihuaman.com.pe.support.repository.bussiness;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInfo {
    private String contactName; // Corresponde a 'clienteNombre'
    private String contactEmail;      // Corresponde a 'clienteEmail'
    private String contactPhone;      // Corresponde a 'clienteTelefono'
}