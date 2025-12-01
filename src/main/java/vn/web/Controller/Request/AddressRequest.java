package vn.web.Controller.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.web.Common.AddressType;

import java.io.Serializable;



@Getter
@Setter
public class AddressRequest implements Serializable {

    @NotBlank
    private Long userid ;
    private String ward ;
    private String street ;
    private String number ;
    private String country ;
    private AddressType addressType ;
}
