package com.ppal007.smarthealth.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelPrescriptionMsg {
    @SerializedName("id")
    @Expose
    private String Id;

    @SerializedName("msg_tbl_id")
    @Expose
    private String MsgTblId;

    @SerializedName("patient_id")
    @Expose
    private String PatientId;

    @SerializedName("doctor_id")
    @Expose
    private String DoctorId;

    @SerializedName("prescription")
    @Expose
    private String Prescription;

    @SerializedName("pres_date")
    @Expose
    private String PresDate;

    @SerializedName("pres_time")
    @Expose
    private String PresTime;

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

//    constructor


    public ModelPrescriptionMsg(String id, String prescription) {
        Id = id;
        Prescription = prescription;
    }

    public String getId() {
        return Id;
    }

    public String getMsgTblId() {
        return MsgTblId;
    }

    public String getPatientId() {
        return PatientId;
    }

    public String getDoctorId() {
        return DoctorId;
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

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
