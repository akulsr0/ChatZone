package com.akul.chatzone;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//Created by AkulSrivastava
//May 2019

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity
{

    public static final int SELECTED = 101;

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference userReference;

    FirebaseStorage mStorage;
    StorageReference profileImageReference;

    EditText userfullname, userstatus;
    TextView logoutUser;
    
    String uid;

    ProgressBar imgUploadProgressBar;
    ImageView profileImage;
    Uri profileImageUri;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        mAuth = FirebaseAuth.getInstance();

        uid = mAuth.getUid();

        imgUploadProgressBar = findViewById(R.id.imguploadProgressBar);

        profileImage = findViewById(R.id.userprofileedit);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImg();
            }
        });

        logoutUser = findViewById(R.id.logoutuser);
        logoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(ProfileActivity.this, "User Logged Out", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        
        mDatabase = FirebaseDatabase.getInstance();
        userReference = mDatabase.getReference("Users/"+uid+"/");

        userfullname = findViewById(R.id.userFullName);
        userstatus = findViewById(R.id.userStatus);

        mStorage = FirebaseStorage.getInstance();
        profileImageReference = mStorage.getReference("ProfileImages/");



    }

//Created by AkulSrivastava
//May 2019


    @Override
    protected void onStart() {
        super.onStart();

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = dataSnapshot.child("displayimage").getValue().toString();
                if(dataSnapshot.child("status").exists()){
                    String status = dataSnapshot.child("status").getValue().toString();
                    userstatus.setText(status);
                }
                if(dataSnapshot.child("fullname").exists()){
                    String fullname = dataSnapshot.child("fullname").getValue().toString();
                    userfullname.setText(fullname);
                }
                if(dataSnapshot.child("displayimage").equals("null")){
                    profileImage.setImageResource(R.drawable.useredit);
                }
                else{
                    Glide.with(getApplicationContext()).load(url).into(profileImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void saveDp()
    {

        if(profileImageUri!=null) {

            final StorageReference fileRef = profileImageReference.child(uid+"."+getFileExtension(profileImageUri));
            fileRef.putFile(profileImageUri)
                    .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                        {
                            if(!task.isSuccessful())
                            {
                                throw task.getException();
                            }
                            return fileRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Toast.makeText(ProfileActivity.this, "Profile Image Set", Toast.LENGTH_SHORT).show();
                    imgUploadProgressBar.setVisibility(View.INVISIBLE);
                    Uri downuri = task.getResult();
                    String url = downuri.toString();
                    userReference.child("displayimage").setValue(url);

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
            ;
        }
        else {
            Toast.makeText(getApplicationContext(),"No image selected", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Date sentMessageTime = Calendar.getInstance().getTime();
        DateFormat newFormat = new SimpleDateFormat("hh:mm a");
        String time = newFormat.format(sentMessageTime);

        status(time);
    }

    public void status(String status){

        HashMap<String,Object> map1 = new HashMap<>();
        map1.put("seenstatus",status);

        userReference.updateChildren(map1);

    }

    private void chooseImg()
    {
        Intent picPick = new Intent();
        picPick.setType("image/*");
        picPick.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(picPick,SELECTED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==SELECTED && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            profileImageUri = data.getData();
            imgUploadProgressBar.setVisibility(View.VISIBLE);
            Glide.with(this).load(profileImageUri).into(profileImage);
            saveDp();
        }



    }

    public void backprofileactivity(View view) {
        Intent i = new Intent(ProfileActivity.this, UserActivity.class);
        startActivity(i);
    }

    public void saveUserInfo(View view) {

        String fullname = userfullname.getText().toString();
        String status = userstatus.getText().toString();

        userReference.child("status").setValue(status);
        userReference.child("fullname").setValue(fullname);

    }
}


//Created by AkulSrivastava
//May 2019
