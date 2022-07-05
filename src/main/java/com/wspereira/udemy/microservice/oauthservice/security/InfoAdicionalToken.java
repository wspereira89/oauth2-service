/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wspereira.udemy.microservice.oauthservice.security;

import com.wspereira.udemy.microservice.oauthservice.model.dto.UserDe;
import com.wspereira.udemy.microservice.oauthservice.service.IUserService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

/**
 *
 * @author stive
 */
@Component
public class InfoAdicionalToken implements TokenEnhancer {

    @Autowired
    private IUserService userService;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accesToken, OAuth2Authentication authenticatio) {
        Map<String, Object> info = new HashMap();
        UserDe user = userService.findByUsername(authenticatio.getName());
        info.put("name", user.getName());
        info.put("email", user.getEmail());
        ((DefaultOAuth2AccessToken) accesToken).setAdditionalInformation(info);
        return accesToken;
    }

}
