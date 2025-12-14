package vn.web.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.web.Model.Product;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product , Long>, JpaSpecificationExecutor<Product> {

    @Query("SELECT p FROM Product p " + " LEFT JOIN FETCH p.brand " + " LEFT JOIN FETCH p.category "
            + "LEFT JOIN FETCH p.inventory"
            + " WHERE p.id = :id")
    Optional<Product> findByIdFullInfo(@Param("id") long id);


//    Entity graph tự động fetch trước các data
    @Override
    @EntityGraph(attributePaths = {"brand", "category" , "inventory"})
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);
}
