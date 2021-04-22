package com.ppal007.smarthealth.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelRegisterAsDoctor {

    @SerializedName("id")
    @Expose
    private String Id;

    @SerializedName("last_ins_id")
    @Expose
    private String LastInsId;

    @SerializedName("name")
    @Expose
    private String Name;

    @SerializedName("email")
    @Expose
    private String Email;

    @SerializedName("phone")
    @Expose
    private String Phone;

    @SerializedName("pass")
    @Expose
    private String Pass;

    @SerializedName("profile_img_path")
    @Expose
    private String ProfileImgPath;

    @SerializedName("img_path")
    @Expose
    private String ImagePath;

    @SerializedName("location")
    @Expose
    private String Location;

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

    public String getLastInsId() {
        return LastInsId;
    }

    public String getEmail() {
        return Email;
    }

    public String getPhone() {
        return Phone;
    }

    public String getLocation() {
        return Location;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
