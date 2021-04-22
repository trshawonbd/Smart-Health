package com.ppal007.smarthealth.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelPatientRequestList {

    @SerializedName("doctor_id")
    @Expose
    private String DoctorId;

    @SerializedName("id")
    @Expose
    private String Id;

    @SerializedName("name")
    @Expose
    private String Name;

    @SerializedName("email")
    @Expose
    private String Email;

    @SerializedName("phone")
    @Expose
    private String Phone;

    @SerializedName("age")
    @Expose
    private String Age;

    @SerializedName("gender")
    @Expose
    private String Gender;

//    constructor
    public ModelPatientRequestList(String id, String name, String email, String phone, String age, String gender) {
        Id = id;
        Name = name;
        Email = email;
        Phone = phone;
        Age = age;
        Gender = gender;
    }

    public String getId() {
        return Id;
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

    public String getAge() {
        return Age;
    }

    public String getGender() {
        return Gender;
    }
}
