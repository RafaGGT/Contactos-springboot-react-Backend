package com.dev.rggt.agendaspring.domain.redes.dto;

import com.dev.rggt.agendaspring.domain.redes.TipoRed;

public record CrearRedDTO( TipoRed tipo, String enlace, Long idContacto) {
}
