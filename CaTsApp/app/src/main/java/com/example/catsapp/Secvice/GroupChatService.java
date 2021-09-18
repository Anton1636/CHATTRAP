package com.example.catsapp.Secvice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.catsapp.Activities.LogInSignUp.VerifyOTPActivity;
import com.example.catsapp.Activities.MainActivity;
import com.example.catsapp.Activities.application.HomeApplication;
import com.example.catsapp.Models.LoginResultDto;
import com.example.catsapp.Models.Message;
import com.example.catsapp.OnReadChatCallBack;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupChatService
{
    private Context context;
//    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String receiverID;

    public GroupChatService(Context context, String receiverID)
    {
        this.context = context;
        this.receiverID = receiverID;
    }

    public GroupChatService(Context context)
    {
        this.context = context;
    }

    public void readChatData(final OnReadChatCallBack onCallBack)
    {
//        reference.child("Chats").addValueEventListener(new ValueEventListener()
//        {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//            {
//                List<Message> list = new ArrayList<>();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren())
//                {
//                    Message chats = snapshot.getValue(Message.class);
//
//                    try
//                    {
//                        if (chats.getSenderId().equals(firebaseUser.getUid()) && chats.getReceiver().equals(receiverID) || chats.getReceiver().equals(firebaseUser.getUid()) && chats.getSenderId().equals(receiverID))
//                        {
//                            list.add(chats);
//                        }
//                    }
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//                onCallBack.onReadSuccess(list);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError)
//            {
//                onCallBack.onReadFailed();
//            }
//        });
    }

    public void sendImage(String imageUrl)
    {
        //Message chats = new Message(getCurrentDate(), "", imageUrl, "IMAGE", firebaseUser.getUid(), receiverID);

//        reference.child("Chats").push().setValue(chats).addOnSuccessListener(new OnSuccessListener<Void>()
//        {
//            @Override
//            public void onSuccess(Void aVoid)
//            {
//                Log.d("Send", "onSuccess: ");
//            }
//        }).addOnFailureListener(new OnFailureListener()
//        {
//            @Override
//            public void onFailure(@NonNull Exception e)
//            {
//                Log.d("Send", "onFailure: "+e.getMessage());
//            }
//        });
//
//        //Add to ChatList
//        DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid()).child(receiverID);
//        chatRef1.child("chatid").setValue(receiverID);
//
//        DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("ChatList").child(receiverID).child(firebaseUser.getUid());
//        chatRef2.child("chatid").setValue(firebaseUser.getUid());
    }

    public String getCurrentDate()
    {
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String today = formatter.format(date);

        Calendar currentDateTime = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        String currentTime = df.format(currentDateTime.getTime());

        return today+", "+currentTime;
    }

    public void sendVoice(String audioPath)
    {
        final Uri uriAudio = Uri.fromFile(new File(audioPath));
        final StorageReference audioRef = FirebaseStorage.getInstance().getReference().child("Chats/Voice/" + System.currentTimeMillis());

        audioRef.putFile(uriAudio).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot audioSnapshot)
            {
                Task<Uri> urlTask = audioSnapshot.getStorage().getDownloadUrl();

                while (!urlTask.isSuccessful()) ;
                Uri downloadUrl = urlTask.getResult();
                String voiceUrl = String.valueOf(downloadUrl);
                //Message chats = new Message(getCurrentDate(), "", voiceUrl, "VOICE", firebaseUser.getUid(), receiverID);

//                reference.child("Chats").push().setValue(chats).addOnSuccessListener(new OnSuccessListener<Void>()
//                {
//                    @Override
//                    public void onSuccess(Void aVoid)
//                    {
//                        Log.d("Send", "onSuccess: ");
//                    }
//                }).addOnFailureListener(new OnFailureListener()
//                {
//                    @Override
//                    public void onFailure(@NonNull Exception e)
//                    {
//                        Log.d("Send", "onFailure: "+e.getMessage());
//                    }
//                });

                //Add to ChatList
//                DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid()).child(receiverID);
//                chatRef1.child("chatid").setValue(receiverID);
//
//                DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("ChatList").child(receiverID).child(firebaseUser.getUid());
//                chatRef2.child("chatid").setValue(firebaseUser.getUid());
            }
        });
    }
}
