package pillihuaman.com.pe.support.repository.bussiness.dao;


import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.repository.BaseMongoRepository;
import pillihuaman.com.pe.support.repository.bussiness.Quotation; // Asegúrate de que la entidad Quotation esté en este paquete

import java.util.List;
import java.util.Optional;

public interface QuotationDAO extends BaseMongoRepository<Quotation> {

    /**
     * Saves a new or updates an existing quotation in the database.
     *
     * @param quotation The Quotation entity to save.
     * @param token     The JWT token for auditing purposes.
     * @return The saved Quotation entity with updated audit info.
     */
    Quotation saveQuotation(Quotation quotation, MyJsonWebToken token);

    /**
     * Finds a quotation by its unique MongoDB ObjectId string.
     *
     * @param id The string representation of the ObjectId.
     * @return An Optional containing the found Quotation, or empty if not found.
     */
    Optional<Quotation> findById(String id);

    /**
     * Retrieves all quotations from the database.
     *
     * @return A list of all Quotation entities.
     */
    List<Quotation> findAll();

    /**
     * Deletes a quotation from the database by its ID.
     *
     * @param id    The ID of the quotation to delete.
     * @param token The JWT token for auditing or logging purposes.
     * @return true if the document was successfully deleted, false otherwise.
     */
    boolean deleteById(String id, MyJsonWebToken token);
}