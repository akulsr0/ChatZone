package com.akul.chatzone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//Created by AkulSrivastava
//May 2019

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//Created by AkulSrivastava
//May 2019

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class FriendProfile extends AppCompatActivity
{

    ImageView backButton, userdp, seenstatus, messagefriend,verifiedLogo;
    TextView usernameToolbar, userfullname, userstatus;

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference friendReference;
    DatabaseReference userReference;
    DatabaseReference followingReference;
    DatabaseReference notificationReference;

    Button followfriend, followingfriend;

    String uid;
    String currentuseruid;

//Created by AkulSrivastava
//May 2019

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        backButton = findViewById(R.id.FPbackbutton);

        userdp = findViewById(R.id.FPuserprofileimage);

        usernameToolbar = findViewById(R.id.FPusernametoolbar);
        userfullname= findViewById(R.id.FPuserfullname);
        userstatus = findViewById(R.id.FPuserstatus);
        seenstatus = findViewById(R.id.FPseenStatus);
        followfriend = findViewById(R.id.FPfollowuser);
        followingfriend = findViewById(R.id.FPfollowinguser);
        verifiedLogo = findViewById(R.id.verifiedLogo);

        mAuth = FirebaseAuth.getInstance();
        currentuseruid = mAuth.getUid();

        Intent i = getIntent();
        uid = i.getStringExtra("uid");

        messagefriend = findViewById(R.id.messageFriend);

        if(uid.equals(currentuseruid)){
            followingfriend.setVisibility(View.GONE);
            followfriend.setVisibility(View.GONE);
            messagefriend.setVisibility(View.GONE);
        }

        if(uid.equals("LC9UdKFWNUYJI3HZ5P3tZRO9WlM2")){
            verifiedLogo.setVisibility(View.VISIBLE);
        }

        mDatabase = FirebaseDatabase.getInstance();
        friendReference = mDatabase.getReference("Users/"+uid+"/");
        notificationReference = mDatabase.getReference("Notifications/"+uid+"/");
        userReference = mDatabase.getReference("Users/"+currentuseruid+"/");

        followingReference = userReference.child("Following/");


        followingReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    for(DataSnapshot followingsnapshot: dataSnapshot.getChildren()){
                        if(followingsnapshot.getKey().toString().equals(uid)){
                            followfriend.setVisibility(View.GONE);
                            followingfriend.setVisibility(View.VISIBLE);
                        }
                    };
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        friendReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String dpurl = dataSnapshot.child("displayimage").getValue().toString();
                String username = dataSnapshot.child("username").getValue().toString();
                String fullname = dataSnapshot.child("fullname").getValue().toString();

                if(dataSnapshot.child("seenstatus").exists()){
                    if(dataSnapshot.child("seenstatus").getValue().toString().equals("online")){
                        seenstatus.setImageResource(R.drawable.online);
                        usernameToolbar.setText(username+ " (online)");
                    }
                    else{
                        String lastseen = dataSnapshot.child("seenstatus").getValue().toString();
                        usernameToolbar.setText(username+ " (last seen at " + lastseen + ")");
                    }
                }

                if(dataSnapshot.child("status").exists()){
                    String status = dataSnapshot.child("status").getValue().toString();
                    userstatus.setText(status);
                }


                Glide.with(getApplicationContext()).load(dpurl).into(userdp);

                userfullname.setText(fullname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();


//Created by AkulSrivastava
//May 2019


        Date sentMessageTime = Calendar.getInstance().getTime();
        DateFormat newFormat = new SimpleDateFormat("hh:mm a");
        String time = newFormat.format(sentMessageTime);

        status(time);
    }

    @Override
    protected void onResume() {
        super.onResume();

        status("online");
    }

    public void status(String status){

        HashMap<String,Object> map1 = new HashMap<>();
        map1.put("seenstatus",status);

        userReference.updateChildren(map1);

    }

    public void followfriend(View view) {

        Toast.makeText(this, "Followed", Toast.LENGTH_SHORT).show();

        friendReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("fullname").getValue().toString();
                String url = dataSnapshot.child("displayimage").getValue().toString();

                HashMap<String,String> map = new HashMap<>();
                map.put("fullname",name);
                map.put("displayimage",url);
                map.put("uid",uid);

                userReference.child("Following").child(uid).setValue(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("fullname").getValue().toString();
                String url = dataSnapshot.child("displayimage").getValue().toString();

                HashMap<String,String> map = new HashMap<>();
                map.put("fullname",name);
                map.put("displayimage",url);
                map.put("uid",currentuseruid);

                friendReference.child("Followers").child(currentuseruid).setValue(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //notification
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String username = dataSnapshot.child("fullname").getValue().toString();
                final String imgurl = dataSnapshot.child("displayimage").getValue().toString();
                final String email = dataSnapshot.child("email").getValue().toString();

                HashMap<String,String> notificationmap = new HashMap<>();
                notificationmap.put("notification",username+" follows you.");
                notificationmap.put("status","false");
                notificationmap.put("image",imgurl);

                notificationReference.child(username+" followsyou").setValue(notificationmap);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        followfriend.setVisibility(View.GONE);
        followingfriend.setVisibility(View.VISIBLE);

    }

    public void unfollowfriend(View view) {

        Toast.makeText(this, "Unfollowed", Toast.LENGTH_SHORT).show();

        followfriend.setVisibility(View.VISIBLE);
        followingfriend.setVisibility(View.GONE);

        DatabaseReference friendfollowerref = mDatabase.getReference("Users/"+uid+"/"+"Followers/");

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String fname = dataSnapshot.child("fullname").getValue().toString();

                notificationReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(fname+" followsyou").child("notification")
                                .getValue().toString().equals(fname+" follows you."))
                        {
                            notificationReference.child(fname+" followsyou").removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        friendfollowerref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot followersnap: dataSnapshot.getChildren()){
                        if(followersnap.getKey().toString().equals(currentuseruid)){
                            followersnap.getRef().removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//Created by AkulSrivastava
//May 2019


        followingReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot followsnap: dataSnapshot.getChildren()){
                        if(followsnap.getKey().toString().equals(uid)){
                            followsnap.getRef().removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

//Created by AkulSrivastava
//May 2019

    public void messageUser(View view) {
        Intent i = new Intent(getApplicationContext(),ChatActivity.class);
        i.putExtra("frienduid",uid);
        startActivity(i);
    }
}


//Created by AkulSrivastava
//May 2019
