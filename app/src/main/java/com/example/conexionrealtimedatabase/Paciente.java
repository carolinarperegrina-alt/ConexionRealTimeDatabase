package com.example.conexionrealtimedatabase;

public class Paciente {
    private String nombre;
    private String apellidos;
    private String grupoSanguineo;
    private String nuhsa;
    private String fechaNacimiento;

    //Constructor vacío (importante para Firebase)
    public Paciente() {
    }

    public Paciente(String nombre, String apellidos, String grupoSanguineo, String nuhsa) {
        this(nombre, apellidos, grupoSanguineo, nuhsa, null);
    }

    public Paciente(String nombre, String apellidos, String grupoSanguineo, String nuhsa, String fechaNacimiento) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.grupoSanguineo = grupoSanguineo;
        this.nuhsa = nuhsa;
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getGrupoSanguineo() {
        return grupoSanguineo;
    }

    public String getNuhsa() {
        return nuhsa;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    @Override
    public String toString() {
        return "Paciente{" +
                "nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", grupoSanguineo='" + grupoSanguineo + '\'' +
                ", nuhsa='" + nuhsa + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                '}';
    }
}