package pillihuaman.com.pe.support.RequestResponse.dto.bussiness;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqContact {
    private String id;
    private String tenantid; // Se usará para filtros, pero el guardado usará el token
    private String type;
    private String name;

    // Campos aplanados para la dirección
    private String street;
    private String city;
    private String region;
    private String country;
    private String message;
    // Listas para teléfonos y correos
    private List<ReqPhone> phones;
    private List<ReqEmail> emails;

}

