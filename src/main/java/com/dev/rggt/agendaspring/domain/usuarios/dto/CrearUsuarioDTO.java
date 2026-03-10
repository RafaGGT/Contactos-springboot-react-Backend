package com.dev.rggt.agendaspring.domain.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CrearUsuarioDTO(
    @NotBlank(message = "El nombre de usuario es obligatorio")
    String username,

    @NotBlank(message = "La contraseña es obligatoria")
    String password,

    @Email(message = "El correo electrónico no es válido")
    @NotBlank(message = "El correo electrónico es obligatorio")
    String correo,

    @NotBlank(message = "El nombre es obligatorio")
    String nombre
) {}
