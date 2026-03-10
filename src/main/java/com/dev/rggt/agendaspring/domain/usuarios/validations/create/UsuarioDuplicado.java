package com.dev.rggt.agendaspring.domain.usuarios.validations.create;

import com.dev.rggt.agendaspring.domain.usuarios.dto.CrearUsuarioDTO;
import com.dev.rggt.agendaspring.domain.usuarios.repository.UsuarioRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioDuplicado implements ValidarCrearUsuario{

    @Autowired
    private UsuarioRepository repository;

    @Override
    public void validate(CrearUsuarioDTO data) {
        var usuarioDuplicado = repository.findByUsername(data.username());
        if(usuarioDuplicado != null){
            throw new ValidationException("Este usuario ya existe.");
        }

        var emailDuplicado = repository.findByCorreo(data.correo());
        if(emailDuplicado != null){
            throw new ValidationException("Este email ya existe.");
        }
    }
}