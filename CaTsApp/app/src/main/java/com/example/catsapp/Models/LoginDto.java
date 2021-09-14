package com.example.catsapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginDto {
    @SerializedName("phoneNo")
    @Expose
    private String phoneNo;

    @SerializedName("password")
    @Expose
    private String password;

    public LoginDto() {
    }

    public LoginDto(String phoneNo, String password) {
        this.phoneNo = phoneNo;
        this.password = password;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String email) {
        this.phoneNo = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
