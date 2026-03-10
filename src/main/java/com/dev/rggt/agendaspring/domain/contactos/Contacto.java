package com.dev.rggt.agendaspring.domain.contactos;

import com.dev.rggt.agendaspring.domain.contactos.dto.CrearContactoDTO;
import com.dev.rggt.agendaspring.domain.contactos.dto.ModificarContactoDTO;
import com.dev.rggt.agendaspring.domain.redes.Red;
import com.dev.rggt.agendaspring.domain.usuarios.Usuario;
import com.dev.rggt.agendaspring.domain.usuarios.dto.ActualizarUsuarioDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;



@Table(name = "contacto")
@Entity(name = "Contacto")
@Getter
@NoArgsConstructor
@AllArgsConstructor
// Sirve para comparar objetos y generar hashcodes basados en el campo "id"
@EqualsAndHashCode(of = "id")
public class Contacto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String telefono;
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    @OneToMany(mappedBy = "contacto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Red> redes;

    public Contacto(CrearContactoDTO datos, Usuario usuario) {
        this.nombre = datos.nombre();
        this.telefono = datos.telefono();
        this.email = datos.email();
        this.usuario = usuario;
    }

    public void actualizarContacto(ModificarContactoDTO modificar) {

        if (modificar.nombre() != null && !modificar.nombre().isBlank()) {
            this.nombre = capitalizado(modificar.nombre());
        }

        if (modificar.telefono() != null && !modificar.telefono().isBlank()) {
            this.telefono = modificar.telefono();
        }

        if (modificar.email() != null && !modificar.email().isBlank()) {
            this.email = modificar.email();
        }
    }

    private String capitalizado(String string) {
        return string.substring(0,1).toUpperCase()+string.substring(1).toLowerCase();
    }


}
