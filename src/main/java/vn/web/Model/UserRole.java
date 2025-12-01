package vn.web.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "user_role")
@Getter
@Setter
public class UserRole extends AbstractEntity<Integer> implements Serializable {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user ;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role ;

}
