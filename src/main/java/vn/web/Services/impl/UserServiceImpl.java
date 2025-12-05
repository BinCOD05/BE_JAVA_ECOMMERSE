package vn.web.Services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.web.Controller.Request.*;
import vn.web.Controller.Response.AddressResponse;
import vn.web.Controller.Response.UserPageResponse;
import vn.web.Controller.Response.UserResponse;
import vn.web.Converter.UserMapper;
import vn.web.Exception.ResourceNotFoundException;
import vn.web.Model.AddressEntity;
import vn.web.Model.UserEntity;
import vn.web.Repository.AddressRepository;
import vn.web.Repository.UserRepository;
import vn.web.Services.UserService;
import vn.web.Util.UserSpecs;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl  implements UserService {

    private final UserRepository userRepository ;
    private final AddressRepository addressRepository;
//    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper ;



    @Override
    @PreAuthorize("authentication.principal.id == #id")
    public UserResponse getUser(long id) {
        UserEntity user = getUserEntity(id);
//        return modelMapper.map(user , UserResponse.class);
        return userMapper.toDTOResponse(user) ;
    }

    @Override
    public UserPageResponse findALl(UserPageRequest req , Pageable pageable) {
        Specification<UserEntity> specs = UserSpecs.search(req);

        Page<UserEntity> userPage  = userRepository.findAll(specs , pageable);

        List<UserResponse> data = userPage.stream().map(user -> userMapper.toDTOResponse(user)).toList();

        return UserPageResponse.builder()
                .page(pageable.getPageNumber())
                .totalElement(userPage.getTotalElements())
                .size(userPage.getSize())
                .totalPage(userPage.getTotalPages())
                .content(data)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserResponse save(UserCreationRequest req) {
//        UserEntity user = modelMapper.map(req , UserEntity.class) ;
        UserEntity user = userMapper.toEntity(req);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        return userMapper.toDTOResponse(userRepository.save(user)) ;
//        return  modelMapper.map(user , UserResponse.class);
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserResponse update(UserUpdateRequest req , long id) {
        UserEntity user = getUserEntity(id);
//        updateUser hàm của interface UserMapper <-> MapperStruct
        userMapper.updateUser(user , req );
        return userMapper.toDTOResponse(userRepository.save(user));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(long id) {
        UserEntity user = getUserEntity(id);
        userRepository.delete(user);
    }

    @Override
    public void changePassword(UserChangePasswdRequest req , long id) {
        UserEntity user = getUserEntity(id);
        if(!passwordEncoder.matches(req.getPassword() , user.getPassword()) || !req.getNewPassword().equals(req.getConfirmPassword())){
            throw new RuntimeException();
        }
        user.setPassword(passwordEncoder.encode(req.getConfirmPassword()));
        userRepository.save(user);
    }



    private UserEntity getUserEntity(long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

   
    private UserEntity findByName(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User Not Found"));
    }

}
