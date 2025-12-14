package vn.web.Controller.Request;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProductUpdateRequest implements Serializable {
    private String name;
    private String color;
    private String storage;
    private BigDecimal price;
    private String description;
    private Boolean isActive;
    private Long brandId;
    private Long categoryId;
    private List<ProductCreationRequest.ProductSpecReqDTO> specs;
    private List<ProductCreationRequest.ProductImageReqDTO> images;
    @Data
    public static class ProductSpecReqDTO {
        private String name;
        private String value;
    }
    @Data
    public static class ProductImageReqDTO {
        private String imageUrl;
        private Boolean isPrimary;
        private Integer sortOrder;
    }
}
