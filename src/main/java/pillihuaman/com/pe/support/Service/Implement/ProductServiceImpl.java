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
import pillihuaman.com.pe.support.Service.SupplierService;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqSupplier;
import pillihuaman.com.pe.support.RequestResponse.dto.RespSupplier;
import pillihuaman.com.pe.support.repository.product.Product;
import pillihuaman.com.pe.support.repository.product.dao.ProductDAO;
import java.util.List;
@Component
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private MapperProduct mapperProduct;
    // Método para guardar o actualizar un Product
    @Override
    public RespBase<RespProduct> saveProduct(MyJsonWebToken jwt, ReqBase<ReqProduct> request) {
        // Obtener la carga útil de la solicitud
        ReqProduct reqProduct = request.getData();
        Product em =mapperProduct.toProduct(reqProduct);
        Product savedProduct = null;
            savedProduct = productDAO.saveProduct(em,
                    jwt);
        RespProduct respProduct =mapperProduct.toRespProduct(savedProduct);
        return new RespBase<>(respProduct);
    }

    // Método para obtener un Product específico
    @Override
    public RespBase<List<RespProduct>> getProduct(MyJsonWebToken jwt, ReqBase<ReqProduct> request) {
        List<Product> products = productDAO.listProducts(request.getData());
        if (products.isEmpty()) {
            return new RespBase<>(null);
        }
        List<RespProduct> responseList = products.stream().map(product -> {
            RespProduct resp =mapperProduct.toRespProduct(product);
            // Si hay supplierId, busca el nombre y lo asigna
            if (resp.getSupplierId() != null && !resp.getSupplierId().isEmpty()) {
                ReqSupplier reqSupplier = ReqSupplier.builder()
                        .id(resp.getSupplierId())
                        .build();

                List<RespSupplier> suppliers = supplierService.listSuppliers(reqSupplier);
                if (!suppliers.isEmpty()) {
                    resp.setSupplierName(suppliers.get(0).getName());
                }
            }

            return resp;
        }).toList();

        return new RespBase<>(responseList);
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

