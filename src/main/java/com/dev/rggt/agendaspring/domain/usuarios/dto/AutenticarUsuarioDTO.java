package com.dev.rggt.agendaspring.domain.usuarios.dto;

import jakarta.validation.constraints.NotNull;

public record AutenticarUsuarioDTO(
        @NotNull(message = "El nombre de usuario es obligatorio")
        String username,
        @NotNull(message = "La contraseña es obligatoria")
        String password
) {
}
