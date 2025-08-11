package pillihuaman.com.pe.support.RequestResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Clases auxiliares para la respuesta
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RespPhone {
    private String type;
    private String number;
}
