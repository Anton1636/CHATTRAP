package com.example.chattrap.Activities.LogInSignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.chattrap.Activities.MainActivity;
import com.example.chattrap.Activities.StartUpActivity;
import com.example.chattrap.Models.User;
import com.example.chattrap.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class VerifyOTPActivity extends AppCompatActivity
{

    PinView pinView;
    String codeBySystem;
    String fullName, username, email, password, date, gender, phoneNo, whatToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verify_o_t_p);

        pinView = findViewById(R.id.pin_view);

        fullName = getIntent().getStringExtra("fullName");
        username = getIntent().getStringExtra("userName");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        date = getIntent().getStringExtra("date");
        gender = getIntent().getStringExtra("gender");
        phoneNo = getIntent().getStringExtra("phoneNo");
        whatToDo = getIntent().getStringExtra("whatToDo");

        sendVerificationCodeToUser(phoneNo);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
    {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken)
        {
            super.onCodeSent(s, forceResendingToken);

            codeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)
        {
            String code = phoneAuthCredential.getSmsCode();

            if(code != null)
            {
                pinView.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e)
        {
            Toast.makeText(VerifyOTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void sendVerificationCodeToUser(String phoneNo)
    {
        PhoneAuthProvider.verifyPhoneNumber(
                PhoneAuthOptions
                        .newBuilder(FirebaseAuth.getInstance())
                        .setActivity(this)
                        .setPhoneNumber(phoneNo)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setCallbacks(mCallbacks)
                        .build());
    }

    private void verifyCode(String code)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem,code);

        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
             .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
             {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task)
                 {
                     if (task.isSuccessful())
                     {
                        storeNewUsersData();

                        if(whatToDo.equals("updateData"))
                        {
                            updateOldUserData();
                        }
                        else
                        {
                            storeNewUsersData();
                        }
                     }
                     else
                     {
                         if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                         {
                             Toast.makeText(VerifyOTPActivity.this, "Verification Not Complete! Try again", Toast.LENGTH_SHORT).show();
                         }
                    }
                 }
             });
    }

    private void updateOldUserData()
    {
        Intent intent = new Intent(getApplicationContext(), SetNewPasswordActivity.class);
        intent.putExtra("phoneNo", phoneNo);

        startActivity(intent);
        finish();
    }

    private void storeNewUsersData() 
    {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Users");
        
        User addNewUser = new User(fullName,username,email,password,date,gender,phoneNo);

        reference.child(phoneNo).setValue(addNewUser);
    }

    public void callNextSceenFronOTP(View view)
    {
        String code = pinView.getText().toString();

        if(!code.isEmpty())
        {
            verifyCode(code);
        }
    }
}