package com.example.catsapp.Activities.LogInSignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.catsapp.Helpers.DataStatic;
import com.example.catsapp.Models.User;
import com.example.catsapp.R;
import com.example.catsapp.Secvice.NetworkService;
import com.google.android.material.textfield.TextInputLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity
{
    Button next, login;
    TextView titleText;
    TextInputLayout fullName, username, email, password;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        next = findViewById(R.id.signUp_next_button);
        login = findViewById(R.id.signUp_login_button);
        titleText = findViewById(R.id.signUp_text);
        fullName = findViewById(R.id.signup_fullname);
        username = findViewById(R.id.signup_username);
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
    }

    public void callNextSignUpScreen(View view)
    {
        if(!validateFullName() | !validateUserName() | !validateEmail() | !validatePassword())
        {
            return;
        }

        Intent intent = new Intent(getApplicationContext(), SignUp2ndActivity.class);

        Pair[] pairs = new Pair[3];

        pairs[0] = new Pair<View,String>(next, "transition_next_button");
        pairs[1] = new Pair<View,String>(login, "transition_login_button");
        pairs[2] = new Pair<View,String>(titleText, "transition_text");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {

            DataStatic.fullName = fullName.getEditText().getText().toString();
            DataStatic.username = username.getEditText().getText().toString();
            DataStatic.email = email.getEditText().getText().toString();
            DataStatic.password = password.getEditText().getText().toString();

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this, pairs);

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
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this, pairs);

            startActivity(intent, options.toBundle());
        }
        else
        {
            startActivity(intent);
        }
    }

    private boolean validateFullName()
    {
        String validation = fullName.getEditText().getText().toString().trim();

        if(validation.isEmpty())
        {
            fullName.setError("Field can not be empty");
            return false;
        }
        else
        {
            fullName.setError(null);
            return true;
        }
    }

    private boolean validateUserName()
    {
        String validation = username.getEditText().getText().toString().trim();

        if(validation.isEmpty())
        {
            username.setError("Field can not be empty");
            return false;
        }
        else if(validation.length() > 20)
        {
            username.setError("Username is too large!");
            return false;
        }
        else
        {
            username.setError(null);
            return true;
        }
    }

    private boolean validateEmail()
    {
        String validation = email.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9] + @[a-z] + \\. + [a-z] +";

        if(validation.isEmpty())
        {
            email.setError("Field can not be empty");
            return false;
        }
        else if(validation.matches(checkEmail))
        {
            email.setError("Invalid Email!");
            return false;
        }
        else
        {
            email.setError(null);
            return true;
        }
    }

    private boolean validatePassword()
    {
        String validation = password.getEditText().getText().toString().trim();
        String checkPassword = "^" + "(?=.*[0-9])" + "(?=.*[a-z])" + "(?=.*[A-Z])" + "(?=.*[a-zA-Z])" + "(?=.*[@#$%^&+=])" + "(?=.*\\S+$)" + ".{6,}" + "$";

        if(validation.isEmpty())
        {
            password.setError("Password shoud contain 6 characters!");
            return false;
        }
        else if(validation.matches(checkPassword))
        {
            password.setError("Invalid Password!");
            return true;
        }
        else
        {
            password.setError(null);
            return true;
        }
    }
}