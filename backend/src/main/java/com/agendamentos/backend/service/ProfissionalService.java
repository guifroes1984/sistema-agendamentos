
package com.agendamentos.backend.service;

import com.agendamentos.backend.dto.request.CriarProfissionalRequest;
import com.agendamentos.backend.dto.response.ProfissionalResponse;
import com.agendamentos.backend.entity.Profissional;
import com.agendamentos.backend.entity.Usuario;
import com.agendamentos.backend.exception.ProfissionalJaCadastradoException;
import com.agendamentos.backend.repository.ProfissionalRepository;
import com.agendamentos.backend.repository.UsuarioRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfissionalService {

    private final ProfissionalRepository profissionalRepository;
    private final UsuarioRepository usuarioRepository;

    public ProfissionalService(
            ProfissionalRepository profissionalRepository,
            UsuarioRepository usuarioRepository) {

        this.profissionalRepository = profissionalRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public ProfissionalResponse cadastrar(
            CriarProfissionalRequest request,
            Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new AccessDeniedException("Usuário não autorizado"));

        if (!Boolean.TRUE.equals(usuario.getAtivo())) {
            throw new AccessDeniedException("Usuário inativo");
        }

        if (profissionalRepository.existsByUsuarioId(usuarioId)) {
            throw new ProfissionalJaCadastradoException(
                    "O usuário já possui um cadastro profissional");
        }

        Profissional profissional = new Profissional();
        profissional.setUsuario(usuario);
        profissional.setNomeExibicao(request.nomeExibicao().trim());
        profissional.setAtivo(true);

        Profissional profissionalSalvo =
                profissionalRepository.saveAndFlush(profissional);

        return new ProfissionalResponse(
                profissionalSalvo.getId(),
                profissionalSalvo.getUsuario().getId(),
                profissionalSalvo.getNomeExibicao(),
                profissionalSalvo.getAtivo(),
                profissionalSalvo.getCriadoEm()
        );
    }
}
