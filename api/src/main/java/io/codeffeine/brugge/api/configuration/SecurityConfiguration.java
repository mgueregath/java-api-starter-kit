/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.codeffeine.brugge.api.configuration;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
/**
 *
 * @author mguer
 */
@Configuration
public class SecurityConfiguration {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.cors(corsCustomizer->corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration corsConfiguration=new CorsConfiguration();
                        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
                      // providing the allowed origin details, can provide multiple origins here, 7070 is the port number of client application here
                        corsConfiguration.setAllowedMethods(Arrays.asList("POST", "PUT", "GET", "DELETE", "OPTIONS"));// allowing all HTTP methods GET,POST,PUT etc, can configure on your need
                        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));// allowing all the request headers, can configure according to your need, which headers to allow
                        corsConfiguration.setMaxAge(Duration.ofMinutes(5L)); // setting the max time till which the allowed origin will not make a pre-flight request again to check if the CORS is allowed on not
                        return corsConfiguration;
                    }
                }));
        httpSecurity.authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest()
            .permitAll())
            .csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();

    }
}
