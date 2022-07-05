/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wspereira.udemy.microservice.oauthservice.security.event;

import com.wspereira.udemy.microservice.oauthservice.model.dto.UserDe;
import com.wspereira.udemy.microservice.oauthservice.service.IUserService;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

/**
 *
 * @author stive
 */
@Component
@Slf4j
public class AuthenticationSuccesErrorHandler implements AuthenticationEventPublisher {

    @Autowired
    private IUserService userService;

    @Override
    public void publishAuthenticationSuccess(Authentication authentication) {

        if (authentication.getDetails() instanceof WebAuthenticationDetails) {
            return;
        }
        UserDetails user = (UserDetails) authentication.getPrincipal();
        log.debug("Usuario logeado {}", user);

        UserDe usuario = userService.findByUsername(user.getUsername());
        if (usuario.getIntentos() != null && usuario.getIntentos() > 0) {
            userService.updateReintento(usuario.getId(), 0);
        }

    }

    @Override
    public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
        log.error("Usuario logeado {} error{}", exception.getMessage());
        Object principal =  authentication.getPrincipal();
        String username="";
        try {
              if (principal instanceof String) {username=(String)principal;}
              else{
                UserDetails  user=(UserDetails)authentication.getPrincipal();
                  username=user.getUsername();
              }
              
            UserDe findByUsername = userService.findByUsername(username);
            log.debug("Usuario feinf {}", findByUsername);
            if (findByUsername.getIntentos() == null) {
                findByUsername.setIntentos(0);
            }
            findByUsername.setIntentos(findByUsername.getIntentos() + 1);

            if (findByUsername.getIntentos() >= 3) {
                log.error("Usuario {} deshabilitado por maximo  reintento", findByUsername.getUsername());
                findByUsername.setEnabled(false);
            }
            userService.update(findByUsername, findByUsername.getId());
        } catch (FeignException e) {
            log.error("Usuario No existe {}", exception.getMessage());

        }

    }

}
