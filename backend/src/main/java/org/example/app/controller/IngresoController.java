package org.example.app.controller;

import org.example.app.ServicioUrgencias;
import org.example.app.controller.dto.IngresoDTO;
import org.example.domain.Ingreso;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/urgencias")
public class IngresoController {

    private final ServicioUrgencias servicioUrgencias;

    public IngresoController(ServicioUrgencias servicioUrgencias) {
        this.servicioUrgencias = servicioUrgencias;
    }

    @PostMapping("/ingresos")
    public void registrarIngreso(@RequestBody IngresoDTO dto) {
        servicioUrgencias.registrarUrgencia(
                dto.cuilPaciente,
                dto.getEnfermera(),
                dto.informe,
                dto.getNivel(),
                dto.temperatura,
                dto.frecuenciaCardiaca,
                dto.frecuenciaRespiratoria,
                dto.presionSistolica,
                dto.presionDiastolica
        );
    }

    @GetMapping("/pendientes")
    public List<Ingreso> obtenerPendientes() {
        return servicioUrgencias.obtenerIngresosPendientes();
    }
}
