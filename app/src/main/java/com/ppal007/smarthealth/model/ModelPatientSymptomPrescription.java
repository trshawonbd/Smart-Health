package com.ppal007.smarthealth.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelPatientSymptomPrescription {

    @SerializedName("id")
    @Expose
    private String Id;

    @SerializedName("symptom_tbl_id")
    @Expose
    private String SymptomTblId;

    @SerializedName("prescription")
    @Expose
    private String Prescription;

    @SerializedName("pres_date")
    @Expose
    private String PresDate;

    @SerializedName("pres_time")
    @Expose
    private String PresTime;

    @SerializedName("pres_year_month")
    @Expose
    private String PresYearMonth;

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    //constructor
    public ModelPatientSymptomPrescription(String id, String symptomTblId, String prescription, String presDate, String presTime) {
        Id = id;
        SymptomTblId = symptomTblId;
        Prescription = prescription;
        PresDate = presDate;
        PresTime = presTime;
    }

    public String getId() {
        return Id;
    }

    public String getSymptomTblId() {
        return SymptomTblId;
    }

    public String getPrescription() {
        return Prescription;
    }

    public String getPresDate() {
        return PresDate;
    }

    public String getPresTime() {
        return PresTime;
    }

    public String getPresYearMonth() {
        return PresYearMonth;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
