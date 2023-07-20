package com.zam.dev.food_order.controller.error;

import com.zam.dev.food_order.model.other.WebResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorHandler implements ErrorController {

    @GetMapping("/error")
    public WebResponse<Object> error(HttpServletRequest request){
        String message = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        int statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        return WebResponse.builder().status(statusCode).message(message).build();
    }

}
