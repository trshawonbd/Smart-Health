package com.ppal007.smarthealth.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ppal on 18-Apr-21.
 */
public class RegiDoctor {
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

    public String getLastInsId() {
        return LastInsId;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
