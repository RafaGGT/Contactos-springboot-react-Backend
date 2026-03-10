package com.dev.rggt.agendaspring.domain.contactos.dto;

import com.dev.rggt.agendaspring.domain.contactos.Contacto;
import com.dev.rggt.agendaspring.domain.redes.dto.DetallesRedDTO;
import com.dev.rggt.agendaspring.domain.redes.Red;

import java.util.List;

public record DetallesContactoDTO(
        Long id,
        String nombre,
        String telefono,
        String email,
        List<DetallesRedDTO> redes
) {
    public DetallesContactoDTO(Contacto c) {
        this(
                c.getId(),
                c.getNombre(),
                c.getTelefono(),
                c.getEmail(),
                c.getRedes()
                        .stream()
                        .map(r -> new DetallesRedDTO(r))
                        .toList()
        );
    }
}
