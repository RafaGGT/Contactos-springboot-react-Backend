package com.dev.rggt.agendaspring.controller;

import com.dev.rggt.agendaspring.domain.contactos.Contacto;
import com.dev.rggt.agendaspring.domain.contactos.dto.ModificarContactoDTO;
import com.dev.rggt.agendaspring.domain.contactos.repository.ContactoRepository;
import com.dev.rggt.agendaspring.domain.redes.dto.CrearRedDTO;
import com.dev.rggt.agendaspring.domain.redes.dto.EliminarRedDTO;
import com.dev.rggt.agendaspring.domain.redes.dto.ModificarRedDTO;
import com.dev.rggt.agendaspring.domain.redes.repository.RedRepository;
import com.dev.rggt.agendaspring.domain.usuarios.Usuario;
import com.dev.rggt.agendaspring.domain.usuarios.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.dev.rggt.agendaspring.domain.redes.Red;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/listas-contactos/red")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Redes", description = "Está vinculado a las redes de los contactos.")
public class RedController {

    @Autowired
    private RedRepository redRepository;
    @Autowired
    private ContactoRepository contactoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    @PostMapping("/añadir-red")
    @Operation(summary = "Añade una nueva red a un contacto existente")
    public ResponseEntity<?> añadirRed(@RequestBody @Valid CrearRedDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        Contacto contacto = contactoRepository.findById(dto.idContacto()).orElseThrow(() -> new RuntimeException("Contacto no encontrado"));
        if (!contacto.getUsuario().getUsername().equals(userDetails.getUsername())) {
            return ResponseEntity.status(403).build(); // Forbidden
        }
        Red nuevaRed = new Red( dto, contacto);
        redRepository.save(nuevaRed);
        return ResponseEntity.ok().build();
    }

    @Transactional
    @PutMapping("/modificar-red")
    @Operation(summary = "Modifica una red existente de un contacto")
    public ResponseEntity<ModificarRedDTO> modificarRed( @RequestBody @Valid ModificarRedDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = (Usuario) usuarioRepository
                .findByUsername(userDetails.getUsername());

        Contacto contacto = contactoRepository
                .findByIdAndUsuarioId(dto.idContacto(), usuario.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Contacto no encontrado"));
        if (contacto == null | !contacto.getUsuario().getUsername().equals(userDetails.getUsername())) {
            return ResponseEntity.status(403).build(); // Forbidden
        } else {
            Red red  = redRepository.findByIdAndContactoId(dto.id(), dto.idContacto());
            red.actualizarRed(dto.enlace());
            return ResponseEntity.ok().build();
        }

    }


    @Transactional
    @DeleteMapping("/eliminar-red")
    @Operation(summary = "Elimina una red existente de un contacto")
    public ResponseEntity<EliminarRedDTO> eliminarRed(@RequestBody @Valid EliminarRedDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        Contacto contacto = contactoRepository.findById(dto.idContacto()).orElseThrow(() -> new RuntimeException("Contacto no encontrado"));
        if (!contacto.getUsuario().getUsername().equals(userDetails.getUsername())) {
            return ResponseEntity.status(403).build(); // Forbidden
        }
        redRepository.deleteById(dto.id());
        return ResponseEntity.ok().build();
    }
}
