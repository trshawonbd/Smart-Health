package com.ppal007.smarthealth.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelBookingSchedule {
    @SerializedName("id")
    @Expose
    private String Id;

    @SerializedName("schedule_tbl_id")
    @Expose
    private String ScheduleTblId;

    @SerializedName("patient_id")
    @Expose
    private String PatientId;

    @SerializedName("doctor_id")
    @Expose
    private String DoctorId;

    @SerializedName("schedule_boking_time")
    @Expose
    private String ScheduleBokingTime;

    @SerializedName("position")
    @Expose
    private String SchedulePosition;

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    public String getId() {
        return Id;
    }

    public String getScheduleTblId() {
        return ScheduleTblId;
    }

    public String getPatientId() {
        return PatientId;
    }

    public String getDoctorId() {
        return DoctorId;
    }

    public String getScheduleBokingTime() {
        return ScheduleBokingTime;
    }

    public String getSchedulePosition() {
        return SchedulePosition;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
