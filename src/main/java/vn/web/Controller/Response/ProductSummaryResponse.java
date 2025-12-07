package vn.web.Controller.Response;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSummaryResponse implements Serializable {
    private Long id ;
    private String name ;
    private String brand ;
    private String category;
    private String  color ;
    private String storage ;
    private String thumbnailUrl ;
    private int quantity ;
}
