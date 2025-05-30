package pillihuaman.com.pe.support.RequestResponse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pillihuaman.com.pe.support.repository.supplier.Contact;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqSupplier {
    private String id;
    private String name;
    private String address;
    private String email;
    private String phone;
    private String country;
    private Boolean status;
    private List<Contact> contacts; // <- lista de contactos del proveedor
    private Integer page;
    private Integer pagesize;
}
