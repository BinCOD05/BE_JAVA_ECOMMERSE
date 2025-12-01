package vn.web.Services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.web.Controller.Request.AddressRequest;
import vn.web.Controller.Request.UserChangePasswdRequest;
import vn.web.Controller.Request.UserCreationRequest;
import vn.web.Controller.Request.UserUpdateRequest;
import vn.web.Controller.Response.AddressResponse;
import vn.web.Controller.Response.UserResponse;

import java.util.List;

public interface UserService {

     UserResponse getUser(long id);

     UserResponse save(UserCreationRequest req);

     UserResponse update(UserUpdateRequest req , long id);

     void delete(long id);

     void changePassword(UserChangePasswdRequest req , long id);

     Page<UserResponse> findAll(Pageable req);

     AddressResponse save (AddressRequest req) ;
//     Address
     AddressResponse getAddress();

     List<AddressResponse> getListAddress(long userid);
//     void delete(UserDeleteRequest req);
}
