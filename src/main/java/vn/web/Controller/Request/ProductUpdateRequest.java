package vn.web.Controller.Request;

import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProductUpdateRequest implements Serializable {
    private String name ;
}
