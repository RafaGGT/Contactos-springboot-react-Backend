package com.dev.rggt.agendaspring.domain.usuarios.dto;

import com.dev.rggt.agendaspring.domain.usuarios.Usuario;

public record DetallesUsuarioDTO(Long id,
                                 String username,
                                 String nombre,
                                 String correo) {
    public DetallesUsuarioDTO(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getNombre(),
                usuario.getCorreo()
        );
    }
}
