package vn.web.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;



@Entity
@Table(name = "cart_item")
@Getter
@Setter
public class CartItem extends AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id ;

    @Column(name = "qty")
    private Integer qty ;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart ;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product ;
}
