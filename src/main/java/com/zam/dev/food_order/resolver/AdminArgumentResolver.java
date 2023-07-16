package com.zam.dev.food_order.resolver;

import com.zam.dev.food_order.entity.Admin;
import com.zam.dev.food_order.entity.User;
import com.zam.dev.food_order.repository.AdminRepository;
import com.zam.dev.food_order.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

@Component
@Slf4j
public class AdminArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AdminRepository amAdminRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Admin.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        String token = servletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if(token == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED , "fill your authorization");
        }
        log.info("token , {}" , token);
        if(token.startsWith("Bearer")){
            token = token.substring(7);
            Admin admin = amAdminRepository.findByToken(token).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
            if (jwtService.isTokenValid(token , admin)) {
                if(admin.getToken() == null){
                    admin.setToken(token);
                    amAdminRepository.save(admin);
                }
                return admin;
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED , "your token is expired");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "your token not valid");
    }
}
