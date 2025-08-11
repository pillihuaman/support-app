package pillihuaman.com.pe.support.repository.company;

import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqContact;
import pillihuaman.com.pe.support.repository.BaseMongoRepository;
import pillihuaman.com.pe.support.repository.contact.Contact;

import java.util.List;

public interface ContactDAO extends BaseMongoRepository<Contact> {
    List<Contact> listContactsByTenant(ReqContact req);
    Contact saveContact(Contact contact, MyJsonWebToken token);
    boolean deleteContact(String id, MyJsonWebToken token);
}