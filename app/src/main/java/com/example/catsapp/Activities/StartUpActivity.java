package com.example.catsapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;

import com.example.catsapp.Activities.LogInSignUp.LoginActivity;
import com.example.catsapp.Activities.LogInSignUp.SignUpActivity;
import com.example.catsapp.R;

public class StartUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start_up);
    }

    public void callLoginScreen(View view)
    {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        Pair[] pairs = new Pair[1];

        pairs[0] = new Pair<View,String>(findViewById(R.id.logIn_button), "transition_login");


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StartUpActivity.this, pairs);

            startActivity(intent, options.toBundle());
        }
        else
        {
            startActivity(intent);
        }
    }

    public void callSignUpScreen(View view)
    {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        Pair[] pairs = new Pair[1];

        pairs[0] = new Pair<View,String>(findViewById(R.id.signUp_button),"transition_signUp");


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StartUpActivity.this, pairs);

            startActivity(intent, options.toBundle());
        }
        else
        {
            startActivity(intent);
        }
    }
}