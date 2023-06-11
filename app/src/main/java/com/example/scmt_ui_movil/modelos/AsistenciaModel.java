package com.example.scmt_ui_movil.modelos;

import java.util.Map;

public class AsistenciaModel {
    private int truta_id;
    private int tusuario_id;
    private int asistencia;
    private String fecha;
    private String hora;
    private Map<String,String> data;

    public AsistenciaModel(int truta_id, int tusuario_id, int asistencia, String fecha, String hora) {
        this.truta_id = truta_id;
        this.tusuario_id = tusuario_id;
        this.asistencia = asistencia;
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

    public int getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(int asistencia) {
        this.asistencia = asistencia;
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
}
