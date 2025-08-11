package pillihuaman.com.pe.support.RequestResponse.dto.bussiness;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ReqEmail {
    private String type;
    private String address;
}
