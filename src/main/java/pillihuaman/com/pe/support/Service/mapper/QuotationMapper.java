package pillihuaman.com.pe.support.Service.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqQuotation;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.RespQuotation;
import pillihuaman.com.pe.support.repository.bussiness.Quotation;
import pillihuaman.com.pe.support.repository.bussiness.QuotationItem;

import java.util.Date;

@Mapper(componentModel = "spring")
public interface QuotationMapper {

    // --- MAPPING: ReqQuotation (DTO de Entrada) -> Quotation (Entidad de BD) ---

    /**
     * Mapea el DTO de la petición a la entidad que se guardará en la base de datos.
     * La clave es usar los nombres de campo EXACTOS del DTO de origen (source).
     */
    @Mapping(source = "clienteNombre", target = "customerInfo.contactName") // CORREGIDO: source es "clienteNombre"
    @Mapping(source = "clienteEmail", target = "customerInfo.contactEmail")   // CORREGIDO: source es "clienteEmail"
    @Mapping(source = "clienteTelefono", target = "customerInfo.contactPhone") // CORREGIDO: source es "clienteTelefono"
    @Mapping(source = "descripcionDetallada", target = "designDetails.detailedDescription")
    // CORREGIDO: source es "descripcionDetallada"
    @Mapping(source = "items", target = "items") // MapStruct usará el helper toQuotationItem para esta lista
    @Mapping(target = "id", ignore = true)        // Ignoramos campos que se generan automáticamente
    @Mapping(target = "totals", ignore = true)
    @Mapping(target = "audit", ignore = true)
    @Mapping(target = "status", ignore = true)
    Quotation toEntity(ReqQuotation req);


    @Mapping(source = "clienteNombre", target = "customerInfo.contactName")
    @Mapping(source = "clienteEmail", target = "customerInfo.contactEmail")
    @Mapping(source = "clienteTelefono", target = "customerInfo.contactPhone")
    @Mapping(source = "descripcionDetallada", target = "designDetails.detailedDescription")
    @Mapping(source = "items", target = "items")
    @Mapping(source = "aceptaTerminos", target = "aceptaTerminos")
    @Mapping(target = "id", ignore = true) // Ignoramos el ID y la auditoría para no sobreescribirlos
    @Mapping(target = "totals", ignore = true)
    @Mapping(target = "audit", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntityFromDto(ReqQuotation dto, @MappingTarget Quotation entity);

    /**
     * ===================================================================================
     * SEGUNDA CORRECCIÓN: MAPEO DE LA LISTA INTERNA
     * ===================================================================================
     * MapStruct necesita saber cómo convertir un objeto `ReqQuotation.Item` a `QuotationItem`
     * porque sus campos internos también tienen nombres diferentes (ej: nombre vs playerName).
     * Al definir este método, MapStruct lo usará automáticamente al mapear la lista `items`.
     */
    @Mapping(source = "nombre", target = "playerName")
    @Mapping(source = "numeroCamisa", target = "shirtNumber")
    @Mapping(source = "talla", target = "size")
    @Mapping(source = "cantidad", target = "quantity")
    // --- CAMBIO CLAVE ---
    // El source ahora es "esConjuntoCompleto" y el target sigue siendo "isFullSet".
    @Mapping(source = "esConjuntoCompleto", target = "isFullSet")

    QuotationItem toQuotationItem(ReqQuotation.Item item);;

    // --- MAPPING: Quotation (Entidad de BD) -> RespQuotation (DTO de Salida) ---
    // (Este mapeo generalmente es correcto)
    @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString")
    @Mapping(source = "audit.dateRegister", target = "createdAt", qualifiedByName = "dateToString")
    @Mapping(source = "audit.dateUpdate", target = "updatedAt", qualifiedByName = "dateToString")
    @Mapping(source = "aceptaTerminos", target = "aceptaTerminos")
    RespQuotation toDto(Quotation entity);


    // --- MÉTODOS AUXILIARES (sin cambios) ---

    @Named("objectIdToString")
    default String objectIdToString(ObjectId id) {
        return id != null ? id.toHexString() : null;
    }

    @Named("stringToObjectId")
    default ObjectId stringToObjectId(String id) {
        return id != null && !id.isEmpty() ? new ObjectId(id) : null;
    }

    @Named("dateToString")
    default String dateToString(Date date) {
        return date != null ? date.toInstant().toString() : null;
    }
}