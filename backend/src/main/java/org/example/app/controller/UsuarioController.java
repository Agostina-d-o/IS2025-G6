package org.example.app.controller;

import org.example.app.ServicioAutenticacion;
import org.example.app.controller.dto.CredencialesDTO;
import org.example.app.controller.dto.RegistroDTO;
import org.example.domain.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UsuarioController {

    private final ServicioAutenticacion servicio;

    public UsuarioController(ServicioAutenticacion servicio) {
        this.servicio = servicio;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CredencialesDTO cred) {
        try {
            Usuario u = servicio.login(cred.email, cred.contrasenia);
            //return ResponseEntity.ok(Map.of("email", u.getEmail(), "rol", u.getAutoridad()));
            return ResponseEntity.ok(Map.of(
                    "email", u.getEmail(),
                    "rol",   u.getAutoridad(),
                    "nombre", u.getNombre(),
                    "apellido", u.getApellido()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage()));
        }

    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody RegistroDTO dto) {
        Usuario u = servicio.registrar(dto.email, dto.contrasenia, dto.rol, dto.nombre, dto.apellido);
        //return ResponseEntity.ok(Map.of("email", u.getEmail(), "rol", u.getAutoridad()));
        return ResponseEntity.ok(Map.of(
                "email", u.getEmail(),
                "rol",   u.getAutoridad(),
                "nombre", u.getNombre(),
                "apellido", u.getApellido()
        ));
    }

}
