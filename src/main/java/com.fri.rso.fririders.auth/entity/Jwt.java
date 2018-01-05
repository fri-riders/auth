package com.fri.rso.fririders.auth.entity;

import java.io.Serializable;

public class Jwt implements Serializable {

    private String email;

    private String token;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
