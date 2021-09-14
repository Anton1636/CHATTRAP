package com.example.catsapp.Secvice;

import com.example.catsapp.Helpers.JSONPlaceHolderApi;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService
{
    private static NetworkService mInstance;
    //private static final String BASE_URL = "http://192.168.137.21:6937";
    private static final String BASE_URL = "http://10.0.2.2:6937";
    private Retrofit mRetrofit;

    private NetworkService()
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder().addInterceptor(interceptor);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public static NetworkService getInstance()
    {
        if (mInstance == null)
        {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public JSONPlaceHolderApi getJSONApi()
    {
        return mRetrofit.create(JSONPlaceHolderApi.class);
    }
}


