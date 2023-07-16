package com.zam.dev.food_order.resolver;

import com.zam.dev.food_order.entity.Admin;
import com.zam.dev.food_order.entity.User;
import com.zam.dev.food_order.repository.UserRepository;
import com.zam.dev.food_order.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import
        org.springframework.http.HttpHeaders;
import org.springframework.web.server.ResponseStatusException;


@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        String token = servletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if(token == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED , "your token is not valid");
        }
        if(token.startsWith("Bearer")){
            token = token.substring(7);
            User user = userRepository.findByToken(token).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
            if (jwtService.isTokenValid(token , user)) {
                if(user.getToken() == null){
                    user.setToken(token);
                    userRepository.save(user);
                }
                return user;
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED , "your token is expired");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "your token not valid");
    }

}
