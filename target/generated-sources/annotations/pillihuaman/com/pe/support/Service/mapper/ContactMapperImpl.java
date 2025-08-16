package pillihuaman.com.pe.support.Service.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.support.RequestResponse.RespContact;
import pillihuaman.com.pe.support.RequestResponse.RespEmail;
import pillihuaman.com.pe.support.RequestResponse.RespPhone;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqContact;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqEmail;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqPhone;
import pillihuaman.com.pe.support.repository.company.Company;
import pillihuaman.com.pe.support.repository.contact.AddressEmbeddable;
import pillihuaman.com.pe.support.repository.contact.Contact;
import pillihuaman.com.pe.support.repository.contact.EmailEmbeddable;
import pillihuaman.com.pe.support.repository.contact.PhoneEmbeddable;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-15T21:45:48-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Amazon.com Inc.)"
)
@Component
public class ContactMapperImpl implements ContactMapper {

    @Override
    public RespContact toRespContact(Contact entity) {
        if ( entity == null ) {
            return null;
        }

        RespContact.RespContactBuilder respContact = RespContact.builder();

        respContact.id( objectIdToString( entity.getId() ) );
        respContact.companyId( objectIdToString( entityCompanyId( entity ) ) );
        respContact.street( entityAddressStreet( entity ) );
        respContact.city( entityAddressCity( entity ) );
        respContact.message( entity.getMessage() );
        respContact.region( entityAddressRegion( entity ) );
        respContact.country( entityAddressCountry( entity ) );
        respContact.type( entity.getType() );
        respContact.name( entity.getName() );
        respContact.phones( phoneEmbeddableListToRespPhoneList( entity.getPhones() ) );
        respContact.emails( emailEmbeddableListToRespEmailList( entity.getEmails() ) );

        return respContact.build();
    }

    @Override
    public List<RespContact> toRespContactList(List<Contact> entities) {
        if ( entities == null ) {
            return null;
        }

        List<RespContact> list = new ArrayList<RespContact>( entities.size() );
        for ( Contact contact : entities ) {
            list.add( toRespContact( contact ) );
        }

        return list;
    }

    @Override
    public Contact toContactEntity(ReqContact req) {
        if ( req == null ) {
            return null;
        }

        Contact.ContactBuilder contact = Contact.builder();

        contact.type( req.getType() );
        contact.name( req.getName() );
        contact.phones( reqPhoneListToPhoneEmbeddableList( req.getPhones() ) );
        contact.emails( reqEmailListToEmailEmbeddableList( req.getEmails() ) );
        contact.message( req.getMessage() );

        return contact.build();
    }

    private ObjectId entityCompanyId(Contact contact) {
        if ( contact == null ) {
            return null;
        }
        Company company = contact.getCompany();
        if ( company == null ) {
            return null;
        }
        ObjectId id = company.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityAddressStreet(Contact contact) {
        if ( contact == null ) {
            return null;
        }
        AddressEmbeddable address = contact.getAddress();
        if ( address == null ) {
            return null;
        }
        String street = address.getStreet();
        if ( street == null ) {
            return null;
        }
        return street;
    }

    private String entityAddressCity(Contact contact) {
        if ( contact == null ) {
            return null;
        }
        AddressEmbeddable address = contact.getAddress();
        if ( address == null ) {
            return null;
        }
        String city = address.getCity();
        if ( city == null ) {
            return null;
        }
        return city;
    }

    private String entityAddressRegion(Contact contact) {
        if ( contact == null ) {
            return null;
        }
        AddressEmbeddable address = contact.getAddress();
        if ( address == null ) {
            return null;
        }
        String region = address.getRegion();
        if ( region == null ) {
            return null;
        }
        return region;
    }

    private String entityAddressCountry(Contact contact) {
        if ( contact == null ) {
            return null;
        }
        AddressEmbeddable address = contact.getAddress();
        if ( address == null ) {
            return null;
        }
        String country = address.getCountry();
        if ( country == null ) {
            return null;
        }
        return country;
    }

    protected RespPhone phoneEmbeddableToRespPhone(PhoneEmbeddable phoneEmbeddable) {
        if ( phoneEmbeddable == null ) {
            return null;
        }

        RespPhone.RespPhoneBuilder respPhone = RespPhone.builder();

        respPhone.type( phoneEmbeddable.getType() );
        respPhone.number( phoneEmbeddable.getNumber() );

        return respPhone.build();
    }

    protected List<RespPhone> phoneEmbeddableListToRespPhoneList(List<PhoneEmbeddable> list) {
        if ( list == null ) {
            return null;
        }

        List<RespPhone> list1 = new ArrayList<RespPhone>( list.size() );
        for ( PhoneEmbeddable phoneEmbeddable : list ) {
            list1.add( phoneEmbeddableToRespPhone( phoneEmbeddable ) );
        }

        return list1;
    }

    protected RespEmail emailEmbeddableToRespEmail(EmailEmbeddable emailEmbeddable) {
        if ( emailEmbeddable == null ) {
            return null;
        }

        RespEmail.RespEmailBuilder respEmail = RespEmail.builder();

        respEmail.type( emailEmbeddable.getType() );
        respEmail.address( emailEmbeddable.getAddress() );

        return respEmail.build();
    }

    protected List<RespEmail> emailEmbeddableListToRespEmailList(List<EmailEmbeddable> list) {
        if ( list == null ) {
            return null;
        }

        List<RespEmail> list1 = new ArrayList<RespEmail>( list.size() );
        for ( EmailEmbeddable emailEmbeddable : list ) {
            list1.add( emailEmbeddableToRespEmail( emailEmbeddable ) );
        }

        return list1;
    }

    protected PhoneEmbeddable reqPhoneToPhoneEmbeddable(ReqPhone reqPhone) {
        if ( reqPhone == null ) {
            return null;
        }

        PhoneEmbeddable.PhoneEmbeddableBuilder phoneEmbeddable = PhoneEmbeddable.builder();

        phoneEmbeddable.type( reqPhone.getType() );
        phoneEmbeddable.number( reqPhone.getNumber() );

        return phoneEmbeddable.build();
    }

    protected List<PhoneEmbeddable> reqPhoneListToPhoneEmbeddableList(List<ReqPhone> list) {
        if ( list == null ) {
            return null;
        }

        List<PhoneEmbeddable> list1 = new ArrayList<PhoneEmbeddable>( list.size() );
        for ( ReqPhone reqPhone : list ) {
            list1.add( reqPhoneToPhoneEmbeddable( reqPhone ) );
        }

        return list1;
    }

    protected EmailEmbeddable reqEmailToEmailEmbeddable(ReqEmail reqEmail) {
        if ( reqEmail == null ) {
            return null;
        }

        EmailEmbeddable.EmailEmbeddableBuilder emailEmbeddable = EmailEmbeddable.builder();

        emailEmbeddable.type( reqEmail.getType() );
        emailEmbeddable.address( reqEmail.getAddress() );

        return emailEmbeddable.build();
    }

    protected List<EmailEmbeddable> reqEmailListToEmailEmbeddableList(List<ReqEmail> list) {
        if ( list == null ) {
            return null;
        }

        List<EmailEmbeddable> list1 = new ArrayList<EmailEmbeddable>( list.size() );
        for ( ReqEmail reqEmail : list ) {
            list1.add( reqEmailToEmailEmbeddable( reqEmail ) );
        }

        return list1;
    }
}
