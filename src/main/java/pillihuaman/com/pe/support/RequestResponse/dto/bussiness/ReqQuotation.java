package pillihuaman.com.pe.support.RequestResponse.dto.bussiness;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import pillihuaman.com.pe.support.repository.bussiness.DesignDetails;
import pillihuaman.com.pe.support.repository.bussiness.ProductionCostConfig;
import pillihuaman.com.pe.support.repository.bussiness.QuotationItem;

import java.util.List;

// DTO representing the incoming request from the Angular form
@Data
public class ReqQuotation {
    // Usamos los nombres del formulario Angular
    private String clienteNombre;
    private String clienteEmail;
    private String clienteTelefono;
    private String descripcionDetallada;
    private List<Item> items;
    private boolean aceptaTerminos;
    private String  tipoCostoProduccion;
    private ProductionCostConfig productionCostConfig;
    private DesignDetails designDetails;
    @Data
    public static class Item {
        private String nombre;
        private Integer numeroCamisa;
        private String talla;
        private int cantidad;

        // --- CAMBIO CLAVE ---
        // Cambiamos el nombre para seguir la misma convención que los otros campos
        // y evitar problemas con los getters de booleanos "is...".
        private boolean fullSet;
    }
}