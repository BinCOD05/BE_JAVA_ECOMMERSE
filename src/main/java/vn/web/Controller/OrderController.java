package vn.web.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.web.Controller.Request.OrderRequest;
import vn.web.Services.OrderService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getMyOrders() {
        return ResponseEntity.ok(orderService.getMyOrders());
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<?> getOrderDetails(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/admin/orders")
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrdersForAdmin());
    }

    @PutMapping("/admin/orders/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }



    @PutMapping("/admin/order-items/{id}/imei")
    public ResponseEntity<?> inputImei(
            @PathVariable Long id,
            @RequestParam String imei
    ) {
        orderService.inputImei(id, imei);
        return ResponseEntity.ok("Đã cập nhật IMEI thành công");
    }

    @GetMapping("/warranty")
    public ResponseEntity<?> checkWarranty(@RequestParam String imei) {
        return ResponseEntity.ok(orderService.checkWarranty(imei));
    }
}