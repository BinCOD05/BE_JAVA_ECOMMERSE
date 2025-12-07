package vn.web.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.web.Model.Brand;

public interface BrandRepository extends JpaRepository<Brand , Long> {
}
