package pillihuaman.com.pe.support.RequestResponse.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespControl {
    private ObjectId id;
    private String idCode;
    private String description;
    private String icono;
    private String iconClass;
    private int status;
    private String styleClass;
    private String id_user;
    private String text;
}


