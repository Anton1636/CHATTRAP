package com.example.catsapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.catsapp.R;
import com.example.catsapp.databinding.ActivitySettingBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SettingActivity extends AppCompatActivity
{
    private ActivitySettingBinding binding;
    //private FirebaseUser firebaseUser;
    //private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
//        firestore = FirebaseFirestore.getInstance();
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        if (firebaseUser!=null)
//        {
//            getInfo();
//        }

        initClickAction();
    }

    private void initClickAction()
    {
        binding.lnProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(SettingActivity.this, ProfileActivity.class));
            }
        });
    }

    private void getInfo()
    {
//        firestore.collection("Users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
//        {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot)
//            {
//                String userName = Objects.requireNonNull(documentSnapshot.get("userName")).toString();
//                String imageProfile = documentSnapshot.getString("imageProfile");
//
//                binding.tvUsername.setText(userName);
//                Glide.with(SettingActivity.this).load(imageProfile).into(binding.imageProfile);
//
//            }
//        }).addOnFailureListener(new OnFailureListener()
//        {
//            @Override
//            public void onFailure(@NonNull Exception e)
//            {
//                Log.d("Get Data", "onFailure: "+e.getMessage());
//            }
//        });
    }
}