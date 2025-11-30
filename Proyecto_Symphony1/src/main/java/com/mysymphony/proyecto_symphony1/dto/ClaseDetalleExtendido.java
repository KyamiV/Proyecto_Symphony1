/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO extendido para representar información complementaria de una clase.
 * Autor: camiv
 * Trazabilidad: se construye desde múltiples fuentes (clases, docentes, inscripciones, notas, auditoría).
 */
public class ClaseDetalleExtendido {

    // 🔑 Identificador de la clase
    private int idClase;
    private String nombreClase;
    private String instrumento;
    private String etapa;
    private String grupo;
    private int cupo;
    private int inscritos;

    // 🧑‍🏫 Docente asignado
    private String nombreDocente;
    private String instrumentoDocente;
    private String diaSemana;
    private String horaInicio;
    private String horaFin;
    private int totalClasesDocente;

    // 📊 Indicadores académicos
    private double porcentajeOcupado;
    private int tablasEnviadas;
    private int estudiantesAprobados;
    private int estudiantesFinalizaronEtapa;

    // 🧾 Historial y trazabilidad
    private String fechaCreacionClase;
    private String usuarioCreador;
    private String ultimaAccionAuditoria;

    // 📁 Estado académico
    private int etapasCompletadas;
    private String estadoAcademico;
    private boolean tablaEnviadaPorDocente;
    private boolean validadaPorAdministrador;

    // 👥 Detalles de estudiantes inscritos
    private List<EstudianteDetalle> estudiantesInscritos = new ArrayList<>();

    // ============================
    // Constructores
    // ============================
    public ClaseDetalleExtendido() {}

    public ClaseDetalleExtendido(int idClase, String nombreClase, String instrumento, String etapa, String grupo, int cupo) {
        this.idClase = idClase;
        this.nombreClase = nombreClase;
        this.instrumento = instrumento;
        this.etapa = etapa;
        this.grupo = grupo;
        this.cupo = cupo;
    }

    // ============================
    // Getters y Setters
    // ============================
    public int getIdClase() { return idClase; }
    public void setIdClase(int idClase) { this.idClase = idClase; }

    public String getNombreClase() { return nombreClase; }
    public void setNombreClase(String nombreClase) { this.nombreClase = nombreClase; }

    public String getInstrumento() { return instrumento; }
    public void setInstrumento(String instrumento) { this.instrumento = instrumento; }

    public String getEtapa() { return etapa; }
    public void setEtapa(String etapa) { this.etapa = etapa; }

    public String getGrupo() { return grupo; }
    public void setGrupo(String grupo) { this.grupo = grupo; }

    public int getCupo() { return cupo; }
    public void setCupo(int cupo) { this.cupo = cupo; }

    public int getInscritos() { return inscritos; }
    public void setInscritos(int inscritos) { this.inscritos = inscritos; }

    public String getNombreDocente() { return nombreDocente; }
    public void setNombreDocente(String nombreDocente) { this.nombreDocente = nombreDocente; }

    public String getInstrumentoDocente() { return instrumentoDocente; }
    public void setInstrumentoDocente(String instrumentoDocente) { this.instrumentoDocente = instrumentoDocente; }

    public String getDiaSemana() { return diaSemana; }
    public void setDiaSemana(String diaSemana) { this.diaSemana = diaSemana; }

    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }

    public String getHoraFin() { return horaFin; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }

    public int getTotalClasesDocente() { return totalClasesDocente; }
    public void setTotalClasesDocente(int totalClasesDocente) { this.totalClasesDocente = totalClasesDocente; }

    public double getPorcentajeOcupado() { return porcentajeOcupado; }
    public void setPorcentajeOcupado(double porcentajeOcupado) { this.porcentajeOcupado = porcentajeOcupado; }

    public int getTablasEnviadas() { return tablasEnviadas; }
    public void setTablasEnviadas(int tablasEnviadas) { this.tablasEnviadas = tablasEnviadas; }

    public int getEstudiantesAprobados() { return estudiantesAprobados; }
    public void setEstudiantesAprobados(int estudiantesAprobados) { this.estudiantesAprobados = estudiantesAprobados; }

    public int getEstudiantesFinalizaronEtapa() { return estudiantesFinalizaronEtapa; }
    public void setEstudiantesFinalizaronEtapa(int estudiantesFinalizaronEtapa) { this.estudiantesFinalizaronEtapa = estudiantesFinalizaronEtapa; }

    public String getFechaCreacionClase() { return fechaCreacionClase; }
    public void setFechaCreacionClase(String fechaCreacionClase) { this.fechaCreacionClase = fechaCreacionClase; }

    public String getUsuarioCreador() { return usuarioCreador; }
    public void setUsuarioCreador(String usuarioCreador) { this.usuarioCreador = usuarioCreador; }

    public String getUltimaAccionAuditoria() { return ultimaAccionAuditoria; }
    public void setUltimaAccionAuditoria(String ultimaAccionAuditoria) { this.ultimaAccionAuditoria = ultimaAccionAuditoria; }

    public int getEtapasCompletadas() { return etapasCompletadas; }
    public void setEtapasCompletadas(int etapasCompletadas) { this.etapasCompletadas = etapasCompletadas; }

    public String getEstadoAcademico() { return estadoAcademico; }
    public void setEstadoAcademico(String estadoAcademico) { this.estadoAcademico = estadoAcademico; }

    public boolean isTablaEnviadaPorDocente() { return tablaEnviadaPorDocente; }
    public void setTablaEnviadaPorDocente(boolean tablaEnviadaPorDocente) { this.tablaEnviadaPorDocente = tablaEnviadaPorDocente; }

    public boolean isValidadaPorAdministrador() { return validadaPorAdministrador; }
    public void setValidadaPorAdministrador(boolean validadaPorAdministrador) { this.validadaPorAdministrador = validadaPorAdministrador; }

    public List<EstudianteDetalle> getEstudiantesInscritos() { return estudiantesInscritos; }
    public void setEstudiantesInscritos(List<EstudianteDetalle> estudiantesInscritos) {
        this.estudiantesInscritos = (estudiantesInscritos != null) ? estudiantesInscritos : new ArrayList<>();
    }

    // ============================
    // toString para trazabilidad
    // ============================
    @Override
    public String toString() {
        return "ClaseDetalleExtendido{" +
                "idClase=" + idClase +
                ", nombreClase='" + nombreClase + '\'' +
                ", instrumento='" + instrumento + '\'' +
                ", etapa='" + etapa + '\'' +
                ", grupo='" + grupo + '\'' +
                ", cupo=" + cupo +
                ", inscritos=" + inscritos +
                ", nombreDocente='" + nombreDocente + '\'' +
                ", instrumentoDocente='" + instrumentoDocente + '\'' +
                ", diaSemana='" + diaSemana + '\'' +
                ", horaInicio='" + horaInicio + '\'' +
                ", horaFin='" + horaFin + '\'' +
                ", totalClasesDocente=" + totalClasesDocente +
                ", porcentajeOcupado=" + porcentajeOcupado +
                ", tablasEnviadas=" + tablasEnviadas +
                ", estudiantesAprobados=" + estudiantesAprobados +
                ", estudiantesFinalizaronEtapa=" + estudiantesFinalizaronEtapa +
                ", fechaCreacionClase='" + fechaCreacionClase + '\'' +
                ", usuarioCreador='" + usuarioCreador + '\'' +
                ", ultimaAccionAuditoria='" + ultimaAccionAuditoria + '\'' +
                ", etapasCompletadas=" + etapasCompletadas +
                ", estadoAcademico='" + estadoAcademico + '\'' +
                ", tablaEnviadaPorDocente=" + tablaEnviadaPorDocente +
                ", validadaPorAdministrador=" + validadaPorAdministrador +
                ", estudiantesInscritos=" + estudiantesInscritos +
                '}';
    }

        // ✅ Clase interna estática para representar cada estudiante inscrito
    public static class EstudianteDetalle {
        private String nombre;
        private String correo;
        private String etapaActual;
        private boolean tieneNotas;
        private String condicion;

        // ============================
        // Constructores
        // ============================
        public EstudianteDetalle() {}

        public EstudianteDetalle(String nombre, String correo, String etapaActual, boolean tieneNotas, String condicion) {
            this.nombre = nombre;
            this.correo = correo;
            this.etapaActual = etapaActual;
            this.tieneNotas = tieneNotas;
            this.condicion = condicion;
        }

        // ============================
        // Getters y Setters
        // ============================
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }

        public String getEtapaActual() { return etapaActual; }
        public void setEtapaActual(String etapaActual) { this.etapaActual = etapaActual; }

        public boolean isTieneNotas() { return tieneNotas; }
        public void setTieneNotas(boolean tieneNotas) { this.tieneNotas = tieneNotas; }

        public String getCondicion() { return condicion; }
        public void setCondicion(String condicion) { this.condicion = condicion; }

        // ============================
        // toString para trazabilidad
        // ============================
        @Override
        public String toString() {
            return "EstudianteDetalle{" +
                    "nombre='" + nombre + '\'' +
                    ", correo='" + correo + '\'' +
                    ", etapaActual='" + etapaActual + '\'' +
                    ", tieneNotas=" + tieneNotas +
                    ", condicion='" + condicion + '\'' +
                    '}';
        }
    }
}