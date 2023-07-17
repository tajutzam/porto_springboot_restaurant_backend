package com.zam.dev.food_order.service.impl;

import com.zam.dev.food_order.entity.User;
import com.zam.dev.food_order.model.*;
import com.zam.dev.food_order.properties.FileProperties;
import com.zam.dev.food_order.repository.UserRepository;
import com.zam.dev.food_order.security.Bcrypt;
import com.zam.dev.food_order.service.FileUploadService;
import com.zam.dev.food_order.service.JwtService;
import com.zam.dev.food_order.service.UserService;
import com.zam.dev.food_order.service.ValidationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {


    private UserRepository userRepository;


    private ValidationService validationService;


    private JwtService jwtService;


    private Bcrypt bcrypt;

    private FileProperties fileProperties;


    private FileUploadService fileUploadService;



    private UserResponse castToUserResponse(User user) {
        return UserResponse.builder().username(user.getUsername()).lastName(user.getLastName()).address(user.getAddress()).id(user.getId()).email(user.getEmail()).phone_number(user
                .getPhoneNumber()).build();
    }

    @Override
    public PageImpl<UserResponse> findAll(int page, int size) {
        Sort sort = Sort.by(Sort.Order.desc("firstName"));
        Pageable pageable = PageRequest.of(page, size , sort);
        Page<User> pageResponse = userRepository.findAll(pageable);
        List<UserResponse> responseList = pageResponse.getContent().stream().map(this::castToUserResponse).toList();
        return new PageImpl<>(responseList, pageable , pageResponse.getTotalElements());
    }

    @Override
    @Transactional
    public TokenResponse login(LoginRequest loginRequest) {
        validationService.validate(loginRequest);
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "your username or password wrong"));
        if (bcrypt.checkPw(loginRequest.getPassword(), user.getPassword())) {
            String token = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            user.setToken(token);
            user.setRefreshToken(refreshToken);
            userRepository.save(user);
            return TokenResponse.builder()
                    .token(token)
                    .refreshToken(refreshToken)
                    .build();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED ,"your username or password wrong");
    }

    @Override
    public TokenResponse createNewToken(RefreshTokenRequest request) {
        validationService.validate(request);
        User user = userRepository.findByRefreshToken(request.getRefreshToken()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "your token is wrong"));
        if (jwtService.isTokenValid(request.getRefreshToken() , user)) {
            String token = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            return TokenResponse.builder().token(token).refreshToken(refreshToken).build();
        }
       throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "your token is not valid");
    }


    @Override
    @Transactional
    public TokenResponse register(UserRegisterRequest request) {
        validationService.validate(request);

        boolean present = userRepository.findByUsernameOrEmail(request.getUsername() , request.getEmail()).isPresent();
        if(present){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "username or email already been taken");
        }
        User user = new User();
        log.info(request.getEmail());
        user.setId(UUID.randomUUID().toString());
        user.setUsername(request.getUsername());
        user.setPassword(bcrypt.hashPw(request.getPassword()));
        user.setAddress(request.getAddress());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhone_number());
        user.setEmail(request.getEmail());
        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        user.setToken(token);
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
        return TokenResponse.builder().token(token).refreshToken(refreshToken).build();
    }

    @Override
    public UserResponse updateAvatar(MultipartFile multipartFile, User user) {
        if(multipartFile.isEmpty()|| multipartFile.getSize() == 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "please pick a image");
        }
        String userAvatarName = fileUploadService.upload(multipartFile, "user", fileProperties.getUser());
        user.setAvatar(userAvatarName);
        userRepository.save(user);
        return castToUserResponse(user);
    }

    @Override
    public UserResponse getUser(User user) {
        return castToUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateUser(UserUpdateRequest request, User user) {
        validationService.validate(request);
        List<User> userList = userRepository.findAllByIdNot(user.getId());
        userList.forEach(userResponse ->{
            if (userResponse.getUsername().equals(request.getUsername())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "username already been taken");
            }
        });
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setAddress(request.getAddress());
        user.setPassword(bcrypt.hashPw(request.getPassword()));
        user.setPhoneNumber(request.getPhone_number());
        user.setEmail(request.getEmail());
        User responseUser = userRepository.save(user);
        return castToUserResponse(responseUser);
    }
}
