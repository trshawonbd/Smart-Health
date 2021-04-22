package com.ppal007.smarthealth.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelMessage {

    @SerializedName("id")
    @Expose
    private String Id;

    @SerializedName("notify_id")
    @Expose
    private String NotifyId;

    @SerializedName("patient_id")
    @Expose
    private String PatientId;

    @SerializedName("doctor_id")
    @Expose
    private String DoctorId;

    @SerializedName("msg")
    @Expose
    private String Msg;

    @SerializedName("msg_date")
    @Expose
    private String MsgDate;

    @SerializedName("msg_time")
    @Expose
    private String MsgTime;

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    public ModelMessage(String id, String patientId, String doctorId, String msg, String msgDate, String msgTime) {
        Id = id;
        PatientId = patientId;
        DoctorId = doctorId;
        Msg = msg;
        MsgDate = msgDate;
        MsgTime = msgTime;
    }

    public String getId() {
        return Id;
    }

    public String getNotifyId() {
        return NotifyId;
    }

    public String getPatientId() {
        return PatientId;
    }

    public String getDoctorId() {
        return DoctorId;
    }

    public String getMsg() {
        return Msg;
    }

    public String getMsgDate() {
        return MsgDate;
    }

    public String getMsgTime() {
        return MsgTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
