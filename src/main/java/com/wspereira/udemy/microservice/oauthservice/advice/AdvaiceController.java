/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wspereira.udemy.microservice.oauthservice.advice;

import feign.FeignException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 *
 * @author stive
 */
@ControllerAdvice
public class AdvaiceController extends ResponseEntityExceptionHandler{
    
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Object> handleFeignException(
            FeignException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(
            UsernameNotFoundException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(UserException.class)
    public ResponseEntity<Object> handleUserException(
            UserException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(InvalidGrantException.class)
    public ResponseEntity<Object> handleInvalidGrantException(
            InvalidGrantException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object>  handleAuthenticationException(AuthenticationException ex, HttpServletResponse response){
         Map<String, Object> body = new LinkedHashMap<>();
        
        body.put("message", ex.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
         return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }
}
