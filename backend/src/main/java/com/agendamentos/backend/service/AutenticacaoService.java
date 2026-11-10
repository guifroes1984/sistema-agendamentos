package com.agendamentos.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agendamentos.backend.dto.request.LoginRequest;
import com.agendamentos.backend.dto.response.LoginResponse;
import com.agendamentos.backend.entity.Usuario;
import com.agendamentos.backend.exception.CredenciaisInvalidasException;
import com.agendamentos.backend.repository.UsuarioRepository;

@Service 
public class AutenticacaoService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final Long expiracao;

    public AutenticacaoService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            TokenService tokenService,
            @Value ("${jwt.expiration}") Long expiracao) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.expiracao = expiracao;
    }

    @Transactional (readOnly = true)
    public LoginResponse autenticar(LoginRequest request) {

        Usuario usuario = usuarioRepository
                .findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(() ->
                        new CredenciaisInvalidasException()
                );

        if (!Boolean.TRUE.equals(usuario.getAtivo())
                || !passwordEncoder.matches(
                        request.senha(),
                        usuario.getSenha())) {

            throw new CredenciaisInvalidasException();
        }

        String token = tokenService.gerarToken(
                usuario.getId(),
                usuario.getEmail()
        );

        return new LoginResponse(token, "Bearer", expiracao);
    }
}
