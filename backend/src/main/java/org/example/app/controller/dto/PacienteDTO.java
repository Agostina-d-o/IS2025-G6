package org.example.app.controller.dto;

import org.example.domain.Paciente;
import org.example.domain.valueobject.AfiliacionObraSocial;
import org.example.domain.valueobject.Domicilio;

public class PacienteDTO {
    public String cuil;
    public String nombre;
    public String apellido;
    public String calle;
    public Integer numero;
    public String localidad;
    public String codigoObraSocial;
    public String nombreObraSocial;
    public String numeroAfiliado;


    public static PacienteDTO from(Paciente p) {
        PacienteDTO dto = new PacienteDTO();
        if (p == null) return dto;
        dto.cuil = p.getCuil();
        dto.nombre = p.getNombre();
        dto.apellido = p.getApellido();

        Domicilio d = p.getDomicilio();
        if (d != null) {
            dto.calle = d.getCalle();
            dto.numero = d.getNumero();
            dto.localidad = d.getLocalidad();
        }

        AfiliacionObraSocial af = p.getAfiliacionObraSocial();
        if (af != null && af.getObraSocial() != null) {
            dto.codigoObraSocial = af.getObraSocial().getCodigo();
            dto.nombreObraSocial = af.getObraSocial().getNombre();
            dto.numeroAfiliado = af.getNumeroAfiliado();
        }

        return dto;
    }
}
