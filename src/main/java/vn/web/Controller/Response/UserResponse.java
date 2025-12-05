package vn.web.Controller.Response;


import lombok.*;
import vn.web.Common.Gender;
import vn.web.Model.AddressEntity;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UserResponse {
    private Long id ;
    private String username;
    private String fullName;
    private String email ;
    private String phone ;
    private Gender gender ;
    private LocalDateTime createdAt ;
//    private List<AddressEntity> addressEntityList;
}
