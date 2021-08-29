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
import com.example.catsapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

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


        //Get values from fields
        String _phoneNumber = phoneNumber.getEditText().getText().toString().trim();
        String _password = password.getEditText().getText().toString().trim();

        if (_phoneNumber.charAt(0) == '0') {
            _phoneNumber = _phoneNumber.substring(1);
        }

        final String _completePhoneNumber = "+" + countryCodePicker.getSelectedCountryCodeWithPlus() + _phoneNumber;

        if (rememberMe.isChecked())
        {
            SessionManager sessionManager = new SessionManager(LoginActivity.this, SessionManager.SESSION_REMEMBERME);

            sessionManager.createRememberMeSession(_phoneNumber, _password);
        }


        //Check weather User exists or not in db
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNo").equalTo(_completePhoneNumber);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    phoneNumber.setError(null);
                    phoneNumber.setErrorEnabled(false);

                    String systemPassword = snapshot.child(_completePhoneNumber).child("password").getValue(String.class);

                    if (systemPassword.equals(_password))
                    {
                        password.setError(null);
                        password.setErrorEnabled(false);

                        //Get users data from db
                        String _fullname = snapshot.child(_completePhoneNumber).child("fullName").getValue(String.class);
                        String _username = snapshot.child(_completePhoneNumber).child("username").getValue(String.class);
                        String _email = snapshot.child(_completePhoneNumber).child("email").getValue(String.class);
                        String _phoneNo = snapshot.child(_completePhoneNumber).child("phoneNo").getValue(String.class);
                        String _password = snapshot.child(_completePhoneNumber).child("password").getValue(String.class);
                        String _dateOfBirth = snapshot.child(_completePhoneNumber).child("date").getValue(String.class);
                        String _gender = snapshot.child(_completePhoneNumber).child("gender").getValue(String.class);

                        //Create a Session
                        SessionManager sessionManager = new SessionManager(LoginActivity.this, SessionManager.SESSION_USERSESSION);

                        sessionManager.createLoginSession(_fullname, _username, _email, _phoneNo, _password, _dateOfBirth, _gender);

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));


                        Toast.makeText(LoginActivity.this, _fullname + "\n" + _email + "\n" + _phoneNo + "\n" + _dateOfBirth, Toast.LENGTH_SHORT).show();
                        progressbar.setVisibility(View.GONE);

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
            Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNo").equalTo(_completePhoneNumber);

            checkUser.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    if (snapshot.exists())
                    {
                        phoneNumber.setError(null);
                        phoneNumber.setErrorEnabled(false);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("phoneNo", _completePhoneNumber);
                        intent.putExtra("whatToDo", "updateData");
                        startActivity(intent);
                        finish();

                    }
                    else
                        {
                        phoneNumber.setError("No such user exist!");
                        phoneNumber.requestFocus();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

                    builder.setMessage("This phone does not belong to any user  ")
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(getApplicationContext(), StartUpActivity.class));
                                    finish();
                                }
                            });
                }
            });
            return true;
        } else if (_password.isEmpty()) {
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