package vn.web.Controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.web.Controller.Request.AddressRequest;
import vn.web.Controller.Request.UserChangePasswdRequest;
import vn.web.Controller.Request.UserCreationRequest;
import vn.web.Controller.Request.UserUpdateRequest;
import vn.web.Controller.Response.ApiResponse;
import vn.web.Controller.Response.UserResponse;
import vn.web.Services.UserService;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/test")
@RequiredArgsConstructor
public class TestApiController {

        private final UserService userService ;

        @Operation( summary = "test api", tags = "Test API", description = " binh dep zai 0 thich noi nhieu")
        @GetMapping(value = "/check")
        public String check(){
            return " binh";
        }



        @Operation( summary = " get user details " , tags = "user" , description = " lay thong tin user qua param")
        @GetMapping(value = "/users/{id}")
        public ApiResponse<UserResponse> getUserDetails(@PathVariable long id ) {
            return ApiResponse.<UserResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("get user details successful")
                    .result(userService.getUser(id))
                    .build() ;
        }



        @Operation(summary = " create user " , tags = " create user " , description = " tao user moi ")
        @PostMapping(value = "/users")
        public ApiResponse<UserResponse> createUser(@RequestBody UserCreationRequest request){

            return ApiResponse.<UserResponse>builder()
                    .status(HttpStatus.CREATED.value())
                    .message("created user successful")
                    .result(userService.save(request))
                    .build();

        }


        @Operation(summary = "update user" , tags = "update user" , description = "cap nhat thong tin user")
        @PutMapping(value = "/users/{id}")
        public ApiResponse<UserResponse> updateUser(@RequestBody UserUpdateRequest request ,
                                                    @PathVariable long id
                                                    ){
            return ApiResponse.<UserResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("updated user info successful")
                    .result(userService.update(request , id))
                    .build();
        }


        @Operation(summary = "delete user" , tags = "delete user" , description = "dua user vao trang thai inactive")
        @DeleteMapping(value = "/users/{id}")
        public void deleteUser(@PathVariable long id){
            userService.delete(id);
        }

        @Operation
        @PatchMapping(value = "/users/change-pwd/{id}")
        public void changePassword(@RequestBody UserChangePasswdRequest request,
                                   @PathVariable long id){
            userService.changePassword(request , id);
        }

        @Operation(summary = "find user" , tags = "find user")
        @GetMapping(value = "/users")
        public ApiResponse<Page> findAll(Pageable pageable){
            return ApiResponse.<Page>builder()
                    .status(HttpStatus.OK.value())
                    .message("find user successful")
                    .result(userService.findAll(pageable))
                    .build();
        }




        @Operation(summary = "add address" , tags = "add address")
        @PostMapping(value = "/address")
        public ResponseEntity<Object> addAddress(@RequestBody AddressRequest AddressRequest){


            Map<String , Object> results = new LinkedHashMap<>();
            results.put("status" , HttpStatus.CREATED.value());
            results.put("message" , "create user successful");
            results.put("data", userService.save(AddressRequest));
            return new ResponseEntity<>(results , HttpStatus.OK);

        }

        @GetMapping(value = "/address/{userid}")
        public ResponseEntity<Object>  getListAddress(@PathVariable long userid ){

            Map<String , Object> results = new LinkedHashMap<>();
            results.put("status" , HttpStatus.CREATED.value());
            results.put("message" , "create user successful");
            results.put("data", userService.getListAddress(userid));
            return  new ResponseEntity<>(results , HttpStatus.OK) ;
        }
}

