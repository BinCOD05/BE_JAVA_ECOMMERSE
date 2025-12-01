package vn.web.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.web.Common.AddressType;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "address")
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;


    @Column(name = "recipient")
    private String recipient ;

    @Column(name = "phone")
    private String phone ;

    @Column(name = "street")
    private String street ;

    @Column(name = "ward")
    private String ward ;

    @Column(name = "city")
    private String city ;

    @Column(name = "line")
    private String line ;

    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity user ;


    @Column( name = "address_type")
    private AddressType  addressType ;

    @Column(name = "is_default")
    private boolean isDefault ;
}
