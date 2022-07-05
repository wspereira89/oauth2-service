/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wspereira.udemy.microservice.oauthservice;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author stive
 */
@Configuration
public class ForexClientConfig {

     @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
    
    
}
