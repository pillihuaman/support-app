package pillihuaman.com.pe.support.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespMenuTree {
    private String type;         // "system", "page" o "menu"
    private String id;           // UUID o ID interno
    private String parentId;     // ID del padre (opcional)
    private String linkParent;   // URL del padre (opcional)
    private String title;        // Título que se muestra
    private String icon;         // Icono o clase de icono
    private String link;         // URL del item

    private Boolean expanded;    // Si está expandido por defecto
    private Boolean hidden;      // Si debe estar oculto
    private Boolean home;        // Si es el item principal
    private Boolean group;       // Si es un grupo no clickable

    private String pathMatch;    // "full" o "prefix"
    private Boolean skipLocationChange; // Si salta el cambio de URL

    private Map<String, Object> queryParams;   // Parámetros de Query
    private String fragment;                   // Fragmento de ancla (#)
    private Boolean preserveFragment;          // Si se preserva el fragmento

    private String ariaRole;      // Accesibilidad (opcional)
    private Object data;          // Datos extra (Object para ser genérico)

    private List<RespMenuTree> children; // Hijos recursivos
}
