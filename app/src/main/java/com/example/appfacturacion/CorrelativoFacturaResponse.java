package com.example.appfacturacion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CorrelativoFacturaResponse {
    @SerializedName("cpe_fac_serie")
    @Expose
    private String cpe_fac_serie;
    @SerializedName("cpe_fac_nrodoc")
    @Expose
    private String cpe_fac_nrodoc;

    public CorrelativoFacturaResponse(String cpe_bol_serie, String cpe_bol_nrodoc,String fechaproceso,String turno,String message) {
        this.cpe_fac_serie = cpe_bol_serie;
        this.cpe_fac_nrodoc = cpe_bol_nrodoc;
        this.fechaproceso =fechaproceso;
        this.turno=turno;
        this.message=message;

    }

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
    public String getCpe_fac_serie() {
        return cpe_fac_serie;
    }

    public void setCpe_fac_serie(String cpe_fac_serie) {
        this.cpe_fac_serie = cpe_fac_serie;
    }

    public String getCpe_fac_nrodoc() {
        return cpe_fac_nrodoc;
    }

    public void setCpe_fac_nrodoc(String cpe_fac_nrodoc) {
        this.cpe_fac_nrodoc = cpe_fac_nrodoc;
    }


}
