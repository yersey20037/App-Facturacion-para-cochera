package com.example.appfacturacion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConsultaDniResponse {
    public ConsultaDniResponse(String status, respuestadni message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public respuestadni getMessage() {
        return message;
    }

    public void setMessage(respuestadni message) {
        this.message = message;
    }

    @SerializedName("status")
    @Expose
    private final String status;
    @SerializedName("message")
    @Expose
    private respuestadni message;
}
