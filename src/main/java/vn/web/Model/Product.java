package vn.web.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "product")
@Setter
@Getter
public class Product extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id ;

    @Column(name = "name")
    private String name ;

    @Column(name = "color")
    private String color ;

    @Column(name = "storage")
    private String storage ;

    @Column(name = "description")
    private String description ;

    @Column(name = "is_active")
    private Boolean isActive ;

    @OneToMany(mappedBy = "product")
    private Set<CartItem> cartItemSet;

}
