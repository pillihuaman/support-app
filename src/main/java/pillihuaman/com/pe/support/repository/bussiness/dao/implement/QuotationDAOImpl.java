package pillihuaman.com.pe.support.repository.bussiness.dao.implement;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.repository.AzureAbstractMongoRepositoryImpl;
import pillihuaman.com.pe.support.repository.bussiness.Quotation;
import pillihuaman.com.pe.support.repository.bussiness.dao.QuotationDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class QuotationDAOImpl extends AzureAbstractMongoRepositoryImpl<Quotation> implements QuotationDAO {

    /**
     * Constructor to set up the database connection details.
     */
    public QuotationDAOImpl() {
        DS_WRITE = Constantes.DW; // Database name from constants
        // Assuming you add this constant to your Constantes.java file
        // For example: public static final String COLLECTION_QUOTATION = "quotations";
        COLLECTION = "quotations"; // Explicit collection name
    }

    /**
     * Provides the entity class type to the abstract repository.
     *
     * @return The Quotation class.
     */
    @Override
    public Class<Quotation> provideEntityClass() {
        return Quotation.class;
    }

    @Override
    public Quotation saveQuotation(Quotation quotation, MyJsonWebToken token) {
        if (quotation == null) {
            return null;
        }
        MongoCollection<Quotation> collection = getCollection(this.COLLECTION, Quotation.class);

        // Logic for a new document (INSERT)
        if (quotation.getId() == null) {
            quotation.setId(new ObjectId()); // Generate a new ID for the document
            AuditEntity audit = new AuditEntity();
            audit.setDateRegister(new Date());

            if (token != null && token.getUser() != null) {
                audit.setCodUser(token.getUser().getId());
                audit.setMail(token.getUser().getMail());
            }
            quotation.setAudit(audit);
            collection.insertOne(quotation);
        }
        // Logic for an existing document (UPDATE)
        else {
            // Retrieve existing audit info or create new if it's null
            AuditEntity audit = quotation.getAudit() != null ? quotation.getAudit() : new AuditEntity();
            audit.setDateUpdate(new Date());

            if (token != null && token.getUser() != null) {
                audit.setCodUserUpdate(token.getUser().getId());
                audit.setMailUpdate(token.getUser().getMail());
            }
            quotation.setAudit(audit);

            // Replace the entire document with the updated version
            collection.replaceOne(Filters.eq("_id", quotation.getId()), quotation);
        }

        return quotation;
    }

    @Override
    public Optional<Quotation> findById(String id) {
        if (id == null || !ObjectId.isValid(id)) {
            return Optional.empty();
        }
        MongoCollection<Quotation> collection = getCollection(this.COLLECTION, Quotation.class);
        Quotation quotation = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        return Optional.ofNullable(quotation);
    }

    @Override
    public List<Quotation> findAll() {
        MongoCollection<Quotation> collection = getCollection(this.COLLECTION, Quotation.class);
        return collection.find().into(new ArrayList<>());
    }

    @Override
    public boolean deleteById(String id, MyJsonWebToken token) {
        if (id == null || !ObjectId.isValid(id)) {
            return false;
        }
        MongoCollection<Quotation> collection = getCollection(this.COLLECTION, Quotation.class);
        DeleteResult result = collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
        return result.getDeletedCount() > 0;
    }
}