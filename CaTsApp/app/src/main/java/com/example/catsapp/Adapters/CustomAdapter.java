package com.example.catsapp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.catsapp.Helpers.Common;
import com.example.catsapp.Helpers.CustomViewHolder;
import com.example.catsapp.Helpers.ImageHelper;
import com.example.catsapp.Helpers.Urls;
import com.example.catsapp.Models.User;
import com.example.catsapp.R;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder>
{

    private List<User> modelList;
    private Context context;
    private ImageHelper imageRequester;

    public CustomAdapter(List<User> modelList, Context context)
    {
        this.modelList = modelList;
        this.context = context;
        imageRequester = ImageHelper.getInstance();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);

        return new CustomViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position)
    {
        if (modelList != null && position < modelList.size())
        {
            User model = modelList.get(position);

            holder.fullName.setText(model.getFullName());
            holder.phoneNo.setText(model.getPhoneNo());

            String url = Urls.BASE+"/images/" + model.getProfileImage();

            imageRequester.setImageFromUrl((NetworkImageView) holder.img, url);
        }
    }

    @Override
    public int getItemCount()
    {
        return modelList.size();
    }
}
