package com.example.appfacturacion;

public class Venta {
    String cdtipodoc;
    String nrodocumento;
    String fecdocumento;
    String cdcliente;
    String nroplaca;
    String motototal;
    String rscliente;
    String drcliente;
    String cdarticulo;
    String dsarticulo;
    String precio;
    String cantidad;
    String estado;
    String message;




    public Venta(String cdtipodoc, String nrodocumento, String fecdocumento, String cdcliente, String nroplaca, String motototal, String rscliente, String drcliente, String cdarticulo, String dsarticulo, String precio, String cantidad, String estado,String message) {
        this.cdtipodoc = cdtipodoc;
        this.nrodocumento = nrodocumento;
        this.fecdocumento = fecdocumento;
        this.cdcliente = cdcliente;
        this.nroplaca = nroplaca;
        this.motototal = motototal;
        this.rscliente = rscliente;
        this.drcliente = drcliente;
        this.cdarticulo = cdarticulo;
        this.dsarticulo = dsarticulo;
        this.precio = precio;
        this.cantidad = cantidad;
        this.estado = estado;
        this.message=message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getRscliente() {
        return rscliente;
    }

    public void setRscliente(String rscliente) {
        this.rscliente = rscliente;
    }

    public String getDrcliente() {
        return drcliente;
    }

    public void setDrcliente(String drcliente) {
        this.drcliente = drcliente;
    }

    public String getCdarticulo() {
        return cdarticulo;
    }

    public void setCdarticulo(String cdarticulo) {
        this.cdarticulo = cdarticulo;
    }

    public String getDsarticulo() {
        return dsarticulo;
    }

    public void setDsarticulo(String dsarticulo) {
        this.dsarticulo = dsarticulo;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCdtipodoc() {
        return cdtipodoc;
    }

    public void setCdtipodoc(String cdtipodoc) {
        this.cdtipodoc = cdtipodoc;
    }

    public String getNrodocumento() {
        return nrodocumento;
    }

    public void setNrodocumento(String nrodocumento) {
        this.nrodocumento = nrodocumento;
    }

    public String getFecdocumento() {
        return fecdocumento;
    }

    public void setFecdocumento(String fecdocumento) {
        this.fecdocumento = fecdocumento;
    }

    public String getCdcliente() {
        return cdcliente;
    }

    public void setCdcliente(String cdcliente) {
        this.cdcliente = cdcliente;
    }

    public String getNroplaca() {
        return nroplaca;
    }

    public void setNroplaca(String nroplaca) {
        this.nroplaca = nroplaca;
    }

    public String getMotototal() {
        return motototal;
    }

    public void setMotototal(String motototal) {
        this.motototal = motototal;
    }


}
