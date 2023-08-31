package com.example.appfacturacion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class respuestaventaturno {

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public respuestaventaturno(String placa, double monto,String turno,String fechaproceso) {
        this.placa = placa;
        this.monto = monto;
        this.turno=turno;
        this.fechaproceso=fechaproceso;
    }

    @SerializedName("placa")
    @Expose
    private String placa;
    @SerializedName("monto")
    @Expose
    private double monto;

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    @SerializedName("turno")
    @Expose
    private String turno;

    public String getFechaproceso() {
        return fechaproceso;
    }

    public void setFechaproceso(String fechaproceso) {
        this.fechaproceso = fechaproceso;
    }

    @SerializedName("fechaproceso")
    @Expose
    private String fechaproceso;

}
