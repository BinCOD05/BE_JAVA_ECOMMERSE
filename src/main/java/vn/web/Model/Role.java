package vn.web.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id ;

    @Column(name = "name")
    private String name ;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "role")
    private Set<UserRole> userRoleSet ;
}
