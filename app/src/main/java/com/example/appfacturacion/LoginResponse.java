package com.example.appfacturacion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    public LoginResponse(String status, respuestalogin message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public respuestalogin getMessage() {
        return message;
    }

    public void setMessage(respuestalogin message) {
        this.message = message;
    }

    @SerializedName("status")
    @Expose
    private final String status;
    @SerializedName("message")
    @Expose
    private respuestalogin message;

}
