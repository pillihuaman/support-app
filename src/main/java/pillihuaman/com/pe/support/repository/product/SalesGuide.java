package pillihuaman.com.pe.support.repository.product;
import lombok.Data;
import java.util.List;

@Data
public class SalesGuide {

    // --- El "Elevator Pitch" ---
    private String valueProposition; // ¿Por qué es especial? "La única camiseta técnica que necesitas, del gimnasio a la calle."
    private String tagline;          // Lema corto y pegadizo: "Rendimiento sin compromisos."

    // --- Para Quién y Para Qué ---
    private List<String> targetAudience; // Ej: ["Corredores urbanos", "Entusiastas del fitness", "Viajeros minimalistas"]
    private List<String> useCases;       // Ej: ["Entrenamiento en el gimnasio", "Correr por la ciudad", "Capa base para senderismo"]

    // --- La Venta Detallada ---
    private List<Benefit> keyBenefits; // La lista de Característica -> Beneficio

    // --- Guía Práctica ---
    private String fitAndStyleGuide; // Ej: "Corte ajustado (slim fit). Considera una talla más para un look relajado. Combina perfectamente con jeans oscuros."
    private List<String> careInstructions; // Ej: ["Lavar a máquina en frío", "No usar lejía", "Secar en secadora a baja temperatura"]

    // --- Preguntas y Confianza ---
    private List<FaqItem> faq; // Lista de preguntas y respuestas frecuentes
}