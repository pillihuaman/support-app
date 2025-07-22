package pillihuaman.com.pe.support.RequestResponse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.Map;

/**
 * DTO para la petición de guardar/actualizar un documento de configuración.
 */
@Data
public class SaveCommonDataReq {

    @NotBlank(message = "El campo 'id' no puede estar vacío.")
    @Size(min = 3, max = 50, message = "El ID debe tener entre 3 y 50 caracteres.")
    private String id;

    @NotBlank(message = "El campo 'configType' no puede estar vacío.")
    private String configType;

    @NotEmpty(message = "El campo 'data' no puede estar vacío.")
    private Map<String, Object> data;
}