package com.agendamentos.backend.config;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

@Configuration
@EnableWebSecurity
public class ConfiguracaoSeguranca {

        @Bean
        public SecurityFilterChain configurarSeguranca(
                        HttpSecurity http) throws Exception {

                http
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(autorizacao -> autorizacao
                                                .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                                                .requestMatchers(
                                                                "/swagger-ui/**",
                                                                "/swagger-ui.html",
                                                                "/v3/api-docs/**",
                                                                "/v3/api-docs")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
                                }));

                return http.build();
        }

        @Bean
        public SecretKey chaveJwt(
                        @Value("${jwt.secret}") String segredo) {

                byte[] chaveDecodificada = Base64.getDecoder().decode(segredo);

                if (chaveDecodificada.length < 32) {
                        throw new IllegalArgumentException(
                                        "A chave JWT deve possuir pelo menos 32 bytes.");
                }

                return new SecretKeySpec(chaveDecodificada, "HmacSHA256");
        }

        @Bean
        public JwtEncoder jwtEncoder(SecretKey chaveJwt) {
                return new NimbusJwtEncoder(
                                new ImmutableSecret<>(chaveJwt));
        }

        @Bean
        public JwtDecoder jwtDecoder(SecretKey chaveJwt) {
                return NimbusJwtDecoder
                                .withSecretKey(chaveJwt)
                                .macAlgorithm(MacAlgorithm.HS256)
                                .build();
        }

}
