package com.issue.manager.configurations;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log4j2
@Configuration
public class CorsConfig {

    @Autowired
    private Environment environment;

    @Bean
    CorsConfigurationSource corsConfigurationSource() throws UnknownHostException {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();

        String allowedOriginsVariable = environment.getProperty("cors.allowed-origins");
        List<String> allowedOriginList = Arrays.asList(allowedOriginsVariable != null ? allowedOriginsVariable.split(",") : new String[] { });

        String hostName = InetAddress.getLoopbackAddress().getHostName();

        List<String> allowedOrigins = new ArrayList<>(List.of(String.format("http://%s:4200", hostName)));
        allowedOrigins.addAll(allowedOriginList);
        log.info(String.format("Setting Allowed Origins: %s", String.join(", ", allowedOrigins)));

        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
