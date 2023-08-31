package com.example.appfacturacion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class respuestadni {
    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombre) {
        this.nombres = nombres;
    }

    public String getApellidopaterno() {
        return apellidopaterno;
    }

    public void setApellidopaterno(String apellidopaterno) {
        this.apellidopaterno = apellidopaterno;
    }

    public String getApellidomaterno() {
        return apellidomaterno;
    }

    public void setApellidomaterno(String apellidomaterno) {
        this.apellidomaterno = apellidomaterno;
    }

    @SerializedName("apellidoPaterno")
    @Expose
    private String apellidopaterno;

    @SerializedName("apellidoMaterno")
    @Expose
    private String apellidomaterno;

    @SerializedName("nombres")
    @Expose
    private String nombres;

}
