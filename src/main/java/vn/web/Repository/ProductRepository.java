package vn.web.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.web.Model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product , Long>, JpaSpecificationExecutor {
}
