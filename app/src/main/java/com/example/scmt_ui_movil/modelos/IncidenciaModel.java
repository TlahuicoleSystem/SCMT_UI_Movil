package com.example.scmt_ui_movil.modelos;

import java.util.Map;

public class IncidenciaModel {
    private int truta_id;
    private int tusuario_id;
    private String nombre;
    private String nombre_ruta;
    private String nombre_incidente;
    private String descripcion;
    private String fecha;
    private String hora;
    private Map<String,String> data;

    public IncidenciaModel(int truta_id, int tusuario_id, String nombre, String descripcion, String fecha, String hora) {
        this.truta_id = truta_id;
        this.tusuario_id = tusuario_id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getTruta_id() {
        return truta_id;
    }

    public void setTruta_id(int truta_id) {
        this.truta_id = truta_id;
    }

    public int getTusuario_id() {
        return tusuario_id;
    }

    public void setTusuario_id(int tusuario_id) {
        this.tusuario_id = tusuario_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public String getNombre_ruta() {
        return nombre_ruta;
    }

    public void setNombre_ruta(String nombre_ruta) {
        this.nombre_ruta = nombre_ruta;
    }

    public String getNombre_incidente() {
        return nombre_incidente;
    }

    public void setNombre_incidente(String nombre_incidente) {
        this.nombre_incidente = nombre_incidente;
    }
}
