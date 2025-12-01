package vn.web.Services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import vn.web.Controller.Request.SignInRequest;
import vn.web.Controller.Response.TokenResponse;
import vn.web.Repository.UserRepository;
import vn.web.Services.AuthenticationService;
import vn.web.Services.JwtService;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager ;

    @Override
    public TokenResponse getAccessToken(SignInRequest req) {

        List<String> authorities = new ArrayList<>();

        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername() , req.getPassword()));
            log.info("isAuthenticated = {}" , authentication.isAuthenticated());
            log.info("Authorities = {}" , authentication.getAuthorities());
            authorities.add(authentication.getAuthorities().toString());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

        var user = userRepository.findByUsername(req.getUsername());
        if(user == null){
            throw new RuntimeException("user not found");
        }




        String accessToken = jwtService.generateAccessToken(user.get().getId() , req.getUsername(), authorities );
        String refreshToken = jwtService.generateAccessToken(user.get().getId() , req.getUsername(), authorities );
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public TokenResponse getRefreshToken(String refreshToken) {
        return null;
    }
}
