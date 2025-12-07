package vn.web.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.web.Common.RoleType;
import vn.web.Model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role , Integer> {
    Optional<Role> findByName(RoleType name);
}
