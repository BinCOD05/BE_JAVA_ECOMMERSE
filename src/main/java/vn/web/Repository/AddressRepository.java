package vn.web.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.web.Model.AddressEntity;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity , Long > {
    Optional<AddressEntity> findById(long id );
    List<AddressEntity> findByUserId(long userid);
}
