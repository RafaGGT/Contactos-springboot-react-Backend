package com.dev.rggt.agendaspring.domain.contactos.repository;

import com.dev.rggt.agendaspring.domain.contactos.Contacto;
import com.dev.rggt.agendaspring.domain.usuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContactoRepository extends JpaRepository<Contacto, Long> {
    List<Contacto> findByUsuario(Usuario usuario);
    Contacto findByIdAndUsuario(Long id, Usuario usuario);
    Optional<Contacto> findByIdAndUsuarioId(Long id, Long usuarioId);

}
