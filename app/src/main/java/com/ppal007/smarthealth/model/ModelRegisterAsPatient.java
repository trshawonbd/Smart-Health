package com.ppal007.smarthealth.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelRegisterAsPatient {

    @SerializedName("id")
    @Expose
    private String Id;

    @SerializedName("name")
    @Expose
    private String Name;

    @SerializedName("last_ins_id")
    @Expose
    private String LastInsId;

    @SerializedName("email")
    @Expose
    private String Email;

    @SerializedName("phone")
    @Expose
    private String Phone;

    @SerializedName("id_number")
    @Expose
    private String IdNumber;

    @SerializedName("age")
    @Expose
    private String Age;

    @SerializedName("gender")
    @Expose
    private String Gender;

    @SerializedName("pass")
    @Expose
    private String Pass;

    @SerializedName("doctor_id")
    @Expose
    private String DoctorId;

    @SerializedName("profile_img_path")
    @Expose
    private String ProfileImgPath;

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

    public String getLastInsId() {
        return LastInsId;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getPhone() {
        return Phone;
    }
}
