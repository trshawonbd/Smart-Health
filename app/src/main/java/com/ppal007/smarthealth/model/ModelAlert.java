package com.ppal007.smarthealth.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelAlert {

    @SerializedName("doctor_id")
    @Expose
    private String DoctorId;

    @SerializedName("patient_tbl_id")
    @Expose
    private String PatientTblId;

    @SerializedName("bp_up")
    @Expose
    private String BpUp;

    @SerializedName("bp_down")
    @Expose
    private String BpDown;

    @SerializedName("spo2")
    @Expose
    private String Spo2;

    @SerializedName("gl_befr_eat")
    @Expose
    private String GlBfr;

    @SerializedName("gl_aftr_eat")
    @Expose
    private String GlAftr;

    @SerializedName("temperature")
    @Expose
    private String Tmpr;

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;


//    public ModelAlert(String patientTblId) {
//        PatientTblId = patientTblId;
//    }


//    public ModelAlert(String patientTblId, String bpUp, String bpDown, String spo2, String glBfr, String glAftr, String tmpr) {
//        PatientTblId = patientTblId;
//        BpUp = bpUp;
//        BpDown = bpDown;
//        Spo2 = spo2;
//        GlBfr = glBfr;
//        GlAftr = glAftr;
//        Tmpr = tmpr;
//    }

    public String getPatientTblId() {
        return PatientTblId;
    }

    public String getBpUp() {
        return BpUp;
    }

    public String getBpDown() {
        return BpDown;
    }

    public String getSpo2() {
        return Spo2;
    }

    public String getGlBfr() {
        return GlBfr;
    }

    public String getGlAftr() {
        return GlAftr;
    }

    public String getTmpr() {
        return Tmpr;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
