package vn.web.Controller.Response;

import lombok.*;
import vn.web.Common.AddressType;

import java.io.Serializable;



@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddressResponse   implements Serializable {
    private String ward ;
    private String street ;
    private String number ;
    private String country ;
    private AddressType addressType ;
}
