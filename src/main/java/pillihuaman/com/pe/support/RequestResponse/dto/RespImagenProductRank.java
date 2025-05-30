package pillihuaman.com.pe.support.RequestResponse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pillihuaman.com.pe.support.RequestResponse.RespProduct;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespImagenProductRank {

    private RespProductView respProductView;
    private RespProduct respProduct;
}
