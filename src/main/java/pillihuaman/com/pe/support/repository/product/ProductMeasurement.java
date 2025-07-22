package pillihuaman.com.pe.support.repository.product;

import lombok.Data;

@Data
public class ProductMeasurement {
    private String size; // Ejemplo: "S", "M", "L"
    private Double chestContour; // Contorno de Pecho
    private Double shoulderWidth; // Espalda (H-H)
    private Double totalLength; // Largo Total
    private Double sleeveLength; // Largo de Manga
}