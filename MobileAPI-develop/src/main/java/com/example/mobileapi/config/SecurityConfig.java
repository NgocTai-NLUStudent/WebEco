package com.example.mobileapi.config;

import com.example.mobileapi.util.JwtUtil;
import com.nimbusds.jose.JWSAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.security.interfaces.RSAPublicKey;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SecurityConfig {
    JwtUtil jwtUtil;

    String[] acceptedEndpoint = {
            "/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs", "/v3/api-docs/**",
            "/swagger-resources", "/swagger-resources/**",
            "/configuration/ui", "/configuration/security",
            "/swagger-ui/**", "/webjars/**", "/swagger-ui.html",
            "/api/auth/login", "/api/customer/introspect", "/api/customer", "/api/test/**",
            "/authenticate"};

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(
                        request ->
                                request.requestMatchers(acceptedEndpoint)
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated());
        httpSecurity.oauth2ResourceServer(
                oauth2 ->
                        oauth2.jwt(
                                        jwtConfigurer ->
                                                jwtConfigurer.decoder(jwtDecoder())
                                                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                                .authenticationEntryPoint(new SecurityExceptionHandler()));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jGA = new JwtGrantedAuthoritiesConverter();
        jGA.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter jAC = new JwtAuthenticationConverter();
        jAC.setJwtGrantedAuthoritiesConverter(jGA);


        return jAC;
    }

    //decode token de kiem tra hop le hay khong
    @Bean
    public JwtDecoder jwtDecoder() {
        String jwtError = "jwtError";
        RSAPublicKey publicKey = jwtUtil.loadPublicKey();

        // Cấu hình JWT Processor với thuật toán RS512
        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        jwtProcessor.setJWSKeySelector((header, context) -> {
            if (!JWSAlgorithm.RS512.equals(header.getAlgorithm())) {
                throw new RuntimeException("Invalid algorithm: " + header.getAlgorithm());
            }
            return List.of(publicKey);
        });

        NimbusJwtDecoder decoder = new NimbusJwtDecoder(jwtProcessor);

        // Trả về một JwtDecoder wrapper để bắt lỗi khi decode token
        return token -> {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            if (jwtUtil.isLogout(token)) {
                request.setAttribute(jwtError, "LOGOUT");
            }
            try {
                return decoder.decode(token);
            } catch (JwtException e) {


                String errorMessage = e.getMessage().toLowerCase();
                if (errorMessage.contains("expired")) {
                    request.setAttribute(jwtError, "EXPIRED");
                } else if (errorMessage.contains("signature")) {
                    request.setAttribute(jwtError, "INVALID_SIGNATURE");
                } else if (errorMessage.contains("unsupported")) {
                    request.setAttribute(jwtError, "UNSUPPORTED");
                } else {
                    request.setAttribute(jwtError, "INVALID");
                }


                // Ném lại exception để ngăn không cho request tiếp tục xử lý
                throw e;
            }
        };
    }


}
