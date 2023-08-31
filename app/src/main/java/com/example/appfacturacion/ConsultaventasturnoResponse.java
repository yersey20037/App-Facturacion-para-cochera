package com.example.appfacturacion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ConsultaventasturnoResponse {
    public ConsultaventasturnoResponse(String status, respuestaventaturno[] message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public respuestaventaturno[] getMessage() {
        return message;
    }

    public void setMessage(respuestaventaturno[] message) {
        this.message = message;
    }

    @SerializedName("status")
    @Expose
    private final String status;

    @SerializedName("message")
    @Expose
    private respuestaventaturno[] message;
}
