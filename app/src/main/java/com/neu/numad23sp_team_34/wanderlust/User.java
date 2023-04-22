package com.neu.numad23sp_team_34.wanderlust;

public class User {

    private String wanderLust_username;
    private String wanderLust_email;
    private String wanderLust_password;

    public User(String WanderLust_username, String WanderLust_email, String WanderLust_password ){
        this.wanderLust_username = WanderLust_username;
        this.wanderLust_email =WanderLust_email;
        this.wanderLust_password =WanderLust_password;
    }

    public String getWanderLust_username() {
        return wanderLust_username;
    }

    public void setWanderLust_username(String wanderLust_username) {
        this.wanderLust_username = wanderLust_username;
    }

    public String getWanderLust_email() {
        return wanderLust_email;
    }

    public void setWanderLust_email(String wanderLust_email) {
        this.wanderLust_email = wanderLust_email;
    }

    public String getWanderLust_password() {
        return wanderLust_password;
    }

    public void setWanderLust_password(String wanderLust_password) {
        this.wanderLust_password = wanderLust_password;
    }
}
