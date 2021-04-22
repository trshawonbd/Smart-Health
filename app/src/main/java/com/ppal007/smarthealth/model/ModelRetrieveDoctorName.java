package com.ppal007.smarthealth.model;

import com.google.gson.annotations.SerializedName;

public class ModelRetrieveDoctorName {

    @SerializedName("id")
    private String Id;

    @SerializedName("name")
    private String Name;

    @SerializedName("location")
    private String Location;

    public ModelRetrieveDoctorName(String id, String name) {
        Id = id;
        Name = name;
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }
}
