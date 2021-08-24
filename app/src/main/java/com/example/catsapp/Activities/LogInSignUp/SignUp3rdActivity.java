package com.example.catsapp.Activities.LogInSignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.catsapp.Activities.StartUpActivity;
import com.example.catsapp.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

public class SignUp3rdActivity extends AppCompatActivity {

    ScrollView scrollView;
    TextInputLayout phoneNumber;
    CountryCodePicker countryCodePicker;
    Button next, login;
    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up3rd);

        next = findViewById(R.id.signUp_next_button);
        login = findViewById(R.id.signUp_login_button);
        titleText = findViewById(R.id.signUp_text);
        countryCodePicker = findViewById(R.id.country_code_picker);
        phoneNumber = findViewById(R.id.phone_number);
        scrollView = findViewById(R.id.signup_3rd_screen_scroll_view);
    }

    private boolean validatePhuneNumber()
    {
        String _phoneNumber = phoneNumber.getEditText().getText().toString().trim();

        if (_phoneNumber.charAt(0) == '0') {
            _phoneNumber = _phoneNumber.substring(1);
        }

        final String _completePhoneNumber = countryCodePicker.getSelectedCountryCodeWithPlus() + _phoneNumber;
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNo").equalTo(_completePhoneNumber);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    phoneNumber.setError(null);
                    phoneNumber.setErrorEnabled(false);

                    Intent intent = new Intent(SignUp3rdActivity.this, MakeSelectionActivity.class);
                    intent.putExtra("phoneNo", _completePhoneNumber);
                    intent.putExtra("whatToDo", "updateData");
                    startActivity(intent);
                    finish();

                } else {
                    phoneNumber.setError("No such user exist!");
                    phoneNumber.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp3rdActivity.this);

                builder.setMessage("This phone does not belong to any user  ")
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
        });
        return true;
    }

    public void callVerifyOTPScreen(View view)
    {
        if(!validatePhuneNumber())
        {
            return ;
        }

        String _fullName = getIntent().getStringExtra("fullName");
        String _email = getIntent().getStringExtra("email");
        String _username = getIntent().getStringExtra("userName");
        String _password = getIntent().getStringExtra("password");
        String _date = getIntent().getStringExtra("date");
        String _gender = getIntent().getStringExtra("gender");
        String _getUserEnteredPhoneNumber = phoneNumber.getEditText().getText().toString().trim();
        String _phoneNo = "+" + countryCodePicker.getFullNumber() + _getUserEnteredPhoneNumber;


        Intent intent = new Intent(getApplicationContext(), VerifyOTPActivity.class);

        intent.putExtra("fullName", _fullName);
        intent.putExtra("userName", _username);
        intent.putExtra("email", _email);
        intent.putExtra("password", _password);
        intent.putExtra("date", _date);
        intent.putExtra("gender", _gender);
        intent.putExtra("phoneNo", _phoneNo);

        Pair[] pairs = new Pair[1];

        pairs[0] = new Pair<View,String>(next, "transition_next_button");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp3rdActivity.this, pairs);

            startActivity(intent, options.toBundle());
        }
        else
        {
            startActivity(intent);
        }
    }

    public void callLoginScreen(View view)
    {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        Pair[] pairs = new Pair[3];

        pairs[0] = new Pair<View,String>(next, "transition_next_button");
        pairs[1] = new Pair<View,String>(login, "transition_login_button");
        pairs[2] = new Pair<View,String>(titleText, "transition_text");


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp3rdActivity.this, pairs);

            startActivity(intent, options.toBundle());
        }
        else
        {
            startActivity(intent);
        }
    }
}