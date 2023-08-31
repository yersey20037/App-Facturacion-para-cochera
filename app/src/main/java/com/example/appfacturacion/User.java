package com.example.appfacturacion;



public class User {
    public String id;
    public String nropos;
    public String email;
    public String rol;
    public String password;
    public String idtoken;
    public String fecha;
    public String status;
    public String changeboleta;


    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getChangeboleta() {
        return changeboleta;
    }
    public void setChangeboleta(String changeboleta) {
        this.changeboleta = changeboleta;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNropos() {
        return nropos;
    }
    public void setNropos(String nropos) {
        this.nropos = nropos;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getIdtoken() {
        return idtoken;
    }
    public void setIdtoken(String idtoken) {
        this.idtoken = idtoken;
    }
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public User(String id, String nropos, String email, String rol, String password, String idtoken, String fecha,String status,String changeboleta) {
        this.id = id;
        this.nropos = nropos;
        this.email = email;
        this.rol = rol;
        this.password = password;
        this.idtoken = idtoken;
        this.fecha = fecha;
        this.status=status;
        this.changeboleta=changeboleta;
    }
}
