package com.ppal007.smarthealth.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelLogin {
    @SerializedName("mood")
    @Expose
    private String Mood;

    @SerializedName("id")
    @Expose
    private String Id;

    @SerializedName("name")
    @Expose
    private String Name;

    @SerializedName("phone")
    @Expose
    private String Phone;

    @SerializedName("age")
    @Expose
    private String Age;

    @SerializedName("gender")
    @Expose
    private String Gender;

    @SerializedName("profile_img_path")
    @Expose
    private String ProfileImgPath;

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

    public String getPhone() {
        return Phone;
    }

    public String getAge() {
        return Age;
    }

    public String getGender() {
        return Gender;
    }

    public String getProfileImgPath() {
        return ProfileImgPath;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
