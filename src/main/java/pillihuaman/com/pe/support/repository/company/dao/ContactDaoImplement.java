package pillihuaman.com.pe.support.repository.company.dao;

// Archivo: ContactDaoImplement.java

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqContact;
import pillihuaman.com.pe.support.repository.AzureAbstractMongoRepositoryImpl;
import pillihuaman.com.pe.support.repository.company.Company;
import pillihuaman.com.pe.support.repository.company.ContactDAO;
import pillihuaman.com.pe.support.repository.contact.Contact;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class ContactDaoImplement extends AzureAbstractMongoRepositoryImpl<Contact> implements ContactDAO {

    public ContactDaoImplement() {
        DS_WRITE = Constantes.DW;
        COLLECTION = "contacts";
    }

    @Override
    public Class<Contact> provideEntityClass() {
        return Contact.class;
    }

    @Override
    public List<Contact> listContactsByTenant(ReqContact req) {
        MongoCollection<Contact> collection = getCollection(this.COLLECTION, Contact.class);
        List<Document> filters = new ArrayList<>();
        filters.add(new Document("status", true));

        if (req.getTenantid() != null && !req.getTenantid().isBlank()) {
            try {
                filters.add(new Document("company.$id", new ObjectId(req.getTenantid())));
            } catch (IllegalArgumentException ignored) {}
        }

        Document query = new Document("$and", filters);
        return collection.find(query).into(new ArrayList<>());
    }

    @Override
    public Contact saveContact(Contact contact, MyJsonWebToken token) {
        if (contact == null || token == null || token.getUser() == null || token.getUser().getTenantId() == null) {
            throw new IllegalArgumentException("El contacto y el token con información del tenant son obligatorios.");
        }

        ObjectId companyIdFromToken = new ObjectId(token.getUser().getTenantId());
        MongoCollection<Contact> collection = getCollection(this.COLLECTION, Contact.class);

        if (contact.getId() == null) { // Creación
            contact.setId(new ObjectId());
            contact.setStatus(true);
            contact.setCompany(Company.builder().id(companyIdFromToken).build());

            AuditEntity audit = new AuditEntity();
            audit.setMail(token.getUser().getMail());
            audit.setDateRegister(new Date());
            contact.setAudit(audit);

            collection.insertOne(contact);
        } else { // Actualización
            Document query = new Document("_id", contact.getId());
            Contact existing = collection.find(query).first();

            if (existing == null) {
                throw new IllegalStateException("Contacto no encontrado con ID: " + contact.getId());
            }

            // Aquí iría la lógica de seguridad para verificar pertenencia al tenant

            AuditEntity audit = existing.getAudit() != null ? existing.getAudit() : new AuditEntity();
            audit.setMailUpdate(token.getUser().getMail());
            audit.setDateUpdate(new Date());
            contact.setAudit(audit);
            contact.setCompany(existing.getCompany()); // Mantiene la compañía original
            contact.setStatus(true);

            collection.replaceOne(query, contact);
        }
        return contact;
    }

    @Override
    public boolean deleteContact(String id, MyJsonWebToken token) {
        if (id == null || id.isBlank() || token == null) {
            throw new IllegalArgumentException("ID y token son obligatorios.");
        }

        ObjectId contactId;
        try {
            contactId = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            return false;
        }

        MongoCollection<Contact> collection = getCollection(this.COLLECTION, Contact.class);
        Document query = new Document("_id", contactId);

        Contact contact = collection.find(query).first();
        if (contact != null) {
            // Aquí iría la lógica de seguridad para verificar pertenencia al tenant

            contact.setStatus(false); // Borrado lógico
            AuditEntity audit = contact.getAudit() != null ? contact.getAudit() : new AuditEntity();
            audit.setMailUpdate(token.getUser().getMail());
            audit.setDateUpdate(new Date());
            contact.setAudit(audit);

            collection.replaceOne(query, contact);
            return true;
        }
        return false;
    }
}