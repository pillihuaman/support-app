package pillihuaman.com.pe.support.repository.bussiness;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pillihuaman.com.pe.support.repository.product.FileMetadata;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DesignDetails {
    private String detailedDescription; // Corresponde a 'descripcionDetallada'
    private FileMetadata logoFile;      // Información del archivo del logo
    private List<FileMetadata> referenceImages; // Lista de imágenes de referencia
}