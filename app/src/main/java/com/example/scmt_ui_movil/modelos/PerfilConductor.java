package com.example.scmt_ui_movil.modelos;

import java.util.Map;

public class PerfilConductor {
    private Map<String, String> data;
    private int id;
    private String nombre;
    private String primer_apellido;
    private String segundo_apellido;
    private String id_empleado;
    private String direccion;
    private String licencia;
    private String usuario;
    private int telefono;

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPrimer_apellido() { return primer_apellido; }

    public void setPrimer_apellido(String primer_apellido) { this.primer_apellido = primer_apellido; }

    public String getSegundo_apellido() { return segundo_apellido; }

    public void setSegundo_apellido(String segundo_apellido) { this.segundo_apellido = segundo_apellido; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getId_empleado() { return id_empleado; }

    public void setId_empleado(String id_empleado) { this.id_empleado = id_empleado; }

    public String getDireccion() { return direccion; }

    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getLicencia() { return licencia; }

    public void setLicencia(String licencia) { this.licencia = licencia; }

    public String getUsuario() { return usuario; }

    public void setUsuario(String usuario) { this.usuario = usuario; }

    public int getTelefono() { return telefono; }

    public void setTelefono(int telefono) { this.telefono = telefono; }
    public Map<String, String> getData() { return data; }

    public void setData(Map<String, String> data) { this.data = data; }

}
