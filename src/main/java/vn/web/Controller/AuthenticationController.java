package vn.web.Controller;


import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.web.Controller.Request.SignInRequest;
import vn.web.Controller.Request.UserCreationRequest;
import vn.web.Controller.Response.ApiResponse;
import vn.web.Controller.Response.TokenResponse;
import vn.web.Controller.Response.UserResponse;
import vn.web.Services.AuthenticationService;
import vn.web.Services.UserService;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService ;
    private final UserService userService;
    @PostMapping(value = "/access-token")
    public TokenResponse getAccessToken(@RequestBody SignInRequest request) {
        return authenticationService.getAccessToken(request);
    }

    @PostMapping(value = "/register")
    public ApiResponse<UserResponse> registerUser(@RequestBody UserCreationRequest request){
        return ApiResponse.<UserResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("created user successful")
                .result(userService.save(request))
                .build();
    }
}
