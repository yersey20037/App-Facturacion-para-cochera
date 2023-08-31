package com.example.appfacturacion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class respuestaruc {
    public String getRazonsocial() {
        return razonsocial;
    }

    public void setRazonsocial(String razonsocial) {
        this.razonsocial = razonsocial;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @SerializedName("razonSocial")
    @Expose
    private String razonsocial;
    @SerializedName("direccion")
    @Expose
    private String direccion;

}
