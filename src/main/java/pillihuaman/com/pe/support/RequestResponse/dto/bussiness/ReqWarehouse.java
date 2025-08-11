package pillihuaman.com.pe.support.RequestResponse.dto.bussiness;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqWarehouse {
    // El ID se usa para las actualizaciones. Para creación es nulo.
    private String id;

    // El companyId se obtendrá del token del usuario en el backend, no se envía desde el cliente.

    private String warehouseCode;
    private String type; // Ej: "CENTRO_DISTRIBUCION"
    private String operationalStatus; // Ej: "OPERATIVO"

    private Integer capacity;
    private String capacityUnit; // Ej: "PALETAS"
    private Integer dockDoors;

    // Se envían los datos de la dirección directamente
    private String street;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    // La descripción puede ayudar a identificar la dirección para el usuario
    private String addressDescription; // Ej: "Entrada principal por Av. Industrial"

    // Contacto del almacén
    private String mainPhoneNumber;
    private String mainEmail;

    // Se envía el ID del empleado que será el manager
    private String managerId;
    private String tenantid;
}