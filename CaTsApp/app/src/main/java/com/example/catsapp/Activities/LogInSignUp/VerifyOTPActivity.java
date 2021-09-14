package com.example.catsapp.Activities.LogInSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.catsapp.Activities.MainActivity;
import com.example.catsapp.Helpers.DataStatic;
import com.example.catsapp.Activities.application.HomeApplication;
import com.example.catsapp.Models.LoginResultDto;
import com.example.catsapp.Models.User;
import com.example.catsapp.R;
import com.example.catsapp.Secvice.JwtSecurityService;
import com.example.catsapp.Secvice.NetworkService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class
VerifyOTPActivity extends AppCompatActivity
{
    PinView pinView;
    int codeOTP;
    String fullName = DataStatic.fullName;
    String username = DataStatic.username;
    String email = DataStatic.email;
    String password = DataStatic.password;
    String date, gender, phoneNo, whatToDo;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verify_o_t_p);

        pinView = findViewById(R.id.pin_view);

        date = getIntent().getStringExtra("date");
        gender = getIntent().getStringExtra("gender");
        phoneNo = getIntent().getStringExtra("phoneNo");
        whatToDo = getIntent().getStringExtra("whatToDo");

        sendSms();
    }

    public void sendSms()
    {
        try
        {
            // Construct data
            String apiKey = "apikey =" + "NjQ2MjYyNGQ3Njc0NTI1MzRiNGU0Nzc5Nzg1MzU5NzI=";
            Random random = new Random();

            codeOTP = random.nextInt(999999);

            String message = "&message =" + "Hi" + DataStatic.fullName + "/n Your OTP " + codeOTP;
            String sender = "&sender =" + "CaTsApp";
            String numbers = "&numbers =" + phoneNo;

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.txtlocal.com/send/?").openConnection();
            String data = apiKey + numbers + message + sender;

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));

            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;

            while ((line = rd.readLine()) != null)
            {
                stringBuffer.append(line);
            }

            rd.close();

            Toast.makeText(VerifyOTPActivity.this, "OTP send successfully!", Toast.LENGTH_SHORT).show();
            //return stringBuffer.toString();
        }
        catch (Exception e)
        {
            Toast.makeText(VerifyOTPActivity.this, "Error SMS" + e, Toast.LENGTH_SHORT).show();
            Toast.makeText(VerifyOTPActivity.this, "error!", Toast.LENGTH_SHORT).show();
            //return "Error " + e;
        }
    }

    public void callNextSceenFromOTP(View view) 
    {
        String code = pinView.getText().toString();

//        if (code == "111111")
//        {
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            Pair[] pairs = new Pair[1];
//
//            pairs[0] = new Pair<View, String>(findViewById(R.id.verify_otp), "transition_verify_otp");
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//            {
                user.setFullName(fullName);
                user.setUsername(username);
                user.setEmail(email);
                user.setPassword(password);
                user.setDate(date);
                user.setGender(gender);
                user.setPhoneNo(phoneNo);

                NetworkService.getInstance()
                        .getJSONApi()
                        .userData(user)
                        .enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, Response response)
                            {
                                LoginResultDto result = (LoginResultDto) response.body();
                                JwtSecurityService jwtService = (JwtSecurityService) HomeApplication.getInstance();
                                jwtService.saveJwtToken(result.getToken());
                                Intent intent = new Intent(VerifyOTPActivity.this, MainActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call call, Throwable t)
                            {
                                Toast.makeText(VerifyOTPActivity.this, "Not to day!", Toast.LENGTH_SHORT).show();
                                t.printStackTrace();
                            }
//                            @Override
//                            public void onResponse(Call<LoginResultDto> call, Response<LoginResultDto> response) {
//                                LoginResultDto result = response.body();
//                                JwtSecurityService jwtService = (JwtSecurityService) HomeApplication.getInstance();
//
//                                jwtService.saveJwtToken(result.getToken());
//
//                                Intent intent = new Intent(VerifyOTPActivity.this, MainActivity.class);
//                                startActivity(intent);
//                            }
//
//                            @Override
//                            public void onFailure(Call<LoginResultDto> call, Throwable t) {
//                                Toast.makeText(VerifyOTPActivity.this, "Not to day!", Toast.LENGTH_SHORT).show();
//                                t.printStackTrace();
//                            }
                        });
//            }
//            else {
//                Intent intent2 = new Intent(getApplicationContext(), SignUp3rdActivity.class);
//                startActivity(intent2);
//            }
//        }
    }
}
