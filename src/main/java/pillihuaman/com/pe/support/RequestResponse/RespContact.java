package pillihuaman.com.pe.support.RequestResponse;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespContact {
    private String id;
    private String companyId;
    private String type;
    private String name;

    // Campos de dirección
    private String street;
    private String city;
    private String region;
    private String country;
    private String message;
    // Listas de contacto
    private List<RespPhone> phones;
    private List<RespEmail> emails;
}

