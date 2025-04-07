package pillihuaman.com.pe.support.repository.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Transaction {
    private Date date;
    private String type; // ingreso, venta, etc.
    private String size; // Size of the product (S, M, L, XL)
    private int quantity;

}
