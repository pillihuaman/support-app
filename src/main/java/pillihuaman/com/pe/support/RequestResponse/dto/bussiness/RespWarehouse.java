// Archivo: RespWarehouse.java
package pillihuaman.com.pe.support.RequestResponse.dto.bussiness;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespWarehouse {

    private String id; // ID del Almacén

    // --- Campos Faltantes Añadidos ---
    private String companyId;
    private String companyName;
    private String addressId;
    private String addressLine2;
    private String state;
    private String addressDescription;

    private String warehouseCode;
    private String type;
    private String operationalStatus;

    // Dirección aplanada
    private String street;
    private String city;
    private String postalCode;
    private String country;
    private List<Double> coordinates;

    private Integer capacity;
    private String capacityUnit;
    private Integer dockDoors;

    private String mainPhoneNumber;
    private String mainEmail;

    private String managerId;
    private String managerFullName;
}