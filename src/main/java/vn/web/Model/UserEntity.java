package vn.web.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.web.Common.Gender;
import vn.web.Common.UserStatus;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "user")
// 1. Khi gọi hàm delete(), Hibernate sẽ lén thay bằng câu update này
@SQLDelete(sql = "UPDATE user SET status = 'INACTIVE' WHERE id = ?")
// 2. Khi gọi hàm find/get, Hibernate tự động thêm điều kiện này vào
@Where(clause = "status = 'ACTIVE' ")
public class UserEntity extends AbstractEntity implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id ;

    @Column(name = "user_name" , unique = true , nullable = false)
    private String username;

    @Column(nullable = false , name = "password")
    private String password ;

    @Column(name = "full_name")
    private String fullName ;

    @Column(name = "phone_number")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender  ;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;

    @OneToMany(mappedBy = "user" , fetch = FetchType.LAZY)
    private List<AddressEntity> addressEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "user"  , fetch = FetchType.EAGER)
    private Set<UserRole> userRoleSet = new HashSet<>();

//    @OneToMany(mappedBy = "user" , fetch = FetchType.LAZY)
//    private Set<Cart> cartSet ;

//    @OneToMany(mappedBy = "user" , fetch = FetchType.LAZY)
//    private

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<Role> roleList = userRoleSet.stream().map(UserRole::getRole).toList();

        List<String> roleNames = roleList.stream().map(Role::getName).toList();

        return roleNames.stream().map(SimpleGrantedAuthority::new).toList();
    }
}
