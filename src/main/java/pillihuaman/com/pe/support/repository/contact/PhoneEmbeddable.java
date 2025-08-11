package pillihuaman.com.pe.support.repository.contact;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhoneEmbeddable {
    private String type; // CELULAR, FIJO
    private String number;
}