package com.agendamentos.backend.service;

import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.agendamentos.backend.dto.request.CriarUsuarioRequest;
import com.agendamentos.backend.dto.response.UsuarioResponse;
import com.agendamentos.backend.entity.Usuario;
import com.agendamentos.backend.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponse cadastrar(CriarUsuarioRequest request) {

        String email = request.email().trim().toLowerCase(Locale.ROOT);

        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException(
                    "Este e-mail já está cadastrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome().trim());
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(request.senha()));

        Usuario usuarioSalvo = usuarioRepository.saveAndFlush(usuario);

        return new UsuarioResponse(
                usuarioSalvo.getId(),
                usuarioSalvo.getNome(),
                usuarioSalvo.getEmail(),
                usuarioSalvo.getAtivo(),
                usuarioSalvo.getCriadoEm());

    }

}