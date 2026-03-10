package com.dev.rggt.agendaspring.controller;

import com.dev.rggt.agendaspring.domain.usuarios.Usuario;
import com.dev.rggt.agendaspring.domain.usuarios.dto.ActualizarUsuarioDTO;
import com.dev.rggt.agendaspring.domain.usuarios.dto.CrearUsuarioDTO;
import com.dev.rggt.agendaspring.domain.usuarios.dto.DetallesUsuarioDTO;
import com.dev.rggt.agendaspring.domain.usuarios.dto.EliminarUsuarioDTO;
import com.dev.rggt.agendaspring.domain.usuarios.repository.UsuarioRepository;
import com.dev.rggt.agendaspring.domain.usuarios.validations.create.ValidarCrearUsuario;
import com.dev.rggt.agendaspring.domain.usuarios.validations.update.ValidarActualizarUsuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Usuario", description = "Registro, datos, actualización y eliminación de usuarios.")
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    List<ValidarCrearUsuario> crearValidador;

    @Autowired
    List<ValidarActualizarUsuario> actualizarValidador;

    public UsuarioController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @PostMapping("/registro")
    @Operation(summary = "Registra un nuevo usuario")
    public ResponseEntity registrar(@RequestBody @Valid CrearUsuarioDTO datos, UriComponentsBuilder uriComponentsBuilder) {

        crearValidador.forEach(v -> v.validate(datos));
        // Codificar la contraseña antes de guardar el usuario
        var contrasenaCodificada = passwordEncoder.encode(datos.password());
        // Crear una nueva instancia de Usuario con los datos proporcionados
        var usuario = new Usuario(datos, contrasenaCodificada);
        usuarioRepository.save(usuario);
        // Construir la URI para el nuevo recurso creado
        var uri = uriComponentsBuilder.path("/registro/{id}")
                .buildAndExpand(usuario.getId())
                .toUri();
        // Devolver una respuesta con el estado 201 Created y los detalles del usuario
        return ResponseEntity.created(uri).body(new DetallesUsuarioDTO(usuario));
    }

    @GetMapping("/usuario/{username}")
    @Operation(summary = "Muestra los datos del usuario designado")
    public ResponseEntity<DetallesUsuarioDTO> obtenerUsuario(@PathVariable String username){
        Usuario usuario = (Usuario) usuarioRepository.findByUsername(username);
        var datosUsuario = new DetallesUsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getNombre(),
                usuario.getCorreo()
        );
        return ResponseEntity.ok(datosUsuario);
    }

    @PutMapping("/modificar/{username}")
    @Transactional
    @Operation(summary = "Actualiza los datos del usuario designado")
    public ResponseEntity<DetallesUsuarioDTO> actualizarUsuario(@RequestBody @Valid ActualizarUsuarioDTO actualizarUsuarioDTO, @PathVariable String username){
        Usuario usuario = (Usuario) usuarioRepository.findByUsername(username);

        usuario.actualizarUsuario(actualizarUsuarioDTO);
        var datosUsuario = new DetallesUsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getNombre(),
                usuario.getCorreo()
        );
        return ResponseEntity.ok(datosUsuario);
    }

    @DeleteMapping("/eliminar/{username}")
    @Transactional
    @Operation(summary = "Elimina el usuario designado")
    public ResponseEntity eliminarUsuario(@RequestBody @Valid EliminarUsuarioDTO eliminarUsuarioDTO, @PathVariable String username){
        Usuario usuario = (Usuario) usuarioRepository.findByUsername(username);

        if (passwordEncoder.matches(eliminarUsuarioDTO.password(), usuario.getPassword())) {
            usuarioRepository.delete(usuario);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(403).build(); // Forbidden
        }
    }
}
