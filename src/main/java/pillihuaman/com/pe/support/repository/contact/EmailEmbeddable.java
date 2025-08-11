package pillihuaman.com.pe.support.repository.contact;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailEmbeddable {
    private String type; // VENTAS, SOPORTE
    private String address;
}