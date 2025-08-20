package pillihuaman.com.pe.support.RequestResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqProduct;
import pillihuaman.com.pe.support.RequestResponse.dto.RespFileMetadata;
import pillihuaman.com.pe.support.repository.product.ProductMeasurement;
import pillihuaman.com.pe.support.repository.product.SalesGuide;
import pillihuaman.com.pe.support.repository.product.SpecificationGroup;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespProduct {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private String category;
    private String barcode;
    private Integer page;
    private Integer pagesize;
    // Identifiers
    private String productCode;
    private String sku;
    private String upc;
    private String subcategory;

    // Supplier & Manufacturer
    private String supplierId;
    private String supplierName;
    private String manufacturer;
    private String brand;
    private List<RespFileMetadata> fileMetadata;

    // Batching & Production
    private String expirationDate;       // ISO 8601 string

    private String manufacturingDate;
    private List<ProductMeasurement> measurements;
    // Embedded Objects
    private ReqProduct.ProductPricing pricing;
    private ReqProduct.ProductInventory inventory;
    private ReqProduct.ProductMedia media;
    private List<String> tags;
    // Status & Audit
    private Boolean status;
    private List<SpecificationGroup> specifications;
    private SalesGuide salesGuide;
    private List<QuantityBasedPrice> quantityPricing;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductPricing {
        private BigDecimal costPrice;
        private BigDecimal sellingPrice;
        private BigDecimal discount;
        private String currency;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductInventory {
        private String unitMeasure;
        private int minStock;
        private int maxStock;
        private boolean isFeatured;
        private boolean isNewArrival;
        private String batch;
        private double weight;
        private double height;
        private double width;
        private double length;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductMedia {
        private List<String> imageUrls;
        private String thumbnailUrl;
        private List<String> tags;
        private String seoTitle;
        private String seoDescription;
    }


}

