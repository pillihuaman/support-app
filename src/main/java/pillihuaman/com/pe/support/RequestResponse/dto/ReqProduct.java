package pillihuaman.com.pe.support.RequestResponse.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import pillihuaman.com.pe.support.RequestResponse.QuantityBasedPrice;
import pillihuaman.com.pe.support.repository.product.ProductMeasurement;
import pillihuaman.com.pe.support.repository.product.SalesGuide;
import pillihuaman.com.pe.support.repository.product.SpecificationGroup;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqProduct {
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
    private String manufacturer;
    private String brand;
    private List<ReqFileMetadata> fileMetadata;
    private List<String> tags;
    private List<ProductMeasurement> measurements;
    private List<SpecificationGroup> specifications;
    // Batching & Production
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "GMT-5")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date expirationDate;       // ISO 8601 string
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "GMT-5")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date manufacturingDate;

    // Embedded Objects
    private ProductPricing pricing;
    private ProductInventory inventory;
    private ProductMedia media;
    private SalesGuide salesGuide;
    // Status & Audit
    private Boolean status;
    private List<QuantityBasedPrice> quantityPricing;
    // Clases internas anidadas o referencias externas
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SizeStock {
        private String size;
        private int stock;
    }

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