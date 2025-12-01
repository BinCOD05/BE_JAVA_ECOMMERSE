package vn.web.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.web.Model.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity , Long>  {
    Optional<UserEntity> findByUsername(String userName);

    Optional<UserEntity> findById (long id);

    Optional<UserEntity> findByEmail(String email);
}
