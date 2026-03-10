package com.dev.rggt.agendaspring.domain.redes.dto;

import com.dev.rggt.agendaspring.domain.redes.TipoRed;

public record DetallesRedDTO(Long id, TipoRed tipo, String enlace) {
    public DetallesRedDTO(com.dev.rggt.agendaspring.domain.redes.Red red) {
        this(
                red.getId(),
                red.getTipo(),
                red.getUrl()
        );
    }
}
