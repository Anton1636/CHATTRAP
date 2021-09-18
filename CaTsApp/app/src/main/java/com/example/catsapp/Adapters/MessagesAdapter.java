package com.example.catsapp.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.catsapp.Activities.ChatActivity;
import com.example.catsapp.Models.Message;
import com.example.catsapp.R;
import com.example.catsapp.databinding.DeleteDialogBinding;
import com.example.catsapp.databinding.ItemReciveBinding;
import com.example.catsapp.databinding.ItemSendBinding;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MessagesAdapter extends RecyclerView.Adapter
{
    Context context;
    ArrayList<Message> messages;

    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;

    String senderRoom;
    String receiverRoom;

//    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String receiverID;

    public MessagesAdapter(Context context, ArrayList<Message> messages, String senderRoom, String receiverRoom)
    {
        this.context = context;
        this.messages = messages;
        this.senderRoom = senderRoom;
        this.receiverRoom = receiverRoom;
    }

    public MessagesAdapter(ChatActivity context, ArrayList<Message> messages, String senderRoom, String receiverRoom) { }

    public MessagesAdapter(Context context, String receiverID)
    {
        this.context = context;
        this.receiverID = receiverID;
    }

    public MessagesAdapter() { }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if(viewType == ITEM_SENT)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.item_send, parent, false);
            return new SentViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_recive, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        Message message = messages.get(position);

//        if(FirebaseAuth.getInstance().getUid().equals(message.getSenderId()))
//        {
//            return ITEM_SENT;
//        }
//        else {
//            return ITEM_RECEIVE;
//        }
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        Message message = messages.get(position);

        int reactions[] = new int[]{
                R.drawable.ic_fb_like,
                R.drawable.ic_fb_love,
                R.drawable.ic_fb_laugh,
                R.drawable.ic_fb_wow,
                R.drawable.ic_fb_sad,
                R.drawable.ic_fb_angry
        };

        ReactionsConfig config = new ReactionsConfigBuilder(context).withReactions(reactions).build();

        ReactionPopup popup = new ReactionPopup(context, config, (pos) ->
        {
            if(holder.getClass() == SentViewHolder.class)
            {
                SentViewHolder viewHolder = (SentViewHolder)holder;

                viewHolder.binding.feeling.setImageResource(reactions[pos]);
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
            }
            else {
                ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;

                viewHolder.binding.feeling.setImageResource(reactions[pos]);
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
            }

            message.setFeeling(pos);

            //FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("messages").child(message.getMessageId()).setValue(message);
           // FirebaseDatabase.getInstance().getReference().child("chats").child(receiverRoom).child("messages").child(message.getMessageId()).setValue(message);

            return true; // true is closing popup, false is requesting a new selection
        });


        if(holder.getClass() == SentViewHolder.class)
        {
            SentViewHolder viewHolder = (SentViewHolder)holder;

            if(message.getMessage().equals("photo"))
            {
                viewHolder.binding.image.setVisibility(View.VISIBLE);
                viewHolder.binding.message.setVisibility(View.GONE);

                Glide.with(context).load(message.getImageUrl()).placeholder(R.drawable.placeholder).into(viewHolder.binding.image);
            }

            viewHolder.binding.message.setText(message.getMessage());

            if(message.getFeeling() >= 0)
            {
                viewHolder.binding.feeling.setImageResource(reactions[message.getFeeling()]);
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
            }
            else {
                viewHolder.binding.feeling.setVisibility(View.GONE);
            }

            viewHolder.binding.message.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    popup.onTouch(v, event);
                    return false;
                }
            });

            viewHolder.binding.image.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    popup.onTouch(v, event);
                    return false;
                }
            });

            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    View view = LayoutInflater.from(context).inflate(R.layout.delete_dialog, null);
                    DeleteDialogBinding binding = DeleteDialogBinding.bind(view);
                    AlertDialog dialog = new AlertDialog.Builder(context).setTitle("Delete Message").setView(binding.getRoot()).create();

                    binding.everyone.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            message.setMessage("This message is removed.");
                            message.setFeeling(-1);

                            //FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("messages").child(message.getMessageId()).setValue(message);
                           // FirebaseDatabase.getInstance().getReference().child("chats").child(receiverRoom).child("messages").child(message.getMessageId()).setValue(message);

                            dialog.dismiss();
                        }
                    });

                    binding.delete.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            //FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("messages").child(message.getMessageId()).setValue(null);

                            dialog.dismiss();
                        }
                    });

                    binding.cancel.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                    return false;
                }
            });
        }
        else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder)holder;

            if(message.getMessage().equals("photo"))
            {
                viewHolder.binding.image.setVisibility(View.VISIBLE);
                viewHolder.binding.message.setVisibility(View.GONE);
                Glide.with(context).load(message.getImageUrl()).placeholder(R.drawable.placeholder).into(viewHolder.binding.image);
            }

            viewHolder.binding.message.setText(message.getMessage());

            if(message.getFeeling() >= 0)
            {
                //message.setFeeling(reactions[message.getFeeling()]);
                viewHolder.binding.feeling.setImageResource(reactions[message.getFeeling()]);
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
            }
            else {
                viewHolder.binding.feeling.setVisibility(View.GONE);
            }

            viewHolder.binding.message.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    popup.onTouch(v, event);
                    return false;
                }
            });

            viewHolder.binding.image.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    popup.onTouch(v, event);
                    return false;
                }
            });

            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    View view = LayoutInflater.from(context).inflate(R.layout.delete_dialog, null);
                    DeleteDialogBinding binding = DeleteDialogBinding.bind(view);
                    AlertDialog dialog = new AlertDialog.Builder(context).setTitle("Delete Message").setView(binding.getRoot()).create();

                    binding.everyone.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            message.setMessage("This message is removed.");
                            message.setFeeling(-1);

                           // FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("messages").child(message.getMessageId()).setValue(message);
                           // FirebaseDatabase.getInstance().getReference().child("chats").child(receiverRoom).child("messages").child(message.getMessageId()).setValue(message);

                            dialog.dismiss();
                        }
                    });

                    binding.delete.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            //FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("messages").child(message.getMessageId()).setValue(null);

                            dialog.dismiss();
                        }
                    });

                    binding.cancel.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount()
    {
        return messages.size();
    }

    public class SentViewHolder extends RecyclerView.ViewHolder
    {
        ItemSendBinding binding;

        public SentViewHolder(@NonNull View itemView)
        {
            super(itemView);

            binding = ItemSendBinding.bind(itemView);
        }
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder
    {
        ItemReciveBinding binding;

        public ReceiverViewHolder(@NonNull View itemView)
        {
            super(itemView);

            binding = ItemReciveBinding.bind(itemView);
        }
    }

    private String getCurrentDate()
    {
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String today = formatter.format(date);

        Calendar currentDateTime = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        String currentTime = df.format(currentDateTime.getTime());

        return today + ", " + currentTime;
    }

    public void sendVoice(String authPath)
    {
        final Uri uriAudio = Uri.fromFile(new File(authPath));
       // final StorageReference audioRef = FirebaseStorage.getInstance().getReference().child("CaTsApp/Voice/" + System.currentTimeMillis());

//        audioRef.putFile(uriAudio).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
//        {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot audioSnapshot)
//            {
//                Task<Uri> uriTask = audioSnapshot.getStorage().getDownloadUrl();
//
//                while (!uriTask.isSuccessful());
//
//                Uri downloadUrl = uriTask.getResult();
//                String voiceUrl = String.valueOf(downloadUrl);
//
//                Message message = new Message(getCurrentDate(), "", voiceUrl, "VOICE", firebaseUser.getUid(), receiverID);
//
//                reference.child("ChatActivity").push().setValue(message).addOnSuccessListener(new OnSuccessListener<Void>()
//                {
//                    @Override
//                    public void onSuccess(Void aVoid)
//                    {
//                        Log.d("Send", "onSuccess:");
//                    }
//                }).addOnFailureListener(new OnFailureListener()
//                {
//                    @Override
//                    public void onFailure(@NonNull Exception e)
//                    {
//                        Log.d("Send", "onFailure:" + e.getMessage());
//                    }
//                });
//
//                DatabaseReference messageRef1 = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid()).child(receiverID);
//                messageRef1.child("messageId").setValue(receiverID);
//
//                DatabaseReference messageRef2 = FirebaseDatabase.getInstance().getReference("ChatList").child(receiverID).child(firebaseUser.getUid());
//                messageRef2.child("messageId").setValue(firebaseUser.getUid());
//            }
//        });
    }
}
