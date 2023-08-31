package com.example.appfacturacion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class respuestalogin {


    @SerializedName("nropos")
    @Expose
    private String nropos;
    @SerializedName("rol")
    @Expose
    private String rol;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("id_estacion")
    @Expose
    private String id_estacion;

    @SerializedName("turno")
    @Expose
    private String turno;
    @SerializedName("fechaproceso")
    @Expose
    private String fechaproceso;
    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getFechaproceso() {
        return fechaproceso;
    }

    public void setFechaproceso(String fechaproceso) {
        this.fechaproceso = fechaproceso;
    }




    public String getId_estacion() {
        return id_estacion;
    }

    public void setId_estacion(String id_estacion) {
        this.id_estacion = id_estacion;
    }
    public String getNropos() {
        return nropos;
    }

    public void setNropos(String nropos) {
        this.nropos = nropos;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public respuestalogin(String nropos, String rol, String token,String id_estacion,String turno,String fechaproceso) {
        this.nropos = nropos;
        this.rol = rol;
        this.token = token;
        this.id_estacion=id_estacion;
        this.turno = turno;
        this.fechaproceso = fechaproceso;
    }

}
