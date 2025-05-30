package pillihuaman.com.pe.support.RequestResponse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespProductView {
    private String id;
    private String productId; // referencia al producto
    private String fileId;    // opcional: si es una imagen específica
    private String userId;      // opcional: para tracking de usuarios
}
