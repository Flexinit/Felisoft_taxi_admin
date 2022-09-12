package com.example.drupp_driver.connectivity.http;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QualStandardResponse<T> {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("response")
    @Expose
    private T response;

    @SerializedName("message")
    @Expose
    private String errorResponse;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    public String getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(String errorResponse) {
        this.errorResponse = errorResponse;
    }
}
