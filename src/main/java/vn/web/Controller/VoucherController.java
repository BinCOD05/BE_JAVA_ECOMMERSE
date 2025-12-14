package vn.web.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.web.Model.Voucher;
import vn.web.Services.VoucherService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VoucherController {

    private final VoucherService voucherService;


    @GetMapping("/vouchers")
    public ResponseEntity<?> getAllVouchers() {
        return ResponseEntity.ok(voucherService.getAllVouchers());
    }

    @PostMapping("/admin/vouchers")
    public ResponseEntity<?> createVoucher(@RequestBody Voucher voucher) {
        try {
            return ResponseEntity.ok(voucherService.createVoucher(voucher));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}