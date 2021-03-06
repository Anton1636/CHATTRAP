package com.example.catsapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.adapters.AdapterViewBindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.catsapp.Activities.LogInSignUp.LoginActivity;
import com.example.catsapp.Activities.LogInSignUp.SignUpActivity;
import com.example.catsapp.Adapters.TopStatusAdapter;
import com.example.catsapp.Adapters.UsersAdapter;
import com.example.catsapp.Helpers.SessionManager;
import com.example.catsapp.Models.Status;
import com.example.catsapp.Models.User;
import com.example.catsapp.Models.UserStatus;
import com.example.catsapp.R;
import com.example.catsapp.Secvice.NetworkService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.example.catsapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
{

    ActivityMainBinding binding;
    ArrayList<User> users;
    UsersAdapter usersAdapter;
    TopStatusAdapter statusAdapter;
    ArrayList<UserStatus> userStatuses;
    ProgressDialog dialog;
    BottomNavigationView bottomView;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       // TextView textView = findViewById(R.id.username);
        SessionManager sessionManager = new SessionManager(MainActivity.this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();
        //String username = userDetails.get(SessionManager.KEY_USERNAME);

       // textView.setText= username;

        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading Image...");
        dialog.setCancelable(false);

        userStatuses = new ArrayList<>();

        usersAdapter = new UsersAdapter(this, users);
        statusAdapter = new TopStatusAdapter(this, userStatuses);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setOrientation(RecyclerView.HORIZONTAL);

        binding.statusList.setLayoutManager(layoutManager);
        binding.statusList.setAdapter(statusAdapter);
        binding.recyclerView.setAdapter(usersAdapter);
        binding.recyclerView.showShimmerAdapter();
        binding.statusList.showShimmerAdapter();

        bottomView = findViewById(R.id.bottomNavigationView);

        bottomView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.chats:
                        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                        startActivity(intent);
                    case R.id.status:
                        Intent intent2 = new Intent(getApplicationContext(), StartUpActivity.class);
                        startActivity(intent2);
                    case R.id.calls:
                        Intent intent3 = new Intent(getApplicationContext(), ContactsActivity.class);
                        startActivity(intent3);
                }

                return false;
            }
        });

//        database.getReference().child("users").addValueEventListener(new ValueEventListener()
//        {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot)
//            {
//               // users.clear();
//                for(DataSnapshot snapshot1 : snapshot.getChildren())
//                {
//                    User user = snapshot1.getValue(User.class);
//
////                    if(!user.getPhoneNo().equals(FirebaseAuth.getInstance().g()))
////                    {
////                        users.add(user);
////                    }
//                }
//
//                binding.recyclerView.hideShimmerAdapter();
//                usersAdapter.notifyDataSetChanged();
//            }

//            @Override
//            public void onCancelled(@NonNull DatabaseError error)
//            {
//
//            }
    //    });

//        database.getReference().child("stories").addValueEventListener(new ValueEventListener()
//        {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot)
//            {
//                if(snapshot.exists())
//                {
//                    userStatuses.clear();
//
//                    for(DataSnapshot storySnapshot : snapshot.getChildren())
//                    {
//                        UserStatus status = new UserStatus();
//
//                        status.setName(storySnapshot.child("name").getValue(String.class));
//                        status.setProfileImage(storySnapshot.child("profileImage").getValue(String.class));
//                        status.setLastUpdated(storySnapshot.child("lastUpdated").getValue(Long.class));
//
//                        ArrayList<Status> statuses = new ArrayList<>();
//
//                        for(DataSnapshot statusSnapshot : storySnapshot.child("statuses").getChildren())
//                        {
//                            Status sampleStatus = statusSnapshot.getValue(Status.class);
//                            statuses.add(sampleStatus);
//                        }
//
//                        status.setStatuses(statuses);
//                        userStatuses.add(status);
//                    }
//
//                    binding.statusList.hideShimmerAdapter();
//                    statusAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error)
//            {
//
//            }
//        });


//        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
//        {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item)
//            {
//                switch (item.getItemId())
//                {
//                    case R.id.status:
//                        Intent intent = new Intent();
//                        intent.setType("image/*");
//                        intent.setAction(Intent.ACTION_GET_CONTENT);
//                        startActivityForResult(intent, 75);
//                        break;
//                }
//                return false;
//            }
//        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null)
        {
            if(data.getData() != null)
            {
                dialog.show();
                //FirebaseStorage storage = FirebaseStorage.getInstance();

                //Date date = new Date();
                //StorageReference reference = storage.getReference().child("status").child(date.getTime() + "");

//                reference.putFile(data.getData()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
//                {
//                    @Override
//                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
//                    {
//                        if(task.isSuccessful())
//                        {
//                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
//                            {
//                                @Override
//                                public void onSuccess(Uri uri)
//                                {
//                                    UserStatus userStatus = new UserStatus();
//                                    userStatus.setName(user.getUsername());
//                                    userStatus.setProfileImage(user.getProfileImage());
//                                    userStatus.setLastUpdated(date.getTime());
//
//                                    HashMap<String, Object> obj = new HashMap<>();
//                                    obj.put("name", userStatus.getName());
//                                    obj.put("profileImage", userStatus.getProfileImage());
//                                    obj.put("lastUpdated", userStatus.getLastUpdated());
//
//                                    String imageUrl = uri.toString();
//                                   // Status status = new Status(imageUrl, userStatus.getLastUpdated());
//
////                                    database.getReference()
////                                            .child("stories")
////                                            .child(FirebaseAuth.getInstance().getUid())
////                                            .updateChildren(obj);
////
////                                    database.getReference().child("stories")
////                                            .child(FirebaseAuth.getInstance().getUid())
////                                            .child("statuses")
////                                            .push()
////                                            .setValue(status);
//
//                                    dialog.dismiss();
//                                }
//                            });
//                        }
//                    }
//                });
            }
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //String currentId = FirebaseAuth.getInstance().getUid();
        //database.getReference().child("presence").child(currentId).setValue("Online");
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        //String currentId = FirebaseAuth.getInstance().getUid();
        //database.getReference().child("presence").child(currentId).setValue("Offline");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.group:
                startActivity(new Intent(MainActivity.this, GroupChatActivity.class));
                break;
            case R.id.search:
                Toast.makeText(this, "Search clicked.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.topmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }
}