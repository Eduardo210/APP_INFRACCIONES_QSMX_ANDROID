package mx.qsistemas.infracciones.DataManagement.Models;

/**
 * Developed by ingmtz on 10/21/16.
 */

public class UserLogin {
    private String idPersonaAyuntamiento, idPersona, username, password, nombre, aPaterno, aMaterno;


    public String getIdPersonaAyuntamiento() {
        return idPersonaAyuntamiento;
    }

    public void setIdPersonaAyuntamiento(String idPersonaAyuntamiento) {
        this.idPersonaAyuntamiento = idPersonaAyuntamiento;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getaPaterno() {
        return aPaterno;
    }

    public void setaPaterno(String aPaterno) {
        this.aPaterno = aPaterno;
    }

    public String getaMaterno() {
        return aMaterno;
    }

    public void setaMaterno(String aMaterno) {
        this.aMaterno = aMaterno;
    }

    public String getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(String idPersona) {
        this.idPersona = idPersona;
    }
}
