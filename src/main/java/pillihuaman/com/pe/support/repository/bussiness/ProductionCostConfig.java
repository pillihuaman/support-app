package pillihuaman.com.pe.support.repository.bussiness;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO PURO para mapear la configuración de costos de producción.
 * Esta clase NO contiene lógica de negocio ni valores por defecto.
 * Es un simple contenedor para los datos obtenidos de la base de datos.
 */
@Data
public class ProductionCostConfig {

    @JsonProperty("targetSalePriceFullSet")
    private BigDecimal targetSalePriceFullSet;

    @JsonProperty("targetSalePricePoloOnly")
    private BigDecimal targetSalePricePoloOnly;

    @JsonProperty("targetSalePriceShortOnly")
    private BigDecimal targetSalePriceShortOnly;

    @JsonProperty("igvRate")
    private BigDecimal igvRate;

    @JsonProperty("defaultFabricPricePerKilo")
    private BigDecimal defaultFabricPricePerKilo;

    @JsonProperty("defaultSewingCostForSet")
    private BigDecimal defaultSewingCostForSet;

    @JsonProperty("fabricYieldMetersPerKg")
    private BigDecimal fabricYieldMetersPerKg;

    @JsonProperty("sublimationPricePerMeter")
    private BigDecimal sublimationPricePerMeter;

    @JsonProperty("socksPrice")
    private BigDecimal socksPrice;

    @JsonProperty("miscCostsPerGarment")
    private BigDecimal miscCostsPerGarment;

    @JsonProperty("embroideryPricePerLogo")
    private BigDecimal embroideryPricePerLogo;

    @JsonProperty("fabricConsumptionFullSet")
    private BigDecimal fabricConsumptionFullSet;

    @JsonProperty("fabricConsumptionPoloOnly")
    private BigDecimal fabricConsumptionPoloOnly;

    @JsonProperty("fabricConsumptionShortOnly")
    private BigDecimal fabricConsumptionShortOnly;
}