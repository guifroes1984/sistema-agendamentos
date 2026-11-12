package com.agendamentos.backend.service;

import com.agendamentos.backend.dto.request.CriarProfissionalRequest;
import com.agendamentos.backend.dto.response.ProfissionalResponse;
import com.agendamentos.backend.entity.Profissional;
import com.agendamentos.backend.entity.Usuario;
import com.agendamentos.backend.exception.ProfissionalJaCadastradoException;
import com.agendamentos.backend.repository.ProfissionalRepository;
import com.agendamentos.backend.repository.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfissionalServiceTest {

    @Mock
    private ProfissionalRepository profissionalRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ProfissionalService profissionalService;

    private Usuario usuario;

    @BeforeEach
    void preparar() {
        usuario = new Usuario();
        usuario.setNome("Maria Silva");
        usuario.setAtivo(true);
    }

    @Test
    void deveCadastrarProfissionalQuandoUsuarioForValido() {

        Long usuarioId = 1L;

        CriarProfissionalRequest request = new CriarProfissionalRequest("Francilaine");

        when(usuarioRepository.findById(usuarioId))
                .thenReturn(Optional.of(usuario));

        when(profissionalRepository.existsByUsuarioId(usuarioId))
                .thenReturn(false);

        when(profissionalRepository.saveAndFlush(any(Profissional.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0, Profissional.class));

        ProfissionalResponse response = profissionalService.cadastrar(request, usuarioId);

        assertNotNull(response);
        assertEquals("Francilaine", response.nomeExibicao());
        assertTrue(response.ativo());

        verify(profissionalRepository, times(1))
                .saveAndFlush(any(Profissional.class));
    }

    @Test
    void naoDeveCadastrarProfissionalQuandoUsuarioJaPossuirCadastro() {

        Long usuarioId = 1L;

        CriarProfissionalRequest request = new CriarProfissionalRequest("Francilaine");

        when(usuarioRepository.findById(usuarioId))
                .thenReturn(Optional.of(usuario));

        when(profissionalRepository.existsByUsuarioId(usuarioId))
                .thenReturn(true);

        ProfissionalJaCadastradoException excecao = assertThrows(
                ProfissionalJaCadastradoException.class,
                () -> profissionalService.cadastrar(request, usuarioId));

        assertEquals(
                "O usuário já possui um cadastro profissional",
                excecao.getMessage());

        verify(profissionalRepository, never())
                .saveAndFlush(any(Profissional.class));
    }

    @Test
    void naoDeveCadastrarProfissionalQuandoUsuarioEstiverInativo() {

        Long usuarioId = 1L;

        usuario.setAtivo(false);

        CriarProfissionalRequest request = new CriarProfissionalRequest("Francilaine");

        when(usuarioRepository.findById(usuarioId))
                .thenReturn(Optional.of(usuario));

        AccessDeniedException excecao = assertThrows(
                AccessDeniedException.class,
                () -> profissionalService.cadastrar(request, usuarioId));

        assertEquals(
                "Usuário inativo",
                excecao.getMessage());

        verify(profissionalRepository, never())
                .existsByUsuarioId(any());

        verify(profissionalRepository, never())
                .saveAndFlush(any(Profissional.class));
    }

    @Test
    void naoDeveCadastrarProfissionalQuandoUsuarioNaoExistir() {

        Long usuarioId = 999L;

        CriarProfissionalRequest request = new CriarProfissionalRequest("Francilaine");

        when(usuarioRepository.findById(usuarioId))
                .thenReturn(Optional.empty());

        AccessDeniedException excecao = assertThrows(
                AccessDeniedException.class,
                () -> profissionalService.cadastrar(request, usuarioId));

        assertEquals(
                "Usuário não autorizado",
                excecao.getMessage());

        verify(profissionalRepository, never())
                .existsByUsuarioId(any());

        verify(profissionalRepository, never())
                .saveAndFlush(any(Profissional.class));
    }

}