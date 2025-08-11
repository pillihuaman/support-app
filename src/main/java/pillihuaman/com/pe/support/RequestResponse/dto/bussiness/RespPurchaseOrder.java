package pillihuaman.com.pe.support.RequestResponse.dto.bussiness;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespPurchaseOrder {
    private String id;
    private String supplierName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "GMT-5")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date orderDate;
    private String status;
}