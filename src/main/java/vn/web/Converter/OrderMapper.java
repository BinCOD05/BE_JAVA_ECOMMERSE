package vn.web.Converter;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.web.Controller.Response.OrderItemResponse;
import vn.web.Controller.Response.OrderResponse;
import vn.web.Model.Order;
import vn.web.Model.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "address", expression = "java(buildAddress(order))")
    OrderResponse toDTOResponse(Order order);

    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "imei", target = "imei")
    OrderItemResponse toItemResponse(OrderItem item);

    default String buildAddress(Order order) {
        if (order == null) return "";
        return (order.getShipLine1() != null ? order.getShipLine1() : "") + ", " +
                (order.getShipWard() != null ? order.getShipWard() : "") + ", " +
                (order.getShipCity() != null ? order.getShipCity() : "");
    }
}
