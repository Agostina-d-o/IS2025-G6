package org.example.domain;

import org.example.domain.valueobject.Cuil;

public abstract class Persona {

    // Campos comunes
    protected Cuil cuil;
    protected String nombre;
    protected String apellido;
    protected String email;

    protected Persona(Cuil cuil, String nombre, String apellido, String email) {
        this.cuil = cuil;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
    }

    public Cuil getCuil() {
        return cuil;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }
    public String getNombreCompleto() {
        return this.nombre + " " + this.apellido;
    }

}
