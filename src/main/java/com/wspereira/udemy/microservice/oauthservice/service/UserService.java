/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wspereira.udemy.microservice.oauthservice.service;

import com.wspereira.udemy.microservice.oauthservice.clients.UsuarioFeignClient;
import com.wspereira.udemy.microservice.oauthservice.model.dto.UserDe;
import feign.FeignException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author stive
 */
@Service
@Slf4j
public class UserService implements IUserService, UserDetailsService {

    @Autowired
    private UsuarioFeignClient client;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserDe usuario = client.findByUsername(username);
             
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String password = request.getParameter("password"); // get from request parameter
            if (!bCryptPasswordEncoder.matches(password, usuario.getPassword())) {

                log.debug("Usuario o Password incorrecto para {}", username, password);

                throw new UsernameNotFoundException("Error en el login, No existe el usuario '" + username + "'en el sistema o la contraseña es incorrecta");
            }
            if(!usuario.getEnabled()){ 
                log.debug("Usuario o Password incorrecto para {}", username, password);

                throw new UsernameNotFoundException("Error en el login, el usuario '" + username + "' se encuentra deshabilitado");
            }
            List<GrantedAuthority> authorities = Arrays.asList("USER")
                    .stream()
                    .map(role -> new SimpleGrantedAuthority(role))
                    .collect(Collectors.toList());
            log.debug("usuario authenticado {}{}", username, password);
            return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
        } catch (FeignException e) {
            log.error("Error en el login, No existe el usuario {}", username);
            throw new UsernameNotFoundException("Error en el login, No existe el usuario '" + username + "'en el sistema");

        }
    }

    @Override
    public UserDe findByUsername(String username) {
        return client.findByUsername(username);
    }

    @Override
    public UserDe update(UserDe usuario, Long id) {
        return client.update(usuario, id);
    }

    @Override
    public UserDe updateReintento(Long id, int intento) {
        return client.updateReintento(id, intento);
    }

}
