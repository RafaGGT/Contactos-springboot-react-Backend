package com.dev.rggt.agendaspring.controller;

import com.dev.rggt.agendaspring.domain.contactos.dto.CrearContactoDTO;
import com.dev.rggt.agendaspring.domain.contactos.dto.DetallesContactoDTO;
import com.dev.rggt.agendaspring.domain.contactos.dto.EliminarContactoDTO;
import com.dev.rggt.agendaspring.domain.contactos.dto.ModificarContactoDTO;
import com.dev.rggt.agendaspring.domain.contactos.repository.ContactoRepository;
import com.dev.rggt.agendaspring.domain.usuarios.Usuario;
import com.dev.rggt.agendaspring.domain.usuarios.repository.UsuarioRepository;
import com.dev.rggt.agendaspring.domain.contactos.Contacto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/listas-contactos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Contactos", description = "Vinculado a los contactos de la agenda.")
public class ContactosController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContactoRepository contactoRepository;

    @PostMapping("/crear-contacto")
    @Transactional
    public ResponseEntity<?> crearContacto(
            @RequestBody @Valid CrearContactoDTO dto,
            @AuthenticationPrincipal Usuario usuario,
            UriComponentsBuilder uriBuilder) {

        Contacto contacto = new Contacto(dto, usuario);
        contactoRepository.save(contacto);

        var uri = uriBuilder
                .path("/contactos/{id}")
                .buildAndExpand(contacto.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }


    @GetMapping("/ver-contactos")
    @Operation(summary = "Enseña contactos del usuario autenticado y sus respectivas redes")
    public ResponseEntity<List<DetallesContactoDTO>> verContactos(
            @AuthenticationPrincipal UserDetails userDetails) {

        UserDetails user = usuarioRepository
                .findByUsername(userDetails.getUsername());

        // convertir UserDetails → Usuario
        Usuario usuario = (Usuario) user;
        //
        List<DetallesContactoDTO> contactos = contactoRepository
                // busca los contactos del usuario autenticado
                .findByUsuario(usuario)
                .stream()
                .map(DetallesContactoDTO::new)
                .toList();

        return ResponseEntity.ok(contactos);
    }

    @PutMapping("/modificar-contacto")
    @Transactional
    @Operation(summary = "Modifica un contacto del usuario autenticado")
    public ResponseEntity<?> modificarContacto(
            @RequestBody @Valid ModificarContactoDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        Usuario usuario = (Usuario) usuarioRepository
                .findByUsername(userDetails.getUsername());

        Contacto contacto = contactoRepository
                .findByIdAndUsuarioId(dto.id(), usuario.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Contacto no encontrado"));

        contacto.actualizarContacto(dto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/eliminar-contacto")
    @Transactional
    @Operation(summary = "Elimina un contacto del usuario autenticado")
    public ResponseEntity<?> eliminarContacto(@RequestBody @Valid EliminarContactoDTO dto,
                                             @AuthenticationPrincipal UserDetails userDetails) {

        Usuario usuario = (Usuario) usuarioRepository
                .findByUsername(userDetails.getUsername());

        Contacto contacto = contactoRepository
                .findByIdAndUsuarioId(dto.id(), usuario.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Contacto no encontrado"));

        contactoRepository.delete(contacto);

        return ResponseEntity.noContent().build();
    }

}
