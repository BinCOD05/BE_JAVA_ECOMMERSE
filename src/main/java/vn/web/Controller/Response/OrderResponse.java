package vn.web.Controller.Response;

import lombok.Getter;
import lombok.Setter;
import vn.web.Common.OrderStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
public class OrderResponse implements Serializable {
    private Long id;
    private String code;
    private String status;
    private LocalDateTime orderDate;
    private BigDecimal totalPrice;

    private String name;
    private String phoneNumber;
    private String address;

    private List<OrderItemResponse> orderItems;
}