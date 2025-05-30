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
public class RespSupplier {
    private String id;
    private String name;
    private String address;
    private String country;
    private String email;
    private String status;
    private String phone;
    private List<Contact> contacts;
}
