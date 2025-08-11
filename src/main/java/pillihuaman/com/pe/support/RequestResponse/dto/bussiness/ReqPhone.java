package pillihuaman.com.pe.support.RequestResponse.dto.bussiness;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Clases auxiliares para el request
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ReqPhone {
    private String type;
    private String number;
}
