package pillihuaman.com.pe.support.RequestResponse.dto.Mapper;

import javax.annotation.processing.Generated;
import pillihuaman.com.pe.support.RequestResponse.RespProduct;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqProduct;
import pillihuaman.com.pe.support.repository.product.Product;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-27T06:55:05-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (JetBrains s.r.o.)"
)
public class MapperProductImpl implements MapperProduct {

    @Override
    public RespProduct toRespProduct(Product product) {
        if ( product == null ) {
            return null;
        }

        RespProduct respProduct = new RespProduct();

        respProduct.setId( objectIdToString( product.getId() ) );
        respProduct.setName( product.getName() );
        respProduct.setDescription( product.getDescription() );
        respProduct.setCategory( product.getCategory() );
        respProduct.setBarcode( product.getBarcode() );

        return respProduct;
    }

    @Override
    public Product toProduct(ReqProduct reqProduct) {
        if ( reqProduct == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.id( stringToObjectId( reqProduct.getId() ) );
        product.name( reqProduct.getName() );
        product.description( reqProduct.getDescription() );
        product.category( reqProduct.getCategory() );
        product.barcode( reqProduct.getBarcode() );

        return product.build();
    }
}
