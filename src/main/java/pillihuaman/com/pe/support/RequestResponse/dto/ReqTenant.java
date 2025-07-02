package pillihuaman.com.pe.support.RequestResponse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqTenant {

    private String id;
    private String name;
    private String domain;
    private boolean active;
}
