package pillihuaman.com.pe.support.repository.common;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;
@AllArgsConstructor
@Document(collection = "common_data")
@Builder
@NoArgsConstructor
@Data
    public class CommonDataDocument {
        @Id
        private String id;

        private List<Map<String, String>> currencies;
        private List<String> sizes;
        private List<String> fileTypes;

        // getters y setters
    }
