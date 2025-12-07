package vn.web.Controller.Request;


import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductFilterRequest implements Serializable {
    private String name ;
}
