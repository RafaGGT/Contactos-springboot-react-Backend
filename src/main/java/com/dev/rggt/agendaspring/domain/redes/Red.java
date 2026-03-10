package com.dev.rggt.agendaspring.domain.redes;

import com.dev.rggt.agendaspring.domain.contactos.Contacto;
import com.dev.rggt.agendaspring.domain.redes.dto.CrearRedDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "red_social")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Red {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoRed tipo;

    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contacto_id", nullable = false)
    private Contacto contacto;

    public Red(CrearRedDTO datos, Contacto contacto) {
        this.tipo = datos.tipo();
        this.url = datos.enlace();
        this.contacto = contacto;
    }

    public void actualizarRed(String enlace) {
        if (enlace != null && !enlace.isBlank()) {
            this.url = enlace;
        }
    }
}
