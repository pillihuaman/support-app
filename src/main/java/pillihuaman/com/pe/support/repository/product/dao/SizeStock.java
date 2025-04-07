package pillihuaman.com.pe.support.repository.product.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SizeStock {
    private String size;
    private int stock;
}
