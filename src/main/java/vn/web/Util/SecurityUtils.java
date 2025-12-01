package vn.web.Util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.web.Model.UserEntity;

public class SecurityUtils {
    public static UserEntity getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof  UserEntity){
            return (UserEntity) authentication.getPrincipal();
        }
        return null ;
    }

    public static long getCurrentId(){
        UserEntity user  = getCurrentUser();
        return user.getId();
    }
}
