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
import java.util.List; // Asegúrate de tener este import

@Mapper(componentModel = "spring")
public interface QuotationMapper {

    // ... (El método toEntity se queda como está) ...
    @Mapping(source = "clienteNombre", target = "customerInfo.contactName")
    @Mapping(source = "clienteEmail", target = "customerInfo.contactEmail")
    @Mapping(source = "clienteTelefono", target = "customerInfo.contactPhone")
    @Mapping(source = "descripcionDetallada", target = "designDetails.detailedDescription")
    @Mapping(source = "items", target = "items")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totals", ignore = true)
    @Mapping(target = "audit", ignore = true)
    @Mapping(target = "status", ignore = true)
    Quotation toEntity(ReqQuotation req);

    // ... (El método updateEntityFromDto se queda como está, lo necesitamos para referencia) ...
    @Mapping(source = "clienteNombre", target = "customerInfo.contactName")
    @Mapping(source = "clienteEmail", target = "customerInfo.contactEmail")
    @Mapping(source = "clienteTelefono", target = "customerInfo.contactPhone")
    @Mapping(source = "descripcionDetallada", target = "designDetails.detailedDescription")
    @Mapping(source = "items", target = "items")
    @Mapping(source = "aceptaTerminos", target = "aceptaTerminos")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totals", ignore = true)
    @Mapping(target = "audit", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntityFromDto(ReqQuotation dto, @MappingTarget Quotation entity);

    // ... (El método toQuotationItem se queda como está) ...
    @Mapping(source = "nombre", target = "playerName")
    @Mapping(source = "numeroCamisa", target = "shirtNumber")
    @Mapping(source = "talla", target = "size")
    @Mapping(source = "cantidad", target = "quantity")
    @Mapping(source = "fullSet", target = "isFullSet")
    QuotationItem toQuotationItem(ReqQuotation.Item item);

    // ▼▼▼ MÉTODO AÑADIDO ▼▼▼
    /**
     * Convierte una lista de DTOs de items a una lista de entidades de items.
     * MapStruct usará automáticamente el método toQuotationItem para cada elemento.
     * @param items La lista de items del DTO de entrada.
     * @return La lista de items de la entidad.
     */
    List<QuotationItem> mapItemsToEntity(List<ReqQuotation.Item> items);


    // ... (El método toDto y los métodos auxiliares se quedan como están) ...
    @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString")
    @Mapping(source = "audit.dateRegister", target = "createdAt", qualifiedByName = "dateToString")
    @Mapping(source = "audit.dateUpdate", target = "updatedAt", qualifiedByName = "dateToString")
    @Mapping(source = "aceptaTerminos", target = "aceptaTerminos")
    RespQuotation toDto(Quotation entity);

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