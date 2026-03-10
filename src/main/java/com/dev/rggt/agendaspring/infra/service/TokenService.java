package com.dev.rggt.agendaspring.infra.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.dev.rggt.agendaspring.domain.usuarios.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    // Genera un token JWT para el usuario que se autentica
    public String generarToken(Usuario usuario) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    // Quien emite el token
                    .withIssuer("Api Agenda Spring")
                    // Genera un token JWT para el usuario que se autentica
                    .withSubject(usuario.getUsername())
                    // fecha de expiración
                    .withExpiresAt(fechaExpiracion())
                    // lo firma con la clave secreta
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("error al generar el token JWT", exception);
        }
    }
    // 3 horas de expiracion
    private Instant fechaExpiracion() {
        return Instant.now().plus(1, ChronoUnit.HOURS);
    }

    // Valida el token JWT y obtiene el sujeto (login del usuario)


    public String getSubject(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("Api Agenda Spring")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception){
            throw new RuntimeException("Token JWT invalido o expirado!");
        }
    }

}