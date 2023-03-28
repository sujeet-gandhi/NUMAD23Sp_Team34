package com.neu.numad23sp_team_34.WanderLust;

public class Users {

    private String WanderLust_username;
    private String WanderLust_email;
    private String WanderLust_password;


//    public Users(){
//    }
    public Users(String WanderLust_username,String WanderLust_email,String WanderLust_password ){
        this.WanderLust_username = WanderLust_username;
        this.WanderLust_email=WanderLust_email;
        this.WanderLust_password=WanderLust_password;
    }


    public String getWanderLust_username() {
        return WanderLust_username;
    }

    public void setWanderLust_username(String wanderLust_username) {
        WanderLust_username = wanderLust_username;
    }

    public String getWanderLust_email() {
        return WanderLust_email;
    }

    public void setWanderLust_email(String wanderLust_email) {
        WanderLust_email = wanderLust_email;
    }

    public String getWanderLust_password() {
        return WanderLust_password;
    }

    public void setWanderLust_password(String wanderLust_password) {
        WanderLust_password = wanderLust_password;
    }
}
