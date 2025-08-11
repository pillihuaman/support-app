package pillihuaman.com.pe.support.repository.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.support.repository.company.Company;

import java.util.List;

/**
 * Entidad centralizada para almacenar direcciones.
 * Cada dirección pertenece a una empresa (multi-tenant).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "addresses")
// Índice para asegurar que una dirección no se duplique dentro de la misma empresa
@CompoundIndex(name = "company_address_unique_idx", def = "{'company': 1, 'country': 1, 'postalCode': 1, 'street': 1}", unique = true)
public class Address {

    @Id
    private ObjectId id;

    // Referencia a la empresa propietaria de esta dirección. CRÍTICO para multi-tenant.
    @DBRef
    private Company company;

    // Detalle de la dirección
    private String street; // Calle y número
    private String addressLine2; // Opcional: Nro de oficina, piso, interior, etc.
    private String city;
    private String state; // Estado, provincia o región
    private String postalCode;
    private String country; // Recomendado usar código ISO de 2 letras (PE, US, ES)

    // Coordenadas para logística y mapas. MongoDB usa el formato [longitud, latitud]
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private List<Double> coordinates;

    // Etiqueta para describir el uso de la dirección
    private String description; // Ej: "Oficina Principal", "Almacén Zona Norte"

    private boolean status; // Para borrado lógico

    private AuditEntity audit;
}