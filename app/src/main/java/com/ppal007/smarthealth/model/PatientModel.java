package com.ppal007.smarthealth.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ppal on 06-Apr-21.
 */
public class PatientModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("id_number")
    @Expose
    private String idNumber;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("pass")
    @Expose
    private String pass;
    @SerializedName("profile_img_path")
    @Expose
    private String profileImgPath;
    @SerializedName("doctor_id")
    @Expose
    private String doctorId;
    @SerializedName("request")
    @Expose
    private String request;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getPass() {
        return pass;
    }

    public String getProfileImgPath() {
        return profileImgPath;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getRequest() {
        return request;
    }
}
