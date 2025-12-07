package vn.web.Controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.web.Common.RoleType;
import vn.web.Controller.Request.*;
import vn.web.Controller.Response.ApiResponse;
import vn.web.Controller.Response.PageResponse;
import vn.web.Controller.Response.UserResponse;
import vn.web.Model.Role;
import vn.web.Services.UserService;
import vn.web.Util.SecurityUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService ;

    @Operation(summary = "Get profile" , tags = "Get Profile User")
    @GetMapping(value = "/me")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<UserResponse> getProfile(){
        long currentId  = SecurityUtils.getCurrentId();
        System.out.println(currentId);
        return ApiResponse.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .message("get profile successful")
                .result(userService.getUser(currentId))
                .build();
    }


    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<UserResponse> getUser(@PathVariable long id ){
        return ApiResponse.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .message("get profile successful")
                .result(userService.getUser(id))
                .build();
    }

    @Operation(summary = "create user by admin")
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<UserResponse> createUser(@RequestBody UserCreationRequest request){
        return ApiResponse.<UserResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("created user successful")
                .result(userService.save(request))
                .build();
    }


    @Operation(summary = "update user info" , tags = "update user" , description = "cập nhật thông tin user")
    @PutMapping(value = "/upd")
    @PreAuthorize("hasAuthority('user')")
    public ApiResponse<UserResponse> updateUser(@RequestBody UserUpdateRequest request){
        long currentId = SecurityUtils.getCurrentId();
        return ApiResponse.<UserResponse>builder()
                .status(HttpStatus.UPGRADE_REQUIRED.value())
                .message("created user successful")
                .result(userService.update(request , currentId))
                .build();
    }


    @PatchMapping(value = "/change-pwd")
    public ApiResponse<UserResponse> changePasswd(@RequestBody UserChangePasswdRequest request){
        long currentId = SecurityUtils.getCurrentId();
        userService.changePassword(request , currentId);
        return ApiResponse.<UserResponse>builder()
                .status(HttpStatus.UPGRADE_REQUIRED.value())
                .message("created user successful")
                .build();
    }

    @DeleteMapping(value = "/{id}")
    public ApiResponse<UserResponse> deleteUser(@PathVariable long id ){
        userService.delete(id);
        return ApiResponse.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .message("created user successful")
                .build();
    }
// Lấy danh sách user/search theo keyword
    @PostMapping(value = "/search")
    public ApiResponse<PageResponse<UserResponse>> findAll(@ModelAttribute UserPageRequest request,
                                             @PageableDefault(size = 10 , sort = "createdAt" , direction = Sort.Direction.ASC)Pageable pageable){
        return ApiResponse.<PageResponse<UserResponse>>builder()
                .status(HttpStatus.OK.value())
                .result(userService.findALl(request , pageable))
                .message("find user successful")
                .build();
    }


    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<UserResponse> updateRole(@RequestBody RoleChangeRequest request , @PathVariable Long id){
        userService.changeRole(request.getRoleType() , id);
        return null ;
    }

}
