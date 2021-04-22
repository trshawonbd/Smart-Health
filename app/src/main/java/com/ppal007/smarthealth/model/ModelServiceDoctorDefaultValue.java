package com.ppal007.smarthealth.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelServiceDoctorDefaultValue {

    @SerializedName("doctor_id")
    @Expose
    private String DoctorId;

    @SerializedName("patient_id ")
    @Expose
    private String PatientId;

    @SerializedName("bp_up")
    @Expose
    private String BpUp;

    @SerializedName("bp_down")
    @Expose
    private String BpDown;

    @SerializedName("Temrature")
    @Expose
    private String Temperature;

    @SerializedName("spo2")
    @Expose
    private String Spo2;

    @SerializedName("ecg")
    @Expose
    private String Ecg;

    @SerializedName("gl_bfr")
    @Expose
    private String GlBfrEat;

    @SerializedName("gl_aftr")
    @Expose
    private String GlAftrEat;

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;


    //constructor
    public ModelServiceDoctorDefaultValue(String patientId, String bpUp, String bpDown, String temperature, String spo2, String ecg, String glBfrEat, String glAftrEat) {
        PatientId = patientId;
        BpUp = bpUp;
        BpDown = bpDown;
        Temperature = temperature;
        Spo2 = spo2;
        Ecg = ecg;
        GlBfrEat = glBfrEat;
        GlAftrEat = glAftrEat;
    }


    public String getPatientId() {
        return PatientId;
    }

    public String getBpUp() {
        return BpUp;
    }

    public String getBpDown() {
        return BpDown;
    }

    public String getTemperature() {
        return Temperature;
    }

    public String getSpo2() {
        return Spo2;
    }

    public String getEcg() {
        return Ecg;
    }

    public String getGlBfrEat() {
        return GlBfrEat;
    }

    public String getGlAftrEat() {
        return GlAftrEat;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
