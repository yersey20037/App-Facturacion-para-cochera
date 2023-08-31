package com.example.appfacturacion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CorrelativoBoletaResponse {

    public CorrelativoBoletaResponse(String cpe_bol_serie, String cpe_bol_nrodoc,String fechaproceso,String turno,String message) {
        this.cpe_bol_serie = cpe_bol_serie;
        this.cpe_bol_nrodoc = cpe_bol_nrodoc;
        this.fechaproceso =fechaproceso;
        this.turno=turno;
        this.message=message;

    }

    @SerializedName("cpe_bol_serie")
    @Expose
    private String cpe_bol_serie;
    @SerializedName("cpe_bol_nrodoc")
    @Expose
    private String cpe_bol_nrodoc;

    public String getFechaproceso() {
        return fechaproceso;
    }

    public void setFechaproceso(String fechaproceso) {
        this.fechaproceso = fechaproceso;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    @SerializedName("fechaproceso")
    @Expose
    private String fechaproceso;
    @SerializedName("turno")
    @Expose
    private String turno;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("message")
    @Expose
    private String message;
    public String getCpe_bol_serie() {
        return cpe_bol_serie;
    }

    public void setCpe_bol_serie(String cpe_bol_serie) {
        this.cpe_bol_serie = cpe_bol_serie;
    }

    public String getCpe_bol_nrodoc() {
        return cpe_bol_nrodoc;
    }

    public void setCpe_bol_nrodoc(String cpe_bol_nrodoc) {
        this.cpe_bol_nrodoc = cpe_bol_nrodoc;
    }


}
