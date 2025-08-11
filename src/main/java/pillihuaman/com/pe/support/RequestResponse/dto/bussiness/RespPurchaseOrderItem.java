package pillihuaman.com.pe.support.RequestResponse.dto.bussiness;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespPurchaseOrderItem {
    private String id;
    private String purchaseOrderId;
    private String productId;
    private Integer quantityOrdered;
    private Double unitCost;
    private String status;
}
