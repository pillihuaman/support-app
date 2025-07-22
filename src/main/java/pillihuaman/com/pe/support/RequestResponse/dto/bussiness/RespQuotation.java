package pillihuaman.com.pe.support.RequestResponse.dto.bussiness;

import lombok.Data;
import pillihuaman.com.pe.support.repository.bussiness.CustomerInfo;
import pillihuaman.com.pe.support.repository.bussiness.DesignDetails;
import pillihuaman.com.pe.support.repository.bussiness.QuotationItem;
import pillihuaman.com.pe.support.repository.bussiness.QuotationTotals;

import java.util.List;

// DTO for the response sent back to the client
@Data
public class RespQuotation {
    private String id;
    private CustomerInfo customerInfo;
    private List<QuotationItem> items;
    private DesignDetails designDetails;
    private QuotationTotals totals;
    private String status;
    private String createdAt;
    private String updatedAt;
    private boolean aceptaTerminos;
}