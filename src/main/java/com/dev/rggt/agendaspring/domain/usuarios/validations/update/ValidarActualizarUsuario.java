package com.dev.rggt.agendaspring.domain.usuarios.validations.update;

import com.dev.rggt.agendaspring.domain.usuarios.dto.ActualizarUsuarioDTO;

public interface ValidarActualizarUsuario {
    void validate(ActualizarUsuarioDTO data);
}