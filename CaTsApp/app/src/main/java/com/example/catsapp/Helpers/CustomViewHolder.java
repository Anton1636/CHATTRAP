package com.example.catsapp.Helpers;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catsapp.R;

public class CustomViewHolder extends RecyclerView.ViewHolder
{
        private View view;
        public ImageView img;
        public TextView fullName;
        public TextView phoneNo;

        public CustomViewHolder(@NonNull View itemView)
        {
                super(itemView);
                this.view = itemView;
                img = itemView.findViewById(R.id.profileR);
                fullName = itemView.findViewById(R.id.fullNameR);
                phoneNo = itemView.findViewById(R.id.phone_numberR);
        }

        public View getView()
        {
                return view;
        }
}
