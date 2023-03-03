package com.neu.numad23sp_team_34.sticktoem.models;

public class User {
    private String username;
    private String name;
    private String email;

    public User() {}

    public User(String username, String name, String email) {
        this.username = username;
        this.name = name;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}

