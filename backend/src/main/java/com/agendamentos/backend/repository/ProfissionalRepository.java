
package com.agendamentos.backend.repository;

import com.agendamentos.backend.entity.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

    boolean existsByUsuarioId(Long usuarioId);

    Optional<Profissional> findByUsuarioId(Long usuarioId);

}
