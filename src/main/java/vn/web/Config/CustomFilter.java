package vn.web.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.web.Common.TokenType;
import vn.web.Services.CustomUserDetailsService;
import vn.web.Services.JwtService;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j
public class CustomFilter extends OncePerRequestFilter {

    private final JwtService jwtService ;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if(StringUtils.hasLength(authHeader) && authHeader.startsWith("Bearer") ){
            authHeader = authHeader.substring(7);
            String username = "";
            try {
                username = jwtService.extractUsername(authHeader , TokenType.ACCESS_TOKEN );
                System.out.println(username);
                log.info("token: {}" , authHeader.substring(0 , 20));
            }catch (Exception e){
                throw new  RuntimeException("error");
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails , null , userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            securityContext.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(securityContext);
            filterChain.doFilter(request , response);
            return ;
        }
        filterChain.doFilter(request, response);
    }


}

















//
//    String authHeader = request.getHeader("Authorization");
//        if(authHeader != null && authHeader.startsWith("Bearer")){
//        authHeader= authHeader.substring(7);
//        String username = "";
//
//        try{
//            username = jwtService.extractUsername(authHeader , TokenType.ACCESS_TOKEN );
//            System.out.println(username);
//        }
//        catch (Exception e){
//            throw new RuntimeException("invalid token");
//        }
//
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken( userDetails , null , userDetails.getAuthorities());
//        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        securityContext.setAuthentication(authentication);
//        SecurityContextHolder.setContext(securityContext);
//        filterChain.doFilter(request , response);
//        return;
//    }
//        filterChain.doFilter(request , response);
//}
