/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wspereira.udemy.microservice.oauthservice.clients;

import com.wspereira.udemy.microservice.oauthservice.ForexClientConfig;
import com.wspereira.udemy.microservice.oauthservice.model.dto.UserDe;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author stive
 */
@FeignClient(name = "user-service", configuration = ForexClientConfig.class)
public interface UsuarioFeignClient {

    @GetMapping("/user/{username}")
    public UserDe findByUsername(@PathVariable(name = "username") String username);

    @PutMapping("/user/{id}/reintento/{intento}")
    public UserDe updateReintento(@PathVariable(name = "id") Long id, @PathVariable(name = "intento") int intento);

    @PutMapping("/user/{id}")
    public UserDe update(@RequestBody UserDe usuario, @PathVariable(name = "id") Long id);

   
}
