package com.example.catsapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.catsapp.Helpers.Common;
import com.example.catsapp.R;
import com.example.catsapp.databinding.ActivityViewImageBinding;

public class ViewImageActivity extends AppCompatActivity
{

    private ActivityViewImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        binding.imageView.setImageBitmap(Common.IMAGE_BITMAP);
    }
}