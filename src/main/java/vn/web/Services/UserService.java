package vn.web.Services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.web.Controller.Request.*;
import vn.web.Controller.Response.AddressResponse;
import vn.web.Controller.Response.UserPageResponse;
import vn.web.Controller.Response.UserResponse;

import java.util.List;

public interface UserService {

     UserResponse getUser(long id);

     UserPageResponse findALl(UserPageRequest req , Pageable pageable);

     UserResponse save(UserCreationRequest req);

     UserResponse update(UserUpdateRequest req , long id);

     void delete(long id);

     void changePassword(UserChangePasswdRequest req , long id);

}
