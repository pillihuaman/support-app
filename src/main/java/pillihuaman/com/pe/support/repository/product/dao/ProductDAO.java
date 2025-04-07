package pillihuaman.com.pe.support.repository.product.dao;



import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqProduct;
import pillihuaman.com.pe.support.repository.BaseMongoRepository;
import pillihuaman.com.pe.support.repository.product.Product;


import java.util.List;

public interface ProductDAO extends BaseMongoRepository<Product> {

    /**
     * Lists Products based on specific request criteria.
     *
     * @param reqProduct The request criteria for filtering Products.
     * @return A list of Products matching the criteria.
     */
    List<Product> listProducts(ReqProduct reqProduct);

    /**
     * Saves or updates an Product entity.
     *
     * @param Product The Product entity to save or update.
     * @param token    The JWT token containing user context.
     * @return The saved or updated Product entity.
     */
    Product saveProduct(Product Product, MyJsonWebToken token);

    /**
     * Finds Products associated with a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of Products linked to the specified user.
     */
    List<Product> findByUserId(String userId);

    boolean deleteInactiveProduct(MyJsonWebToken jwt, String id);

}
