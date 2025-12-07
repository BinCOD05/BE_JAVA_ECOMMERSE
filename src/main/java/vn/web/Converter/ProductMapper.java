package vn.web.Converter;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.web.Controller.Request.ProductCreationRequest;
import vn.web.Controller.Response.ProductDetailResponse;
import vn.web.Model.Product;
import vn.web.Model.ProductImage;
import vn.web.Model.ProductSpecs;

@Mapper(componentModel = "spring")
public interface ProductMapper{
    @Mapping(target = "productImages", source = "images")
    @Mapping(target = "productSpecs", source = "specs")
    @Mapping(target = "brand.id" , source = "brandId")
    @Mapping(target = "category.id" , source = "categoryId")
    Product toEntity(ProductCreationRequest request);

    @Mapping(target = "images", source = "productImages")
    @Mapping(target = "specs", source = "productSpecs")
    ProductDetailResponse toDTOResponse(Product product);
}
