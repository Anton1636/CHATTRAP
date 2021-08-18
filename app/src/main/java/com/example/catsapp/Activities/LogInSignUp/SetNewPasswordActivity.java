package com.example.catsapp.Activities.LogInSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.catsapp.Activities.StartUpActivity;
import com.example.catsapp.Helpers.CheckInternet;
import com.example.catsapp.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetNewPasswordActivity extends AppCompatActivity {

    RelativeLayout progressBar;
    TextInputLayout newPassword, confirmNewPassword;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_set_new_password);

        progressBar = findViewById(R.id.new_password_progress_bar);
        newPassword = findViewById(R.id.new_password);
        confirmNewPassword = findViewById(R.id.confirm_new_password);
        update = findViewById(R.id.update_button);
    }

    public void setNewPasswordBtn(View view)
    {
        CheckInternet checkInternet = new CheckInternet();

        if(!checkInternet.isConnected(this))
        {
            showCustomDialog();
            return;
        }

        if(!validatePassword() | !validateConfirmPassword())
        {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        String _newPassword = newPassword.getEditText().getText().toString().trim();
        String _phoneNumber = getIntent().getStringExtra("phoneNo");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.child(_phoneNumber).child("password").setValue(_newPassword);

        startActivity(new Intent(getApplicationContext(), ForgetPasswordSuccessMessageActivity.class));
        finish();
    }

    private boolean validateConfirmPassword()
    {
        String _password = confirmNewPassword.getEditText().getText().toString().trim();

        if(_password.isEmpty())
        {
            confirmNewPassword.setError("Password number can't be empty");
            confirmNewPassword.requestFocus();
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean validatePassword()
    {
        String _password = newPassword.getEditText().getText().toString().trim();

        if(_password.isEmpty())
        {
            newPassword.setError("Password number can't be empty");
            newPassword.requestFocus();
            return false;
        }
        else
        {
            return true;
        }
    }

    private void showCustomDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(SetNewPasswordActivity.this);

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