package com.ppal007.smarthealth.activity.message.model;

/**
 * Created by ppal on 20-Feb-21.
 */
public class User {

    private String id;
    private String useremail;
    private String userid;
    private String username;
    private String usermode;

    public User(String id, String useremail, String userid, String username, String usermode) {
        this.id = id;
        this.useremail = useremail;
        this.userid = userid;
        this.username = username;
        this.usermode = usermode;
    }

    public User() {

    }

    public String getId() {
        return id;
    }

    public String getUseremail() {
        return useremail;
    }

    public String getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }

    public String getUsermode() {
        return usermode;
    }
}
