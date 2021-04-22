package com.ppal007.smarthealth.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelAcceptPatient {

    @SerializedName("doctor_id")
    @Expose
    private String DoctorId;

    @SerializedName("id")
    @Expose
    private String Id;

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
