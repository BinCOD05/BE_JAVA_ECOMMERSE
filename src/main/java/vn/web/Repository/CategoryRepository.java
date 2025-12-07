package vn.web.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.web.Model.Category;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
