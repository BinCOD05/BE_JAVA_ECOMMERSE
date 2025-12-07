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
            + " WHERE p.id = :id")
    Optional<Product> findByIdFullInfo(@Param("id") long id);


    @Override
    @EntityGraph(attributePaths = {"brand", "category" , "inventory"}) // Tự động FETCH brand và category
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);
}
