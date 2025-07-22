package pillihuaman.com.pe.support.repository.product;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecificationAttribute {

    private String key;   // Ejemplo: "Procesador", "Color", "Material"
    private String value; // Ejemplo: "Snapdragon 8 Gen 2", "Negro espacial", "Roble macizo"
}