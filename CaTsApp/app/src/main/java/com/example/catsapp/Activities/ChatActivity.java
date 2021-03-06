package com.example.catsapp.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.devlomi.record_view.OnBasketAnimationEnd;
import com.devlomi.record_view.OnRecordListener;
import com.example.catsapp.Activities.LogInSignUp.DialogReviewSendImage;
import com.example.catsapp.Activities.LogInSignUp.VerifyOTPActivity;
import com.example.catsapp.Activities.application.HomeApplication;
import com.example.catsapp.Adapters.GroupMessagesAdapter;
import com.example.catsapp.Helpers.DataStatic;
import com.example.catsapp.Models.LoginResultDto;
import com.example.catsapp.Models.Message;
import com.example.catsapp.Models.MessageResultDto;
import com.example.catsapp.OnReadChatCallBack;
import com.example.catsapp.R;
import com.example.catsapp.Secvice.GroupChatService;
import com.example.catsapp.Secvice.JwtSecurityService;
import com.example.catsapp.Secvice.NetworkService;
import com.example.catsapp.databinding.ActivityGroupChatBinding;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity
{
    private static final String TAG = "GroupChatActivity";
    private static final int REQUEST_CORD_PERMISSION = 332;
    private ActivityGroupChatBinding binding;
    private String receiverID;
    private GroupMessagesAdapter adapder;
    private List<Message> list = new ArrayList<>();
    private String userProfile,userName;
    private boolean isActionShown = false;
    private GroupChatService chatService;
    private int IMAGE_GALLERY_REQUEST = 111;
    private Uri imageUri;
    private EditText messageText;

    //Audio
    private MediaRecorder mediaRecorder;
    private String audio_path;
    private String sTime;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_chat);
        DataStatic.id = "1";
        messageText = findViewById(R.id.messageBox);


        initialize();
        initBtnClick();
        readChats();
    }

    private void initialize()
    {
        userName = DataStatic.username;
        receiverID = DataStatic.id;
        userProfile = DataStatic.image;

        chatService = new GroupChatService(this,receiverID);

        if (receiverID!=null)
        {
            Log.d(TAG, "onCreate: receiverID "+receiverID);
            binding.tvUsername.setText(userName);

            if (userProfile != null)
            {
                if (userProfile.equals(""))
                {
                    binding.imageProfile.setImageResource(R.drawable.avatar);  // set  default image when profile user is null
                }
                else {
                    Glide.with(this).load(userProfile).into(binding.imageProfile);
                }
            }
        }

        binding.edMessage.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (TextUtils.isEmpty(binding.edMessage.getText().toString()))
                {
                    binding.btnSend.setVisibility(View.INVISIBLE);
                    binding.recordButton.setVisibility(View.VISIBLE);
                }
                else {
                    binding.btnSend.setVisibility(View.VISIBLE);
                    binding.recordButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);

        layoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setHasFixedSize(true);
        binding.recordButton.setRecordView(binding.recordView);
        binding.recordView.setOnRecordListener(new OnRecordListener()
        {
            @Override
            public void onStart()
            {
                //Start Recording..
                if (!checkPermissionFromDevice())
                {
                    binding.btnEmoji.setVisibility(View.INVISIBLE);
                    binding.btnFile.setVisibility(View.INVISIBLE);
                    binding.btnCamera.setVisibility(View.INVISIBLE);
                    binding.edMessage.setVisibility(View.INVISIBLE);

                    startRecord();

                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                    if (vibrator != null)
                    {
                        vibrator.vibrate(100);
                    }

                }
                else {
                    requestPermission();
                }

            }

            @Override
            public void onCancel()
            {
                try
                {
                    mediaRecorder.reset();
                }
                catch
                (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish(long recordTime)
            {
                binding.btnEmoji.setVisibility(View.VISIBLE);
                binding.btnFile.setVisibility(View.VISIBLE);
                binding.btnCamera.setVisibility(View.VISIBLE);
                binding.edMessage.setVisibility(View.VISIBLE);

                //Stop Recording..
                try
                {
                    sTime = getHumanTimeText(recordTime);
                    stopRecord();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLessThanSecond()
            {
                binding.btnEmoji.setVisibility(View.VISIBLE);
                binding.btnFile.setVisibility(View.VISIBLE);
                binding.btnCamera.setVisibility(View.VISIBLE);
                binding.edMessage.setVisibility(View.VISIBLE);
            }
        });

        binding.recordView.setOnBasketAnimationEndListener(new OnBasketAnimationEnd()
        {
            @Override
            public void onAnimationEnd()
            {
                binding.btnEmoji.setVisibility(View.VISIBLE);
                binding.btnFile.setVisibility(View.VISIBLE);
                binding.btnCamera.setVisibility(View.VISIBLE);
                binding.edMessage.setVisibility(View.VISIBLE);
            }
        });

    }

    @SuppressLint("DefaultLocale")
    private String getHumanTimeText(long milliseconds)
    {
        return String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

    private void readChats()
    {
        chatService.readChatData(new OnReadChatCallBack()
        {
            @Override
            public void onReadSuccess(List<Message> list)
            {
                Log.d(TAG, "onReadSuccess: List "+list.size());
                binding.recyclerView.setAdapter(new GroupMessagesAdapter(list,ChatActivity.this));
            }

            @Override
            public void onReadFailed()
            {
                Log.d(TAG, "onReadFailed: ");
            }
        });
    }

    private void initBtnClick()
    {
        binding.btnSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!TextUtils.isEmpty(binding.edMessage.getText().toString()))
                {
                    Message message = new Message();

                    message.setMessage(messageText.toString());
                    message.setSenderId(DataStatic.username);

                    NetworkService.getInstance()
                            .getJSONApi()
                            .sendMessage(message)
                            .enqueue(new Callback()
                            {
                                @Override
                                public void onResponse(Call call, Response response)
                                {
                                    MessageResultDto result = (MessageResultDto) response.body();
                                    JwtSecurityService jwtService = (JwtSecurityService) HomeApplication.getInstance();

                                    jwtService.saveJwtToken(result.getToken());

                                    Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailure(Call call, Throwable t)
                                {
                                    Toast.makeText(ChatActivity.this, "Not to day!", Toast.LENGTH_SHORT).show();
                                    t.printStackTrace();
                                }
                            });
                    binding.edMessage.setText("");
                }
            }
        });

        binding.imageProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(ChatActivity.this, UserProfileActivity.class)
                        .putExtra("userID",receiverID)
                        .putExtra("userProfile",userProfile)
                        .putExtra("userName",userName));
            }
        });

        binding.btnFile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isActionShown)
                {
                    binding.layoutActions.setVisibility(View.GONE);
                    isActionShown = false;
                }
                else {
                    binding.layoutActions.setVisibility(View.VISIBLE);
                    isActionShown = true;
                }

            }
        });

        binding.btnGallery.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openGallery();
            }
        });
    }

    private void openGallery()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "select image"), IMAGE_GALLERY_REQUEST);
    }

    private boolean checkPermissionFromDevice()
    {
        int write_external_strorage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

        return write_external_strorage_result == PackageManager.PERMISSION_DENIED || record_audio_result == PackageManager.PERMISSION_DENIED;
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, REQUEST_CORD_PERMISSION);
    }

    private void startRecord()
    {
        setUpMediaRecorder();

        try
        {
            mediaRecorder.prepare();
            mediaRecorder.start();
            Toast.makeText(ChatActivity.this, "Recording...", Toast.LENGTH_LONG).show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Toast.makeText(ChatActivity.this, "Recording Error , Please restart your app ", Toast.LENGTH_LONG).show();
        }

    }

    private void stopRecord()
    {
        try
        {
            if (mediaRecorder != null)
            {
                mediaRecorder.stop();
                mediaRecorder.reset();
                mediaRecorder.release();
                mediaRecorder = null;

                //sendVoice();
                chatService.sendVoice(audio_path);
            }
            else {
                Toast.makeText(getApplicationContext(), "Null", Toast.LENGTH_LONG).show();
            }

        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Stop Recording Error :" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setUpMediaRecorder()
    {
        String path_save = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + UUID.randomUUID().toString() + "audio_record.m4a";

        audio_path = path_save;
        mediaRecorder = new MediaRecorder();

        try
        {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setOutputFile(path_save);
        }
        catch (Exception e)
        {
            Log.d(TAG, "setUpMediaRecord: " + e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imageUri = data.getData();

            //uploadToFirebase();
            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                reviewImage(bitmap);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }

    private void reviewImage(Bitmap bitmap)
    {
        new DialogReviewSendImage(ChatActivity.this, bitmap).show(new DialogReviewSendImage.OnCallBack()
        {
            @Override
            public void onButtonSendClick()
            {
                // to Upload Image to firebase storage to get url image...
                if (imageUri!=null)
                {
                    final ProgressDialog progressDialog = new ProgressDialog(ChatActivity.this);

                    progressDialog.setMessage("Sending image...");
                    progressDialog.show();

                    binding.layoutActions.setVisibility(View.GONE);
                    isActionShown = false;

//                    new FirebaseService(ChatActivity.this).uploadImageToFireBaseStorage(imageUri, new FirebaseService.OnCallBack()
//                    {
//                        @Override
//                        public void onUploadSuccess(String imageUrl)
//                        {
//                            // to send chat image//
//                            chatService.sendImage(imageUrl);
//                            progressDialog.dismiss();
//                        }
//
//                        @Override
//                        public void onUploadFailed(Exception e)
//                        {
//                            e.printStackTrace();
//                        }
//                    });
                }

            }
        });
    }
}