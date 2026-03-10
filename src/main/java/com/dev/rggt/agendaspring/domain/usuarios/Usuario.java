package com.dev.rggt.agendaspring.domain.usuarios;

import com.dev.rggt.agendaspring.domain.contactos.Contacto;
import com.dev.rggt.agendaspring.domain.usuarios.dto.ActualizarUsuarioDTO;
import com.dev.rggt.agendaspring.domain.usuarios.dto.CrearUsuarioDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Table(name = "usuarios")
@Entity(name = "Usuario")
@Getter
@NoArgsConstructor
@AllArgsConstructor
// Sirve para comparar objetos y generar hashcodes basados en el campo "id"
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    private String correo;
    private String nombre;
    private LocalDateTime fechaCreacion;

    public Usuario(CrearUsuarioDTO datos, String passwordCodificada) {
        this.username = datos.username();
        this.password = passwordCodificada;
        this.correo = datos.correo();
        this.nombre = datos.nombre();
        this.fechaCreacion = LocalDateTime.now();
    }


    public void actualizarUsuario(ActualizarUsuarioDTO actualizarUsuarioDTO) {
        if (actualizarUsuarioDTO.nombre() != null){
            this.nombre = capitalizado(actualizarUsuarioDTO.nombre());
        }
        if (actualizarUsuarioDTO.correo() != null){
            this.correo = actualizarUsuarioDTO.correo();
        }
    }

    private String capitalizado(String string) {
        return string.substring(0,1).toUpperCase()+string.substring(1).toLowerCase();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
