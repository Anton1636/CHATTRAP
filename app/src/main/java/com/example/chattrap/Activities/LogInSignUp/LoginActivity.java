package com.example.chattrap.Activities.LogInSignUp;

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
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chattrap.Activities.StartUpActivity;
import com.example.chattrap.Helpers.CheckInternet;
import com.example.chattrap.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class LoginActivity extends AppCompatActivity
{
    TextInputLayout phoneNumber, password;
    CountryCodePicker countryCodePicker;
    RelativeLayout progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        countryCodePicker = findViewById(R.id.login_country_code_picker);
        phoneNumber = findViewById(R.id.login_phone_number);
        password = findViewById(R.id.login_password);
        progressbar = findViewById(R.id.login_progress_bar);
    }

    public void letTheUserLoggedIn(View view)
    {
        CheckInternet checkInternet = new CheckInternet();

        if(!checkInternet.isConnected(this))
        {
            showCustomDialog();
            return;
        }


        if(!validateFields())
        {
            return;
        }

        progressbar.setVisibility(View.VISIBLE);


        String _phoneNumber = phoneNumber.getEditText().getText().toString().trim();
        String _password = password.getEditText().getText().toString().trim();

        if(_phoneNumber.charAt(0) == '0')
        {
            _phoneNumber = _phoneNumber.substring(1);
        }

        final String _completePhoneNumber = "+" + countryCodePicker.getFullNumber() + _phoneNumber;
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNo").equalTo(_completePhoneNumber);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    phoneNumber.setError(null);
                    phoneNumber.setErrorEnabled(false);

                    String systemPassword = snapshot.child(_completePhoneNumber).child("password").getValue(String.class);

                    if(systemPassword.equals(_password))
                    {
                        password.setError(null);
                        password.setErrorEnabled(false);

                        String _fullname = snapshot.child(_completePhoneNumber).child("fullName").getValue(String.class);
                        String _email = snapshot.child(_completePhoneNumber).child("email").getValue(String.class);
                        String _phoneNo = snapshot.child(_completePhoneNumber).child("phoneNo").getValue(String.class);
                        String _dateOfBirth = snapshot.child(_completePhoneNumber).child("date").getValue(String.class);

                        Toast.makeText(LoginActivity.this, _fullname + "\n" + _email + "\n" + _phoneNo + "\n" + _dateOfBirth, Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        progressbar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Password doesn't match!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "No such user exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                progressbar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCustomDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

        builder.setMessage("Please connect to the internet to proceed further")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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

        if(_phoneNumber.isEmpty())
        {
            phoneNumber.setError("Phone number can't be empty");
            phoneNumber.requestFocus();
            return false;
        }
        else if(_password.isEmpty())
        {
            password.setError("Password number can't be empty");
            password.requestFocus();
            return false;
        }
        else
        {
            return true;
        }
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
