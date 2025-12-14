package vn.web.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.web.Model.Voucher;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    Optional<Voucher> findByCode(String code);
}