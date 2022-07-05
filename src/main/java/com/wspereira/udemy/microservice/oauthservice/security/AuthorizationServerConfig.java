/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wspereira.udemy.microservice.oauthservice.security;

import java.util.Arrays;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 *
 * @author stive
 */
@RefreshScope
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${config.security.oauth.client.id}")
    private String client_id;
    @Value("${config.security.oauth.client.secret}")
    private String client_secret;
    @Value("${config.security.oauth.jwt.key}")
    private String jwt_key;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private InfoAdicionalToken adicionalToken;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain token = new TokenEnhancerChain();
        token.setTokenEnhancers(Arrays.asList(adicionalToken, accessTokenConverter()));
        endpoints.
                authenticationManager(authenticationManager).
                tokenStore(tokenStore()).
                accessTokenConverter(accessTokenConverter()).
                tokenEnhancer(token);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        clients.inMemory().
                withClient(client_id).
                secret(bCryptPasswordEncoder.encode(client_secret)).
                scopes("read", "write").
                authorizedGrantTypes("password", "refresh_token").
                accessTokenValiditySeconds(3600).
                refreshTokenValiditySeconds(3600);
        /*Para varios clientes
                    and().
                withClient("frontendapp").
                secret(bCryptPasswordEncoder.encode("12345")).
                scopes("read","write").
                authorizedGrantTypes("password","refresh_token").
                accessTokenValiditySeconds(3600).
                refreshTokenValiditySeconds(3600)*/;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.
                tokenKeyAccess("permitAll()").
                checkTokenAccess("isAuthenticated()");
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();

        tokenConverter.setSigningKey(Base64.encodeBase64String(jwt_key.getBytes()));
        return tokenConverter;
    }

    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

}
