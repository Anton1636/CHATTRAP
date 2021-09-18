package com.example.catsapp.Helpers;

import com.example.catsapp.Models.LoginDto;
import com.example.catsapp.Models.LoginResultDto;
import com.example.catsapp.Models.Message;
import com.example.catsapp.Models.MessageResultDto;
import com.example.catsapp.Models.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JSONPlaceHolderApi
{
    @GET("/api/Account/registration/{id}")
    public Call<User> getUserWithID(@Path("id") int id);

    @GET("/api/Test")
    public Call<List<User>> getAllUsers();

    @POST("/api/Account/registration")
    public Call<LoginResultDto> userData(@Body User data);

    @POST("/api/account/login")
    public Call<LoginResultDto> login(@Body LoginDto loginDto);


    @POST("/api/Messages/setMessage")
    public Call<MessageResultDto> sendMessage(@Body Message messageResultDto);
}
