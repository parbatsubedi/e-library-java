package com.aadim.elibrary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //create a bean for
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception
    {
        return httpSecurity.authorizeHttpRequests( auth -> auth.requestMatchers("/").permitAll()
                .requestMatchers("/index").permitAll().anyRequest().authenticated()
        ).formLogin(form->form
                .defaultSuccessUrl("/",true)
        ).logout(config->config.logoutSuccessUrl("/")).build();
    }
}
