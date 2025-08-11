package pillihuaman.com.pe.support.repository.common;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Representa un documento genérico en la colección 'common_data'.
 * Puede almacenar diferentes tipos de configuración gracias al campo 'configType'
 * y al uso de un mapa para datos dinámicos.
 */
@Data
@Document(collection = "common_data")
public class CommonDataDocument {

    @Id
    private String id; // Por ejemplo: "DEFAULT_SYSTEM_CONFIG", "PRODUCTION_COSTS_V1"

    /**
     * Campo clave para identificar el tipo de configuración almacenada en este documento.
     * Ejemplo: "SYSTEM_DEFAULTS", "PRODUCTION_CONFIG".
     */
    private String configType;

    /**
     * Mapa flexible para almacenar cualquier estructura de configuración.
     *
     * @JsonAnySetter permite a Jackson deserializar cualquier campo JSON desconocido
     * en este mapa.
     * @JsonAnyGetter permite serializar las entradas de este mapa como campos
     * de primer nivel en el JSON de salida.
     */
    @JsonIgnore
    private Map<String, Object> data = new LinkedHashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getPayload() {
        return data;
    }

    @JsonAnySetter
    public void setPayload(String key, Object value) {
        this.data.put(key, value);
    }
}