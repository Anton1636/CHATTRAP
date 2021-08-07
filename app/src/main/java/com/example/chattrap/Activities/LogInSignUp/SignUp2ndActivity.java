package com.example.chattrap.Activities.LogInSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chattrap.R;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class SignUp2ndActivity extends AppCompatActivity {

    Button next, login;
    TextView titleText;
    DatePicker datePicker;
    List<String> genders = Arrays.asList("Not Important", "Male", "Female", "Agender", "Androgyne", "Androgynous", "Bigender", "Cis", "Cis Female", "Cis Male", "Cis Man", "Cis Woman", "Cisgender", "Cisgender Female", "Cisgender Male", "Cisgender Man", "Cisgender Woman", "Female to Male", "FTM", "Gender Fluid", "Gender Nonconforming", "Gender Questioning", "Gender Variant", "Genderqueer", "Intersex", "Male to Female", "MTF", "Neither", "Neutrois", "Non-binary", "Other", "Pangender", "Trans", "Trans Female", "Trans Male", "Trans Man", "Trans Person", "Trans Woman", "Trans*", "Trans* Female", "Trans* Male", "Trans* Man", "Trans* Person", "Trans* Woman", "Transfeminine", "Transgender", "Transgender Female", "Transgender Male", "Transgender Man", "Transgender Person", "Transgender Woman", "Transmasculine", "Transsexual", "Transsexual Female", "Transsexual Male", "Transsexual Man", "Transsexual Person", "Transsexual Woman", "Two spirit");
    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.selected_item, genders);
    Spinner spinner = findViewById(R.id.spinner);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up2nd);

        adapter.setDropDownViewResource(R.layout.dropdown_item);
        spinner.setAdapter(adapter);

        next = findViewById(R.id.signUp_next_button);
        login = findViewById(R.id.signUp_login_button);
        titleText = findViewById(R.id.signUp_text);
        datePicker = findViewById(R.id.age_picker);

    }

    public void callNextSignUpScreen(View view)
    {
        if(!validateAge())
        {
            return;
        }

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getDayOfMonth();
        int year = datePicker.getYear();
        String _date = day + "/" + month + "/" + year;
        String _gender = spinner.getSelectedItem().toString();

        Intent intent = new Intent(getApplicationContext(), SignUp3rdActivity.class);

        intent.putExtra("date", _date);
        intent.putExtra("gender", _gender);

        Pair[] pairs = new Pair[3];

        pairs[0] = new Pair<View,String>(next, "transition_next_button");
        pairs[1] = new Pair<View,String>(login, "transition_login_button");
        pairs[2] = new Pair<View,String>(titleText, "transition_text");

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
        Pair[] pairs = new Pair[3];

        pairs[0] = new Pair<View,String>(next, "transition_next_button");
        pairs[1] = new Pair<View,String>(login, "transition_login_button");
        pairs[2] = new Pair<View,String>(titleText, "transition_text");


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

    private boolean validateAge()
    {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = datePicker.getYear();
        int isAgeValid = currentYear -userAge;

        if(isAgeValid < 14)
        {
            Toast.makeText(this, "You are not eligible to apply", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }
    }
}