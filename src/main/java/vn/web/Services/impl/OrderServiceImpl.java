package vn.web.Services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.web.Common.OrderStatus;
import vn.web.Controller.Request.OrderRequest;
import vn.web.Controller.Response.OrderResponse;
import vn.web.Converter.OrderMapper;
import vn.web.Model.*;
import vn.web.Repository.*;
import vn.web.Services.OrderService;
import vn.web.Util.SecurityUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository ;
    private final AddressRepository addressRepository ;
    private final CartItemRepository cartItemRepository ;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper ;
    private final OrderItemRepository orderItemRepository;
    private final VoucherRepository voucherRepository;

        @Override
        @Transactional
        public OrderResponse createOrder(OrderRequest req) {
            UserEntity currentUser = SecurityUtils.getCurrentUser() ;
            UserEntity user = userRepository.getReferenceById(currentUser.getId());
            AddressEntity address = addressRepository.findByIdAndUserId(req.getAddressId() , currentUser.getId()).orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ"));
            Order order = new Order() ;

            order.setName(user.getFullName());
            order.setUser(user);
            order.setPhoneNumber(user.getPhone());
            order.setNote(req.getNote());
            order.setShipCity(address.getCity());
            order.setShipDistrict(address.getDistrict());
            order.setStatus(OrderStatus.PENDING);
            List<CartItem> cartItems = cartItemRepository.findAllById(req.getCartItemIds());

            if(cartItems.isEmpty()){
                throw  new RuntimeException("Không có sản phẩm nào trong giỏ hàng");
            }

            List<OrderItem> orderItems = new ArrayList<>();

            BigDecimal totalPrice = BigDecimal.ZERO;

            for (CartItem cartItem : cartItems){
                OrderItem orderItem = new OrderItem() ;
                Product product  = cartItem.getProduct();

                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setProduct(product);
                orderItem.setOrder(order);
                orderItem.setPrice(product.getPrice());
                BigDecimal currentPrice = product.getPrice();

                BigDecimal quantity = BigDecimal.valueOf(cartItem.getQuantity());

                totalPrice = totalPrice.add(currentPrice.multiply(quantity));
                orderItems.add(orderItem);
            }
            if (req.getVoucherCode() != null && !req.getVoucherCode().trim().isEmpty()) {
                Voucher voucher = voucherRepository.findByCode(req.getVoucherCode())
                        .orElseThrow(() -> new RuntimeException("Mã giảm giá không tồn tại"));
                if (voucher.getQuantity() <= 0 || LocalDateTime.now().isAfter(voucher.getExpirationDate())) {
                    throw new RuntimeException("Mã giảm giá đã hết hạn hoặc hết lượt dùng");
                }

                totalPrice = totalPrice.subtract(voucher.getDiscountAmount());
                if (totalPrice.compareTo(BigDecimal.ZERO) < 0) {
                    totalPrice = BigDecimal.ZERO;
                }

                voucher.setQuantity(voucher.getQuantity() - 1);
                voucherRepository.save(voucher);
            }
            order.setTotalPrice(totalPrice);
            order.setOrderItems(orderItems);

            Order savedOrder = orderRepository.save(order);
            cartItemRepository.deleteAll(cartItems);

            return orderMapper.toDTOResponse(savedOrder);
        }

    @Override
    public List<OrderResponse> getMyOrders() {
        UserEntity currentUser = SecurityUtils.getCurrentUser();

        List<Order> orders = orderRepository.findByUserIdOrderByOrderDateDesc(currentUser.getId());

        List<OrderResponse> responseList = new ArrayList<>();
        for (Order order : orders) {
            responseList.add(orderMapper.toDTOResponse(order));
        }
        return responseList;
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        return orderMapper.toDTOResponse(order);
    }

    @Override
    public List<OrderResponse> getAllOrdersForAdmin() {

        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "orderDate"));

        List<OrderResponse> responseList = new ArrayList<>();
        for (Order order : orders) {
            responseList.add(orderMapper.toDTOResponse(order));
        }
        return responseList;
    }



    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

        try {
            OrderStatus statusEnum = OrderStatus.valueOf(newStatus.toUpperCase());
            order.setStatus(statusEnum);
            if (statusEnum == OrderStatus.DELIVERED) {
                LocalDateTime activationDate = LocalDateTime.now();

                for (OrderItem item : order.getOrderItems()) {
                    int months = (item.getProduct().getWarrantyPeriod() != null)
                            ? item.getProduct().getWarrantyPeriod()
                            : 12;

                    LocalDateTime expireDate = activationDate.plusMonths(months);

                    item.setWarrantyEndDate(expireDate);
                    // Lúc này IMEI phải được Admin nhập trước roiiiiiii
                }
                orderItemRepository.saveAll(order.getOrderItems());
            }
            Order updatedOrder = orderRepository.save(order);
            return orderMapper.toDTOResponse(updatedOrder);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Trạng thái không hợp lệ");
        }
    }





    public void inputImei(Long orderItemId, String imeiCode) {
        OrderItem item = orderItemRepository.findById(orderItemId).get();
        item.setImei(imeiCode);
        orderItemRepository.save(item);
    }

    @Transactional
    public void confirmDelivery(Long orderId) {
        Order order = orderRepository.findById(orderId).get();
        order.setStatus(OrderStatus.DELIVERED);

        for (OrderItem item : order.getOrderItems()) {
            if (item.getImei() != null) {
                int warrantyMonth = item.getProduct().getWarrantyPeriod();
                item.setWarrantyEndDate(LocalDateTime.now().plusMonths(warrantyMonth));
            }
        }
        orderRepository.save(order);
    }

    public String checkWarranty(String imeiInput) {
        OrderItem item = orderItemRepository.findByImei(imeiInput)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với IMEI: " + imeiInput));

        if (item.getWarrantyEndDate() == null) {
            return "Sản phẩm này chưa được kích hoạt bảo hành (Đơn hàng chưa hoàn tất).";
        }

        LocalDateTime now = LocalDateTime.now();
        Product product = item.getProduct();

        StringBuilder result = new StringBuilder();
        result.append("Sản phẩm: ").append(product.getName()).append("\n");
        result.append("IMEI: ").append(item.getImei()).append("\n");

        if (now.isBefore(item.getWarrantyEndDate())) {
            result.append("Trạng thái: CÒN BẢO HÀNH\n");
            result.append("Hết hạn vào: ").append(item.getWarrantyEndDate().toLocalDate());
        } else {
            result.append("Trạng thái: ĐÃ HẾT BẢO HÀNH\n");
            result.append("Ngày hết hạn: ").append(item.getWarrantyEndDate().toLocalDate());
        }

        return result.toString();
    }

}
