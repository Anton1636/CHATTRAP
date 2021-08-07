package com.example.chattrap.Activities.LogInSignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class ForgetPasswordActivity extends AppCompatActivity {

    private ImageView screenIcon;
    private TextView title, description;
    private TextInputLayout phoneNumberTextField;
    private CountryCodePicker countryCodePicker;
    private Button nextButton;
    RelativeLayout progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_forget_password);


        screenIcon = findViewById(R.id.forget_password_icon);
        title = findViewById(R.id.forget_password_title);
        description = findViewById(R.id.forget_password_description);
        phoneNumberTextField = findViewById(R.id.forget_password_phone_number);
        countryCodePicker = findViewById(R.id.country_code_picker);
        nextButton = findViewById(R.id.forget_password_next_button);
        progressBar = findViewById(R.id.forget_password_progress_bar);

    }

    public void verifyPhoneNumber(View view)
    {

        CheckInternet checkInternet = new CheckInternet();

        if(!checkInternet.isConnected(this))
        {
            showCustomDialog();
            return;
        }

        if (!validateFields())
        {
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        String _phoneNumber = phoneNumberTextField.getEditText().getText().toString().trim();

        if (_phoneNumber.charAt(0) == '0') {
            _phoneNumber = _phoneNumber.substring(1);
        }

        final String _completePhoneNumber = "+" + countryCodePicker.getFullNumber() + _phoneNumber;
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNo").equalTo(_completePhoneNumber);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    phoneNumberTextField.setError(null);
                    phoneNumberTextField.setErrorEnabled(false);

                    Intent intent = new Intent(getApplicationContext(), VerifyOTPActivity.class);
                    intent.putExtra("phoneNo", _completePhoneNumber);
                    intent.putExtra("whatToDo", "updateData");
                    startActivity(intent);
                    finish();

                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    phoneNumberTextField.setError("No such user exist!");
                    phoneNumberTextField.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean validateFields()
    {
        if(phoneNumberTextField.isEmpty())
        {
            phoneNumberTextField.setError("Phone number can't be empty");
            phoneNumberTextField.requestFocus();
            return false;
        }
        else
        {
            return true;
        }
    }

    private void showCustomDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPasswordActivity.this);

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
}