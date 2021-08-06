package com.example.chattrap.Activities.LogInSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.chattrap.Activities.StartUpActivity;
import com.example.chattrap.R;

import java.util.Arrays;
import java.util.List;

public class SignUp2ndActivity extends AppCompatActivity {

    ImageView backButton;
    Button next, login;
    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2nd);

        final List<String> genders = Arrays.asList("Not Important", "Male", "Female", "Agender", "Androgyne", "Androgynous", "Bigender", "Cis", "Cis Female", "Cis Male", "Cis Man", "Cis Woman", "Cisgender", "Cisgender Female", "Cisgender Male", "Cisgender Man", "Cisgender Woman", "Female to Male", "FTM", "Gender Fluid", "Gender Nonconforming", "Gender Questioning", "Gender Variant", "Genderqueer", "Intersex", "Male to Female", "MTF", "Neither", "Neutrois", "Non-binary", "Other", "Pangender", "Trans", "Trans Female", "Trans Male", "Trans Man", "Trans Person", "Trans Woman", "Trans*", "Trans* Female", "Trans* Male", "Trans* Man", "Trans* Person", "Trans* Woman", "Transfeminine", "Transgender", "Transgender Female", "Transgender Male", "Transgender Man", "Transgender Person", "Transgender Woman", "Transmasculine", "Transsexual", "Transsexual Female", "Transsexual Male", "Transsexual Man", "Transsexual Person", "Transsexual Woman", "Two spirit");
        final Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.selected_item,genders);

        adapter.setDropDownViewResource(R.layout.dropdown_item);
        spinner.setAdapter(adapter);


        backButton = findViewById(R.id.signUp_back_button);
        next = findViewById(R.id.signUp_next_button);
        login = findViewById(R.id.signUp_login_button);
        titleText = findViewById(R.id.signUp_text);
    }

    public void callNextSignUpScreen(View view)
    {
        Intent intent = new Intent(getApplicationContext(), SignUp3rdActivity.class);

        Pair[] pairs = new Pair[4];

        pairs[0] = new Pair<View,String>(backButton, "transition_back_button");
        pairs[1] = new Pair<View,String>(next, "transition_next_button");
        pairs[2] = new Pair<View,String>(login, "transition_login_button");
        pairs[3] = new Pair<View,String>(titleText, "transition_text");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp2ndActivity.this, pairs);

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
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp2ndActivity.this, pairs);

            startActivity(intent, options.toBundle());
        }
        else
        {
            startActivity(intent);
        }
    }
}