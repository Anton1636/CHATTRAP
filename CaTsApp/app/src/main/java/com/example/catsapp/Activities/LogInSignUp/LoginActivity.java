package com.example.catsapp.Activities.LogInSignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.catsapp.Activities.MainActivity;
import com.example.catsapp.Activities.StartUpActivity;
import com.example.catsapp.Helpers.CheckInternet;
import com.example.catsapp.Helpers.SessionManager;
import com.example.catsapp.Models.LoginDto;
import com.example.catsapp.Models.LoginResultDto;
import com.example.catsapp.Models.User;
import com.example.catsapp.R;
import com.example.catsapp.Secvice.NetworkService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity
{
    TextInputLayout phoneNumber, password;
    CountryCodePicker countryCodePicker;
    RelativeLayout progressbar;
    CheckBox rememberMe;
    TextInputEditText phoneNumberEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        countryCodePicker = findViewById(R.id.login_country_code_picker);
        phoneNumber = findViewById(R.id.login_phone_number);
        password = findViewById(R.id.login_password);
        progressbar = findViewById(R.id.login_progress_bar);
        rememberMe = findViewById(R.id.remember_me);
        phoneNumberEditText = findViewById(R.id.login_phone_number_editText);
        passwordEditText = findViewById(R.id.login_password_editText);

        //Check weather phone and pass is saved in Shared Preferences
        SessionManager sessionManager = new SessionManager(LoginActivity.this, SessionManager.SESSION_REMEMBERME);

        if (sessionManager.checkRememberMe())
        {
            HashMap<String, String> rememberMeDetails = sessionManager.getRememberMeDetailFromSession();
            passwordEditText.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONPHONENUMBER));
            passwordEditText.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONPASSWORD));
        }
    }

    public void letTheUserLoggedIn(View view)
    {
        CheckInternet checkInternet = new CheckInternet();

        if (!checkInternet.isConnected(this))
        {
            showCustomDialog();
            return;
        }


        if (!validateFields())
        {
            return;
        }

        progressbar.setVisibility(View.VISIBLE);

        String _phoneNumber = phoneNumber.getEditText().getText().toString().trim();
        String _password = password.getEditText().getText().toString().trim();

        if (_phoneNumber.charAt(0) == '0') {
            _phoneNumber = _phoneNumber.substring(1);
        }

        final String _completePhoneNumber = countryCodePicker.getSelectedCountryCodeWithPlus() + _phoneNumber;

        if (rememberMe.isChecked())
        {
            SessionManager sessionManager = new SessionManager(LoginActivity.this, SessionManager.SESSION_REMEMBERME);

            sessionManager.createRememberMeSession(_phoneNumber, _password);
        }

        LoginDto loginDto = new LoginDto(
                _completePhoneNumber,
                password.getEditText().getText().toString()
        );

        NetworkService.getInstance()
                      .getJSONApi()
                      .login(loginDto)
                      .enqueue(new Callback<LoginResultDto>()
                      {
                          @Override
                          public void onResponse(Call<LoginResultDto> call, Response<LoginResultDto> response)
                          {
                              if (response.isSuccessful())
                              {
                                  //LoginResultDto result = response.body();
                                  //JwtSecurityService jwtService = (JwtSecurityService) HomeApplication.getInstance();

                                  //jwtService.saveJwtToken(result.getToken());

                                  Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                  startActivity(intent);
                              }
                              else {
                                  Toast.makeText(LoginActivity.this, "No such user exist!", Toast.LENGTH_SHORT).show();
                              }
                          }

                          @Override
                          public void onFailure(Call<LoginResultDto> call, Throwable t)
                          {
                              Toast.makeText(LoginActivity.this, "No such user exist!", Toast.LENGTH_SHORT).show();
                              t.printStackTrace();
                          }
                      });
    }

    private void showCustomDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

        builder.setMessage("Please connect to the internet to proceed further")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        startActivity(new Intent(getApplicationContext(), StartUpActivity.class));
                        finish();
                    }
                });
    }

    private boolean validateFields()
    {
        String _phoneNumber = phoneNumber.getEditText().getText().toString().trim();
        String _password = password.getEditText().getText().toString().trim();

        if (!_phoneNumber.isEmpty())
        {
            _phoneNumber = phoneNumber.getEditText().getText().toString().trim();

            if (_phoneNumber.charAt(0) == '0')
            {
                _phoneNumber = _phoneNumber.substring(1);
            }

            final String _completePhoneNumber = countryCodePicker.getSelectedCountryCodeWithPlus() + _phoneNumber;
           // Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNo").equalTo(_completePhoneNumber);
           // DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
            User user = new User();

//            checkUser.addListenerForSingleValueEvent(new ValueEventListener()
//            {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot)
//                {
//                   // DataSnapshot peopleNumber = snapshot.child("Users").getChildren();//.child(user.getId()).child("phoneNo").getValue(Integer.class);
//
//                    for (DataSnapshot postSnapshot: snapshot.getChildren())
//                    {
//                        // TODO: handle the post
//                        Object data= postSnapshot.getValue();
//                    }
////                    if (_completePhoneNumber == String.valueOf(peopleNumber))
////                    {
////                        phoneNumber.setError(null);
////                        phoneNumber.setErrorEnabled(false);
////
////                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
////                        intent.putExtra("phoneNo", _completePhoneNumber);
////                        intent.putExtra("whatToDo", "updateData");
////                        startActivity(intent);
////                        finish();
////                    }
////                    else {
////                        phoneNumber.setError("No such user exist!");
////                        phoneNumber.requestFocus();
////                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error)
//                {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//
//                    builder.setMessage("This phone does not belong to any user  ").setNegativeButton("Cancel", new DialogInterface.OnClickListener()
//                            {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which)
//                                {
//                                    startActivity(new Intent(getApplicationContext(), StartUpActivity.class));
//
//                                    finish();
//                                }
//                            });
//                }
//            });

            return true;
        }
        else if (_password.isEmpty())
        {
            password.setError("Password number can't be empty");
            password.requestFocus();
            return false;
        }
        return true;
    }

    public void callSignUpScreen(View view)
    {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        Pair[] pairs = new Pair[1];

        pairs[0] = new Pair<View,String>(findViewById(R.id.signUp_button), "transition_setUp");


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);

            startActivity(intent, options.toBundle());
        }
        else
        {
            startActivity(intent);
        }
    }

    public void callForgetPassword(View view)
    {
        startActivity(new Intent(getApplicationContext(), ForgetPasswordActivity.class));
    }
}