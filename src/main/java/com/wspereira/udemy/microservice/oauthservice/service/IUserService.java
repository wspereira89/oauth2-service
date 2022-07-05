/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wspereira.udemy.microservice.oauthservice.service;

import com.wspereira.udemy.microservice.oauthservice.model.dto.UserDe;

/**
 *
 * @author stive
 */
public interface IUserService {
     public UserDe findByUsername( String username);

   
    public UserDe updateReintento( Long id, int intento);

    public UserDe update( UserDe usuario,  Long id);
}
