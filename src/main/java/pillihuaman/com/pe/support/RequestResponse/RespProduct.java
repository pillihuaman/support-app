package pillihuaman.com.pe.support.RequestResponse;

import java.sql.Timestamp;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespProduct {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private String category;
    private String barcode;
}

