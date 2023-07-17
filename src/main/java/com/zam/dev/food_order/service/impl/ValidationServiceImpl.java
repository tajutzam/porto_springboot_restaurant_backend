package com.zam.dev.food_order.service.impl;

import com.zam.dev.food_order.service.ValidationService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class ValidationServiceImpl implements ValidationService {
    private Validator validator;

    @Override
    public void validate(Object object) {

        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);
        if(constraintViolations.size() != 0){
            throw new ConstraintViolationException(constraintViolations);
        }

    }
}
