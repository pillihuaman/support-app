package pillihuaman.com.pe.support.repository.product;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecificationGroup {

    private String groupName; // Ejemplo: "Especificaciones Técnicas", "Dimensiones", "Contenido"
    private List<SpecificationAttribute> attributes;
}