package pillihuaman.com.pe.support.repository.store.dao;

import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqStore;
import pillihuaman.com.pe.support.RequestResponse.RespStore;
import pillihuaman.com.pe.support.repository.BaseMongoRepository;
import pillihuaman.com.pe.support.repository.store.Store;

import java.util.List;

public interface StoreDAO extends BaseMongoRepository<Store> {

    /**
     * Lists stores based on specific request criteria.
     *
     * @param reqStore The request criteria for filtering stores.
     * @return A list of stores matching the criteria.
     */
    List<Store> listStores(ReqStore reqStore);

    /**
     * Saves or updates a Store entity.
     *
     * @param store The Store entity to save or update.
     * @param token The JWT token containing user context.
     * @return The saved or updated Store entity.
     */
    Store saveStore(Store store, MyJsonWebToken token);

    /**
     * Finds stores associated with a specific owner.
     *
     * @param ownerId The ID of the store owner.
     * @return A list of stores linked to the specified owner.
     */
    List<RespStore> findByOwnerId(String ownerId);

    /**
     * Deletes a store logically (sets status to inactive).
     *
     * @param token The JWT token for user authentication.
     * @param id    The ID of the store to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    boolean deleteInactiveStore(MyJsonWebToken token, String id);
}
