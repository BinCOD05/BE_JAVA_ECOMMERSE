package vn.web.Converter;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.web.Controller.Response.CartItemResponse;
import vn.web.Model.CartItem;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

        @Mapping(source = "product.color" , target = "color")
        @Mapping(source = "product.price" , target = "price")
//        @Mapping(source = "product.productImages.imageUrl" , target = "productImage")
        @Mapping(source = "selected" , target = "selected")
        @Mapping(source = "product.name" , target = "productName")
        @Mapping(source = "product.id" , target = "productId")
        CartItemResponse toDTOResponse(CartItem cartItem);
}
