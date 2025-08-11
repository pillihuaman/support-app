package pillihuaman.com.pe.support.RequestResponse.dto.bussiness;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqPurchaseOrder {
    private String id;
    private String supplierId;
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "GMT-5")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private String orderDate;
    private Integer page;
    private Integer pagesize;
}
