package com.ppal007.smarthealth.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelDoctorSchedule {

    @SerializedName("id")
    @Expose
    private String Id;

    @SerializedName("doctor_name")
    @Expose
    private String Name;

    @SerializedName("doctor_id")
    @Expose
    private String DoctorId;

    @SerializedName("schedule_date")
    @Expose
    private String ScheduleDate;

    @SerializedName("schedule_time_start")
    @Expose
    private String ScheduleTimeStart;

    @SerializedName("schedule_time_end")
    @Expose
    private String ScheduleTimeEnd;

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getDoctorId() {
        return DoctorId;
    }

    public String getScheduleDate() {
        return ScheduleDate;
    }

    public String getScheduleTimeStart() {
        return ScheduleTimeStart;
    }

    public String getScheduleTimeEnd() {
        return ScheduleTimeEnd;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
