package pillihuaman.com.pe.support.RequestResponse.dto.Mapper;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.support.RequestResponse.RespProduct;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqFileMetadata;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqProduct;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqSizeStock;
import pillihuaman.com.pe.support.RequestResponse.dto.RespFileMetadata;
import pillihuaman.com.pe.support.repository.product.FileMetadata;
import pillihuaman.com.pe.support.repository.product.Product;
import pillihuaman.com.pe.support.repository.product.ProductInventory;
import pillihuaman.com.pe.support.repository.product.ProductMeasurement;
import pillihuaman.com.pe.support.repository.product.ProductMedia;
import pillihuaman.com.pe.support.repository.product.ProductPricing;
import pillihuaman.com.pe.support.repository.product.SizeStock;
import pillihuaman.com.pe.support.repository.product.SpecificationGroup;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-28T10:35:08-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Amazon.com Inc.)"
)
@Component
public class MapperProductImpl implements MapperProduct {

    @Autowired
    private MapperFileMetadata mapperFileMetadata;

    @Override
    public RespProduct toRespProduct(Product product) {
        if ( product == null ) {
            return null;
        }

        RespProduct respProduct = new RespProduct();

        respProduct.setId( objectIdToString( product.getId() ) );
        respProduct.setProductCode( product.getProductCode() );
        respProduct.setBarcode( product.getBarcode() );
        respProduct.setSku( product.getSku() );
        respProduct.setUpc( product.getUpc() );
        respProduct.setName( product.getName() );
        respProduct.setDescription( product.getDescription() );
        respProduct.setCategory( product.getCategory() );
        respProduct.setSubcategory( product.getSubcategory() );
        respProduct.setSupplierId( objectIdToString( product.getSupplierId() ) );
        respProduct.setManufacturer( product.getManufacturer() );
        respProduct.setBrand( product.getBrand() );
        respProduct.setExpirationDate( dateToString( product.getExpirationDate() ) );
        respProduct.setManufacturingDate( dateToString( product.getManufacturingDate() ) );
        respProduct.setPricing( productPricingToProductPricing( product.getPricing() ) );
        respProduct.setInventory( productInventoryToProductInventory( product.getInventory() ) );
        respProduct.setMedia( productMediaToProductMedia( product.getMedia() ) );
        respProduct.setStatus( product.isStatus() );
        respProduct.setFileMetadata( fileMetadataListToRespFileMetadataList( product.getFileMetadata() ) );
        List<String> list1 = product.getTags();
        if ( list1 != null ) {
            respProduct.setTags( new ArrayList<String>( list1 ) );
        }
        List<ProductMeasurement> list2 = product.getMeasurements();
        if ( list2 != null ) {
            respProduct.setMeasurements( new ArrayList<ProductMeasurement>( list2 ) );
        }
        respProduct.setSalesGuide( product.getSalesGuide() );
        List<SpecificationGroup> list3 = product.getSpecifications();
        if ( list3 != null ) {
            respProduct.setSpecifications( new ArrayList<SpecificationGroup>( list3 ) );
        }

        return respProduct;
    }

    @Override
    public Product toProduct(ReqProduct reqProduct) {
        if ( reqProduct == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.id( stringToObjectId( reqProduct.getId() ) );
        product.productCode( reqProduct.getProductCode() );
        product.barcode( reqProduct.getBarcode() );
        product.sku( reqProduct.getSku() );
        product.upc( reqProduct.getUpc() );
        product.name( reqProduct.getName() );
        product.description( reqProduct.getDescription() );
        product.category( reqProduct.getCategory() );
        product.subcategory( reqProduct.getSubcategory() );
        product.supplierId( stringToObjectId( reqProduct.getSupplierId() ) );
        product.manufacturer( reqProduct.getManufacturer() );
        product.brand( reqProduct.getBrand() );
        if ( reqProduct.getExpirationDate() != null ) {
            product.expirationDate( stringToDate( new SimpleDateFormat().format( reqProduct.getExpirationDate() ) ) );
        }
        if ( reqProduct.getManufacturingDate() != null ) {
            product.manufacturingDate( stringToDate( new SimpleDateFormat().format( reqProduct.getManufacturingDate() ) ) );
        }
        product.pricing( productPricingToProductPricing1( reqProduct.getPricing() ) );
        product.inventory( productInventoryToProductInventory1( reqProduct.getInventory() ) );
        product.media( productMediaToProductMedia1( reqProduct.getMedia() ) );
        if ( reqProduct.getStatus() != null ) {
            product.status( reqProduct.getStatus() );
        }
        List<String> list = reqProduct.getTags();
        if ( list != null ) {
            product.tags( new ArrayList<String>( list ) );
        }
        List<ProductMeasurement> list1 = reqProduct.getMeasurements();
        if ( list1 != null ) {
            product.measurements( new ArrayList<ProductMeasurement>( list1 ) );
        }
        product.salesGuide( reqProduct.getSalesGuide() );
        product.fileMetadata( reqFileMetadataListToFileMetadataList( reqProduct.getFileMetadata() ) );
        List<SpecificationGroup> list3 = reqProduct.getSpecifications();
        if ( list3 != null ) {
            product.specifications( new ArrayList<SpecificationGroup>( list3 ) );
        }

        return product.build();
    }

    protected ReqProduct.ProductPricing productPricingToProductPricing(ProductPricing productPricing) {
        if ( productPricing == null ) {
            return null;
        }

        ReqProduct.ProductPricing productPricing1 = new ReqProduct.ProductPricing();

        if ( productPricing.getCostPrice() != null ) {
            productPricing1.setCostPrice( BigDecimal.valueOf( productPricing.getCostPrice() ) );
        }
        if ( productPricing.getSellingPrice() != null ) {
            productPricing1.setSellingPrice( BigDecimal.valueOf( productPricing.getSellingPrice() ) );
        }
        if ( productPricing.getDiscount() != null ) {
            productPricing1.setDiscount( BigDecimal.valueOf( productPricing.getDiscount() ) );
        }
        productPricing1.setCurrency( productPricing.getCurrency() );

        return productPricing1;
    }

    protected ReqProduct.ProductInventory productInventoryToProductInventory(ProductInventory productInventory) {
        if ( productInventory == null ) {
            return null;
        }

        ReqProduct.ProductInventory productInventory1 = new ReqProduct.ProductInventory();

        productInventory1.setUnitMeasure( productInventory.getUnitMeasure() );
        if ( productInventory.getMinStock() != null ) {
            productInventory1.setMinStock( productInventory.getMinStock() );
        }
        if ( productInventory.getMaxStock() != null ) {
            productInventory1.setMaxStock( productInventory.getMaxStock() );
        }
        productInventory1.setFeatured( productInventory.isFeatured() );
        productInventory1.setNewArrival( productInventory.isNewArrival() );
        productInventory1.setBatch( productInventory.getBatch() );
        if ( productInventory.getWeight() != null ) {
            productInventory1.setWeight( productInventory.getWeight() );
        }
        if ( productInventory.getHeight() != null ) {
            productInventory1.setHeight( productInventory.getHeight() );
        }
        if ( productInventory.getWidth() != null ) {
            productInventory1.setWidth( productInventory.getWidth() );
        }
        if ( productInventory.getLength() != null ) {
            productInventory1.setLength( productInventory.getLength() );
        }

        return productInventory1;
    }

    protected ReqProduct.ProductMedia productMediaToProductMedia(ProductMedia productMedia) {
        if ( productMedia == null ) {
            return null;
        }

        ReqProduct.ProductMedia productMedia1 = new ReqProduct.ProductMedia();

        List<String> list = productMedia.getImageUrls();
        if ( list != null ) {
            productMedia1.setImageUrls( new ArrayList<String>( list ) );
        }
        productMedia1.setThumbnailUrl( productMedia.getThumbnailUrl() );
        List<String> list1 = productMedia.getTags();
        if ( list1 != null ) {
            productMedia1.setTags( new ArrayList<String>( list1 ) );
        }
        productMedia1.setSeoTitle( productMedia.getSeoTitle() );
        productMedia1.setSeoDescription( productMedia.getSeoDescription() );

        return productMedia1;
    }

    protected List<RespFileMetadata> fileMetadataListToRespFileMetadataList(List<FileMetadata> list) {
        if ( list == null ) {
            return null;
        }

        List<RespFileMetadata> list1 = new ArrayList<RespFileMetadata>( list.size() );
        for ( FileMetadata fileMetadata : list ) {
            list1.add( mapperFileMetadata.toDto( fileMetadata ) );
        }

        return list1;
    }

    protected ProductPricing productPricingToProductPricing1(ReqProduct.ProductPricing productPricing) {
        if ( productPricing == null ) {
            return null;
        }

        ProductPricing productPricing1 = new ProductPricing();

        if ( productPricing.getCostPrice() != null ) {
            productPricing1.setCostPrice( productPricing.getCostPrice().doubleValue() );
        }
        if ( productPricing.getSellingPrice() != null ) {
            productPricing1.setSellingPrice( productPricing.getSellingPrice().doubleValue() );
        }
        if ( productPricing.getDiscount() != null ) {
            productPricing1.setDiscount( productPricing.getDiscount().doubleValue() );
        }
        productPricing1.setCurrency( productPricing.getCurrency() );

        return productPricing1;
    }

    protected ProductInventory productInventoryToProductInventory1(ReqProduct.ProductInventory productInventory) {
        if ( productInventory == null ) {
            return null;
        }

        ProductInventory productInventory1 = new ProductInventory();

        productInventory1.setUnitMeasure( productInventory.getUnitMeasure() );
        productInventory1.setMinStock( productInventory.getMinStock() );
        productInventory1.setMaxStock( productInventory.getMaxStock() );
        productInventory1.setFeatured( productInventory.isFeatured() );
        productInventory1.setNewArrival( productInventory.isNewArrival() );
        productInventory1.setBatch( productInventory.getBatch() );
        productInventory1.setWeight( productInventory.getWeight() );
        productInventory1.setHeight( productInventory.getHeight() );
        productInventory1.setWidth( productInventory.getWidth() );
        productInventory1.setLength( productInventory.getLength() );

        return productInventory1;
    }

    protected ProductMedia productMediaToProductMedia1(ReqProduct.ProductMedia productMedia) {
        if ( productMedia == null ) {
            return null;
        }

        ProductMedia productMedia1 = new ProductMedia();

        List<String> list = productMedia.getImageUrls();
        if ( list != null ) {
            productMedia1.setImageUrls( new ArrayList<String>( list ) );
        }
        productMedia1.setThumbnailUrl( productMedia.getThumbnailUrl() );
        List<String> list1 = productMedia.getTags();
        if ( list1 != null ) {
            productMedia1.setTags( new ArrayList<String>( list1 ) );
        }
        productMedia1.setSeoTitle( productMedia.getSeoTitle() );
        productMedia1.setSeoDescription( productMedia.getSeoDescription() );

        return productMedia1;
    }

    protected SizeStock reqSizeStockToSizeStock(ReqSizeStock reqSizeStock) {
        if ( reqSizeStock == null ) {
            return null;
        }

        SizeStock sizeStock = new SizeStock();

        sizeStock.setSize( reqSizeStock.getSize() );
        sizeStock.setStock( reqSizeStock.getStock() );

        return sizeStock;
    }

    protected List<SizeStock> reqSizeStockListToSizeStockList(List<ReqSizeStock> list) {
        if ( list == null ) {
            return null;
        }

        List<SizeStock> list1 = new ArrayList<SizeStock>( list.size() );
        for ( ReqSizeStock reqSizeStock : list ) {
            list1.add( reqSizeStockToSizeStock( reqSizeStock ) );
        }

        return list1;
    }

    protected FileMetadata reqFileMetadataToFileMetadata(ReqFileMetadata reqFileMetadata) {
        if ( reqFileMetadata == null ) {
            return null;
        }

        FileMetadata.FileMetadataBuilder fileMetadata = FileMetadata.builder();

        fileMetadata.id( reqFileMetadata.getId() );
        fileMetadata.filename( reqFileMetadata.getFilename() );
        fileMetadata.contentType( reqFileMetadata.getContentType() );
        fileMetadata.size( reqFileMetadata.getSize() );
        fileMetadata.hashCode( reqFileMetadata.getHashCode() );
        fileMetadata.dimension( reqFileMetadata.getDimension() );
        fileMetadata.userId( reqFileMetadata.getUserId() );
        fileMetadata.uploadTimestamp( reqFileMetadata.getUploadTimestamp() );
        fileMetadata.status( reqFileMetadata.getStatus() );
        fileMetadata.order( reqFileMetadata.getOrder() );
        fileMetadata.typeFile( reqFileMetadata.getTypeFile() );
        fileMetadata.url( reqFileMetadata.getUrl() );
        fileMetadata.position( reqFileMetadata.getPosition() );
        fileMetadata.productId( reqFileMetadata.getProductId() );
        fileMetadata.sizeStock( reqSizeStockListToSizeStockList( reqFileMetadata.getSizeStock() ) );

        return fileMetadata.build();
    }

    protected List<FileMetadata> reqFileMetadataListToFileMetadataList(List<ReqFileMetadata> list) {
        if ( list == null ) {
            return null;
        }

        List<FileMetadata> list1 = new ArrayList<FileMetadata>( list.size() );
        for ( ReqFileMetadata reqFileMetadata : list ) {
            list1.add( reqFileMetadataToFileMetadata( reqFileMetadata ) );
        }

        return list1;
    }
}
