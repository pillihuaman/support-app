// Archivo: Warehouse.java
package pillihuaman.com.pe.support.repository.warehouse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.support.repository.common.Address;
import pillihuaman.com.pe.support.repository.company.Company;
import pillihuaman.com.pe.support.repository.employee.Employee;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "warehouses")
// Índice para asegurar que el código es único DENTRO de cada empresa
@CompoundIndex(name = "warehouse_code_idx", def = "{'warehouseCode': 1}", unique = true)

public class Warehouse {

    @Id
    private ObjectId id;

    // --- CAMPO CRÍTICO FALTANTE ---
    @DBRef
    private Company company; // ¡Añadido!

    @Field("warehouseCode")
    private String warehouseCode;


    private boolean status;

    @Field("type")
    private String type;

    @Field("operationalStatus")
    private String operationalStatus;

    // --- REFERENCIA A LA ENTIDAD ADDRESS ---
    @DBRef
    private Address address; // ¡Corregido!

    private Integer capacity;
    @Field("capacityUnit")
    private String capacityUnit;

    private Integer dockDoors;

    // --- OBJETO EMBEBIDO DE CONTACTO ---
    private ContactInfo contactInfo; // ¡Añadido!

    @DBRef
    private Employee manager;

    private AuditEntity audit;
}