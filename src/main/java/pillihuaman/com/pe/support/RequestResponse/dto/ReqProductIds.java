package pillihuaman.com.pe.support.RequestResponse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@AllArgsConstructor
@Data
public class ReqProductIds {
    private List<String> productIds;
    private Boolean status; // <-- Nuevo campo
}