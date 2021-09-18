package com.example.catsapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.catsapp.Activities.ChatActivity;
import com.example.catsapp.Models.User;
import com.example.catsapp.R;

import java.util.List;

public class ContactsAdapter  extends RecyclerView.Adapter<ContactsAdapter.ViewHolder>
{
    private List<User> list;
    private Context context;

    public ContactsAdapter(List<User> list, Context context)
    {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_contact_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        final User user = list.get(position);

        holder.username.setText(user.getUsername());
        holder.desc.setText(user.getPhoneNo());

        Glide.with(context).load(user.getProfileImage()).into(holder.imageProfile);

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                context.startActivity(new Intent(context, ChatActivity.class)
                        .putExtra("userID",user.getId())
                        .putExtra("userName",user.getUsername())
                        .putExtra("userProfile",user.getProfileImage()));
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        private ImageView imageProfile;
        private TextView username,desc;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.tv_username);
            desc = itemView.findViewById(R.id.tv_desc);
        }
    }
}
