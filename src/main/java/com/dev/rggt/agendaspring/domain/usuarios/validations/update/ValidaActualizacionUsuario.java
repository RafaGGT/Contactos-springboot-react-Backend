package com.dev.rggt.agendaspring.domain.usuarios.validations.update;

import com.dev.rggt.agendaspring.domain.usuarios.dto.ActualizarUsuarioDTO;
import com.dev.rggt.agendaspring.domain.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.validation.ValidationException;

@Component
public class ValidaActualizacionUsuario implements ValidarActualizarUsuario {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public void validate(ActualizarUsuarioDTO data) {
        if(data.correo() != null){
            var emailDuplicado = repository.findByCorreo(data.correo());
            if(emailDuplicado != null){
                throw new ValidationException("Este email ya esta en uso");
            }
        }
    }
}