package com.agendamentos.backend.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final Long expiracao;

    public TokenService(
            JwtEncoder jwtEncoder,
            @Value("${jwt.expiration}") Long expiracao) {

        this.jwtEncoder = jwtEncoder;
        this.expiracao = expiracao;
    }

    public String gerarToken(Long usuarioId, String email) {

        Instant agora = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("sistema-agendamentos")
                .issuedAt(agora)
                .expiresAt(agora.plus(expiracao, ChronoUnit.SECONDS))
                .subject(usuarioId.toString())
                .claim("email", email)
                .build();

        JwsHeader header = JwsHeader
                .with(MacAlgorithm.HS256)
                .build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims)
        ).getTokenValue();
    }
}
