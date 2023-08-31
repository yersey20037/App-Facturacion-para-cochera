package com.example.appfacturacion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConsultaRucResponse {
    public ConsultaRucResponse(String status, respuestaruc message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public respuestaruc getMessage() {
        return message;
    }

    public void setMessage(respuestaruc message) {
        this.message = message;
    }

    @SerializedName("status")
    @Expose
    private final String status;
    @SerializedName("message")
    @Expose
    private respuestaruc message;
}
