package org.example.domain;

public abstract class Persona {

    // Campos comunes
    protected String cuil;
    protected String nombre;
    protected String apellido;
    protected String email;

    protected Persona(String cuil, String nombre, String apellido, String email) {
        this.cuil = cuil;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
    }

    public String getCuil() {
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
}
