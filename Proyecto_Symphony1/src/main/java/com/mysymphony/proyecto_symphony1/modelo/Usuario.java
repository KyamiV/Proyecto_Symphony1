/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.modelo;

/**
 * Modelo institucional para representar usuarios del sistema SymphonySIAS.
 * Incluye atributos comunes y específicos para roles como docente y estudiante.
 * Autor: camiv
 */
public class Usuario {

    // 🔹 Atributos comunes
    private int idUsuario;
    private String correo;
    private String nombre;
    private String clave;
    private String rol;
    private String estado;
    private String usuarioCreador;

    // 🔹 Atributos específicos para docentes
    private Integer idDocente;   // ✅ agregado
    private String instrumento;
    private String etapa;

    // 🔹 Atributo específico para estudiantes
    private Integer idEstudiante;

    // 🔸 Constructores
    public Usuario() {}

    // Constructor básico para correo, nombre y rol
    public Usuario(String correo, String nombre, String rol) {
        this.correo = correo;
        this.nombre = nombre;
        this.rol = rol;
    }

    // ✅ Constructor agregado para herencia (Docente, Estudiante)
    public Usuario(int idUsuario, String nombre) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
    }

    // Constructor completo
    public Usuario(int idUsuario, String correo, String nombre, String rol,
                   String clave, String estado, String usuarioCreador,
                   Integer idDocente, String instrumento, String etapa,
                   Integer idEstudiante) {
        this.idUsuario = idUsuario;
        this.correo = correo;
        this.nombre = nombre;
        this.rol = rol;
        this.clave = clave;
        this.estado = estado;
        this.usuarioCreador = usuarioCreador;
        this.idDocente = idDocente;
        this.instrumento = instrumento;
        this.etapa = etapa;
        this.idEstudiante = idEstudiante;
    }

    // 🔸 Getters y Setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    // 👉 Alias para compatibilidad con JSP que usa getId()
    public int getId() { return idUsuario; }
    public void setId(int id) { this.idUsuario = id; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getUsuarioCreador() { return usuarioCreador; }
    public void setUsuarioCreador(String usuarioCreador) { this.usuarioCreador = usuarioCreador; }

    public Integer getIdDocente() { return idDocente; }   // ✅ nuevo getter
    public void setIdDocente(Integer idDocente) { this.idDocente = idDocente; } // ✅ nuevo setter

    public String getInstrumento() { return instrumento; }
    public void setInstrumento(String instrumento) { this.instrumento = instrumento; }

    public String getEtapa() { return etapa; }
    public void setEtapa(String etapa) { this.etapa = etapa; }

    public Integer getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(Integer idEstudiante) { this.idEstudiante = idEstudiante; }

    // 🔍 toString útil para depuración y auditoría
    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", correo='" + correo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", clave='" + clave + '\'' +
                ", rol='" + rol + '\'' +
                ", estado='" + estado + '\'' +
                ", usuarioCreador='" + usuarioCreador + '\'' +
                ", idDocente=" + idDocente +
                ", instrumento='" + instrumento + '\'' +
                ", etapa='" + etapa + '\'' +
                ", idEstudiante=" + idEstudiante +
                '}';
    }
}