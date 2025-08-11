package pillihuaman.com.pe.support.RequestResponse.dto.bussiness;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqInventory {
    private String id;
    private String productId;
    private String warehouseId;
    private Integer quantityInStock;
    private Integer reorderLevel;
    private Integer page;
    private Integer pagesize;
}