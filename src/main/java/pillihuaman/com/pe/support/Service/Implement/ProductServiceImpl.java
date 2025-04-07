package pillihuaman.com.pe.support.Service.Implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.common.ReqBase;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.support.RequestResponse.RespProduct;
import pillihuaman.com.pe.support.RequestResponse.dto.Mapper.MapperProduct;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqProduct;
import pillihuaman.com.pe.support.Service.ProductService;
import pillihuaman.com.pe.support.repository.product.Product;
import pillihuaman.com.pe.support.repository.product.dao.ProductDAO;
import java.util.List;
@Component
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDAO productDAO;

    // Método para guardar o actualizar un Product
    @Override
    public RespBase<RespProduct> saveProduct(MyJsonWebToken jwt, ReqBase<ReqProduct> request) {
        // Obtener la carga útil de la solicitud
        ReqProduct reqProduct = request.getData();
        Product em = MapperProduct.INSTANCE.toProduct(reqProduct);
        Product savedProduct = null;
            savedProduct = productDAO.saveProduct(em,
                    jwt);

        RespProduct respProduct = MapperProduct.INSTANCE.toRespProduct(savedProduct);
        return new RespBase<>(respProduct);
    }

    // Método para obtener un Product específico
    @Override
    public RespBase<List<RespProduct>> getProduct(MyJsonWebToken jwt, ReqBase<ReqProduct> request) {
        List<Product> Products = productDAO.listProducts(request.getData());
        if (Products.isEmpty()) {
            return new RespBase<>(null);
        }
        return new RespBase<>(Products.stream()
                .map(MapperProduct.INSTANCE::toRespProduct)
                .toList());
    }

    // Método para listar Products asociados a un usuario
    @Override
    public RespBase<RespProduct> listProductsByUser(MyJsonWebToken jwt, ReqBase<ReqProduct> request) {
        // Obtener la carga útil de la solicitud
        ReqProduct reqProduct = request.getData();
        List<Product> Products = productDAO.findByUserId(reqProduct.getId());
        RespBase<List<RespProduct>> res = new RespBase<>(null);
        return null;
    }

    @Override
    public RespBase<Boolean> deleteProduct(MyJsonWebToken jwt, String id) {
        boolean deleted = productDAO.deleteInactiveProduct(jwt, id);
        return new RespBase<>(deleted);
    }
}

