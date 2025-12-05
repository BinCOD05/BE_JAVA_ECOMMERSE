package vn.web.Util;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import vn.web.Controller.Request.UserPageRequest;
import vn.web.Model.UserEntity;

import java.util.ArrayList;
import java.util.List;


public class UserSpecs {
    public static Specification<UserEntity> search(UserPageRequest request){
        return (root , query , cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.hasText(request.getKeyword())) {

                String key = "%" + request.getKeyword().trim().toLowerCase() + "%";
                Predicate hasName = cb.like(cb.lower(root.get("fullName")), key);
                Predicate hasEmail = cb.like(cb.lower(root.get("email")), key);
                predicates.add(cb.or(hasName, hasEmail));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
