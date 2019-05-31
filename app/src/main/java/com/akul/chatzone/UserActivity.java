package com.akul.chatzone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

//Created by AkulSrivastava
//May 2019

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class UserActivity extends AppCompatActivity
{

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference userReference;

    String uid;
    TextView welcometext, userfullnamenavbar, navProfile, navFindFriends;
    TextView navHome;
    ProgressBar welcomeprogressbar;

    private Toolbar toolbar;

    FrameLayout mainframe;

    ImageView profileImageHomeToolbar, seenstatusnavbar;
    ImageView profileImageNavBar, usertoolbarseenstatus;

    DrawerLayout drawerLayout;

//Created by AkulSrivastava
//May 2019


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame,new Home());
        fragmentTransaction.commit();

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        userReference = mDatabase.getReference("Users/"+uid+"/");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        usertoolbarseenstatus = findViewById(R.id.usertoolbarseenstatus);
        seenstatusnavbar = findViewById(R.id.seenstatusnavBar);

        mainframe = findViewById(R.id.mainFrame);

        navFindFriends = findViewById(R.id.navFindFriends);
        navFindFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(Gravity.RIGHT);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainFrame,new FindFriends());
                fragmentTransaction.commit();
            }
        });

        navHome = findViewById(R.id.navHome);
        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(Gravity.RIGHT);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainFrame,new Home());
                fragmentTransaction.commit();
            }
        });

        navProfile = findViewById(R.id.navProfile);
        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        userfullnamenavbar = findViewById(R.id.userfullnamenavbar);
        userfullnamenavbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        profileImageHomeToolbar = findViewById(R.id.profileImageHomeToolbar);
        profileImageHomeToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        profileImageNavBar = findViewById(R.id.profileImageNavBar);
        profileImageNavBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        drawerLayout = findViewById(R.id.drawerLayout);

        welcometext = findViewById(R.id.userTitle);
        welcomeprogressbar = findViewById(R.id.welcomeProgressBar);
        welcomeprogressbar.setVisibility(View.VISIBLE);

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("username").getValue().toString();
                welcometext.setText(username);
                welcomeprogressbar.setVisibility(View.INVISIBLE);

                if(dataSnapshot.child("seenstatus").exists()){
                    if(dataSnapshot.child("seenstatus").getValue().toString().equals("online")){
                        usertoolbarseenstatus.setImageResource(R.drawable.online);
                        seenstatusnavbar.setImageResource(R.drawable.online);
                    }
                    else {
                        usertoolbarseenstatus.setImageResource(0);
                        seenstatusnavbar.setImageResource(0);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = dataSnapshot.child("displayimage").getValue().toString();
                if(dataSnapshot.child("fullname").exists()){
                    String fname = dataSnapshot.child("fullname").getValue().toString();
                    userfullnamenavbar.setText(fname);
                }
                if(dataSnapshot.child("displayimage").equals("null")){
                    profileImageHomeToolbar.setImageResource(R.drawable.useredit);
                    profileImageNavBar.setImageResource(R.drawable.useredit);
                }
                else{
                    Glide.with(UserActivity.this).load(url).into(profileImageHomeToolbar);
                    Glide.with(UserActivity.this).load(url).into(profileImageNavBar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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

    public void profileButton(View view) {
        Intent i = new Intent(this,ProfileActivity.class);
        startActivity(i);
    }

    public void menuButton(View view) {

        if(drawerLayout.isDrawerOpen(GravityCompat.END)){
            drawerLayout.closeDrawer(Gravity.RIGHT);
        }
        else{
            drawerLayout.openDrawer(Gravity.RIGHT);
        }


    }


    public void homeButton(View view) {
        drawerLayout.closeDrawer(Gravity.RIGHT);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame,new Home());
        fragmentTransaction.commit();
    }

    public void status(String status){

        HashMap<String,Object> map1 = new HashMap<>();
        map1.put("seenstatus",status);

        userReference.updateChildren(map1);

    }

    public void notificationFrag(View view) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame,new Notifications());
        fragmentTransaction.commit();

    }

    public void logoutuser(View view) {
        mAuth.signOut();
        Toast.makeText(getApplicationContext(), "User Logged Out", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    public void FollowingFrag(View view) {
        drawerLayout.closeDrawer(Gravity.RIGHT);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame,new FollowingFragment());
        fragmentTransaction.commit();
    }

    public void FollowersFrag(View view) {
        drawerLayout.closeDrawer(Gravity.RIGHT);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame,new FollowersFragment());
        fragmentTransaction.commit();
    }

    public void chatsfrag(View view) {
        drawerLayout.closeDrawer(Gravity.RIGHT);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame,new ChatFragment());
        fragmentTransaction.commit();
    }
}
//Created by AkulSrivastava
//May 2019
