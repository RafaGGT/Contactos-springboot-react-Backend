package com.dev.rggt.agendaspring.domain.redes.repository;

import com.dev.rggt.agendaspring.domain.redes.Red;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RedRepository extends JpaRepository<Red, Long> {
    Red findByIdAndContactoId(Long id, Long contactoId);
}
