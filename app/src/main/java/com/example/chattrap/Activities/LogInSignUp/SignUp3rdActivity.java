package com.example.chattrap.Activities.LogInSignUp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chattrap.Activities.StartUpActivity;
import com.example.chattrap.R;

public class SignUp3rdActivity extends AppCompatActivity {

    ImageView backButton;
    Button next, login;
    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up3rd);

        backButton = findViewById(R.id.signUp_back_button);
        next = findViewById(R.id.signUp_next_button);
        login = findViewById(R.id.signUp_login_button);
        titleText = findViewById(R.id.signUp_text);
    }

    public void callNextSignUpScreen(View view)
    {
        Intent intent = new Intent(getApplicationContext(), VerifyOTPActivity.class);

        Pair[] pairs = new Pair[4];

        pairs[0] = new Pair<View,String>(backButton, "transition_back_button");
        pairs[1] = new Pair<View,String>(next, "transition_next_button");
        pairs[2] = new Pair<View,String>(login, "transition_login_button");
        pairs[3] = new Pair<View,String>(titleText, "transition_text");

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
        Pair[] pairs = new Pair[4];

        pairs[0] = new Pair<View,String>(backButton, "transition_back_button");
        pairs[1] = new Pair<View,String>(next, "transition_next_button");
        pairs[2] = new Pair<View,String>(login, "transition_login_button");
        pairs[3] = new Pair<View,String>(titleText, "transition_text");


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
