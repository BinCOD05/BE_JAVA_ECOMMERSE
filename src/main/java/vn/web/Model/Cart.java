package vn.web.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;


@Entity
@Table(name = "cart")
@Getter
@Setter
public class Cart extends  AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id ;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user ;

    @OneToMany(mappedBy = "cart" , fetch = FetchType.LAZY)
    private Set<CartItem> cartItemSet;

}
