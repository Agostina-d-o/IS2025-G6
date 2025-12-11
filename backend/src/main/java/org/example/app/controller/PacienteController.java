package org.example.app.controller;

import org.example.app.ServicioUrgencias;
import org.example.app.controller.dto.PacienteDTO;
import org.example.app.controller.dto.PacienteSimpleDTO;
import org.example.domain.Autoridad;
import org.example.domain.valueobject.AfiliacionObraSocial;
import org.example.domain.valueobject.Cuil;
import org.example.domain.valueobject.Domicilio;
import org.example.domain.valueobject.ObraSocial;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final ServicioUrgencias servicioUrgencias;

    public PacienteController(ServicioUrgencias servicioUrgencias) {
        this.servicioUrgencias = servicioUrgencias;
    }


    @PostMapping
    public ResponseEntity<?> registrarPaciente(@RequestBody PacienteDTO dto) {

        if (dto.autoridad != Autoridad.ENFERMERA) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "No tiene permiso para registrar pacientes"));
        }

        try {

            int numero = dto.numero != null ? dto.numero : 0;
            Domicilio domicilio = new Domicilio(dto.calle, numero, dto.localidad);


            AfiliacionObraSocial afiliacion = null;
            if (dto.codigoObraSocial != null && !dto.codigoObraSocial.isBlank()) {

                ObraSocial obraSocial = new ObraSocial(dto.codigoObraSocial, dto.nombreObraSocial);
                afiliacion = new AfiliacionObraSocial(obraSocial, dto.numeroAfiliado);
            }


            servicioUrgencias.registrarPaciente(
                    dto.cuil,
                    dto.nombre,
                    dto.apellido,
                    domicilio,
                    afiliacion
            );


            return ResponseEntity.ok("Paciente registrado con éxito");
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping
    public List<PacienteSimpleDTO> listar() {
        return servicioUrgencias.listarPacientesRegistrados()
                .stream().map(PacienteSimpleDTO::from).toList();
    }

    @GetMapping("/{cuil}")
    public ResponseEntity<?> buscarPorCuil(@PathVariable String cuil) {
        try {
            var cuilObj = new Cuil(cuil);

            return servicioUrgencias.listarPacientesRegistrados().stream()
                    .filter(p -> p.getCuil().equals(cuilObj))
                    .findFirst()
                    .map(PacienteSimpleDTO::from)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }



}