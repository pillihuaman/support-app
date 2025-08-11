package pillihuaman.com.pe.support.repository.contact;
// Archivo: AddressEmbeddable.java

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressEmbeddable {
    private String street;
    private String city;
    private String region;
    private String country;
}


