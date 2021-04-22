package com.ppal007.smarthealth.adapter.sharedPref;

import android.content.Context;
import android.content.SharedPreferences;

import com.ppal007.smarthealth.utils.Common;

public class AdapterSharedPref {

    private SharedPreferences sharedPreferences;
    private Context context;

    private String userId,userName,userGender,userAge,userEmail,userPhone,loginMood,profile_img,user_pass;

    public AdapterSharedPref(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Common.LOGIN_SHARED_PREFERENCE,Context.MODE_PRIVATE);
    }

//    getter setter


    public String getUserId() {
        userId = sharedPreferences.getString("userDataId","");
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userDataId",userId);
        editor.apply();
    }

    public String getUser_pass() {
        user_pass = sharedPreferences.getString("userDataPass","");
        return user_pass;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userDataPass",user_pass);
        editor.apply();
    }

    public String getUserName() {
        userName = sharedPreferences.getString("userDataName","");
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userDataName",userName);
        editor.apply();
    }

    public String getUserGender() {
        userGender = sharedPreferences.getString("userDataGender","");
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userDataGender",userGender);
        editor.apply();
    }

    public String getUserAge() {
        userAge = sharedPreferences.getString("userDataAge","");
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userDataAge",userAge);
        editor.apply();
    }

    public String getUserEmail() {
        userEmail = sharedPreferences.getString("userDataEmail","");
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userDataEmail",userEmail);
        editor.apply();
    }

    public String getUserPhone() {
        userPhone = sharedPreferences.getString("userDataPhone","");
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userDataPhone",userPhone);
        editor.apply();
    }

    public String getProfile_img() {
        profile_img = sharedPreferences.getString("userDataImgPath","");
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userDataImgPath",profile_img);
        editor.apply();
    }

    public String getLoginMood() {
        loginMood = sharedPreferences.getString("userDataMode","");
        return loginMood;
    }

    public void setLoginMood(String loginMood) {
        this.loginMood = loginMood;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userDataMode",loginMood);
        editor.apply();
    }

    public void login_status(boolean status){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Common.LOGIN_STATUS_SHARED_PREFERENCE,status);
        editor.apply();
    }
    public boolean read_login_status(){
        boolean status;
        status=sharedPreferences.getBoolean(Common.LOGIN_STATUS_SHARED_PREFERENCE, false);
        return status;
    }

    public void removeUser(){
        sharedPreferences.edit().clear().apply();
    }
}
