package com.ppal007.smarthealth.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelPatientSymptoms {
    @SerializedName("id")
    @Expose
    private String Id;

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

    @SerializedName("ecg")
    @Expose
    private String Ecg;

    @SerializedName("gl_befr_eat")
    @Expose
    private String GlBfrEat;

    @SerializedName("gl_aftr_eat")
    @Expose
    private String GlAftrEat;

    @SerializedName("temperature")
    @Expose
    private String Temperature;

    @SerializedName("submit_date")
    @Expose
    private String SubMitDate;

    @SerializedName("submit_time")
    @Expose
    private String SubmitTime;

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    public ModelPatientSymptoms(String id, String bpUp, String bpDown, String spo2, String ecg, String glBfrEat, String glAftrEat, String temperature, String submitdate, String submitTime) {
        Id = id;
        BpUp = bpUp;
        BpDown = bpDown;
        Spo2 = spo2;
        Ecg = ecg;
        GlBfrEat = glBfrEat;
        GlAftrEat = glAftrEat;
        Temperature = temperature;
        SubMitDate = submitdate;
        SubmitTime = submitTime;
    }

    public String getId() {
        return Id;
    }

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

    public String getEcg() {
        return Ecg;
    }

    public String getGlBfrEat() {
        return GlBfrEat;
    }

    public String getGlAftrEat() {
        return GlAftrEat;
    }

    public String getTemperature() {
        return Temperature;
    }

    public String getSubMitDate() {
        return SubMitDate;
    }

    public String getSubmitTime() {
        return SubmitTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
