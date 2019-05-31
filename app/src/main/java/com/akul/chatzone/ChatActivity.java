package com.akul.chatzone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//Created by AkulSrivastava
//May 2019

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.ios.IosEmojiProvider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity
{

    ImageView cabackbutton, causerimage;
    TextView causername, caseenstatus;

    String friendUID, currenuseruid;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference userReference, friendReference, messageReference;

    RecyclerView chatRecycler;
    List<Message> msgList;
    MessageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        //Created by AkulSrivastava
        //May 2019


        EmojiManager.install(new IosEmojiProvider());

        mAuth = FirebaseAuth.getInstance();
        currenuseruid = mAuth.getUid();

        friendUID = getIntent().getStringExtra("frienduid");

        caseenstatus = findViewById(R.id.CAseenstatus);

        cabackbutton = findViewById(R.id.CAbackbutton);
        cabackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),UserActivity.class);
                startActivity(i);
            }
        });

        causerimage = findViewById(R.id.CAuserimage);
        causerimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),FriendProfile.class);
                i.putExtra("uid",friendUID);
                startActivity(i);
            }
        });

        causername = findViewById(R.id.CAusername);
        causername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),FriendProfile.class);
                i.putExtra("uid",friendUID);
                startActivity(i);
            }
        });

        chatRecycler = findViewById(R.id.chatRecycler);
        chatRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        layoutManager.setSmoothScrollbarEnabled(false);
        chatRecycler.setLayoutManager(layoutManager);


        mDatabase = FirebaseDatabase.getInstance();

        userReference = mDatabase.getReference("Users/"+currenuseruid+"/");

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("fullname").getValue().toString();
                String img = dataSnapshot.child("displayimage").getValue().toString();

                SharedPreferences pref = getSharedPreferences("msgpref",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("name",name);
                editor.putString("img",img);
                editor.apply();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        friendReference = mDatabase.getReference("Users/"+friendUID+"/");
        messageReference = mDatabase.getReference("Messages/");

        String msgid;
        String[] array1={currenuseruid.toLowerCase(),friendUID.toLowerCase()};
        String[] array2={currenuseruid.toLowerCase(),friendUID.toLowerCase()};
        Arrays.sort(array2);
        if (Arrays.equals(array1, array2)){
            msgid = currenuseruid+friendUID;
        }
        else{
            msgid = friendUID+currenuseruid;
        }

        final DatabaseReference msgviewref = mDatabase.getReference("Messages/"+msgid+"/");

        msgList = new ArrayList<>();

        msgviewref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot chatSnap: dataSnapshot.getChildren()){
                        Message message = chatSnap.getValue(Message.class);
                        msgList.add(message);
                    }
                    adapter = new MessageAdapter(getApplicationContext(),msgList);
                    chatRecycler.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        msgviewref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                msgList.clear();


                msgviewref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot chatSnap: dataSnapshot.getChildren()){
                                Message message = chatSnap.getValue(Message.class);
                                msgList.add(message);
                            }
                            adapter = new MessageAdapter(getApplicationContext(),msgList);
                            chatRecycler.setAdapter(adapter);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        friendReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("fullname").getValue().toString();
                String imgurl = dataSnapshot.child("displayimage").getValue().toString();
                String seenstatus = dataSnapshot.child("seenstatus").getValue().toString();
                if(!seenstatus.equals("online")){
                    caseenstatus.setText("last seen at "+seenstatus);
                }
                else{
                    caseenstatus.setText(seenstatus);
                }
                causername.setText(name);
                Glide.with(getApplicationContext()).load(imgurl).into(causerimage);

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

    public void status(String status){

        HashMap<String,Object> map1 = new HashMap<>();
        map1.put("seenstatus",status);

        userReference.updateChildren(map1);

    }

    public void sendMessage(final View view) {

        EditText message = findViewById(R.id.messagetext);
        String msgid;
        String strmessage = message.getText().toString();

        String[] array1={currenuseruid.toLowerCase(),friendUID.toLowerCase()};
        String[] array2={currenuseruid.toLowerCase(),friendUID.toLowerCase()};
        Arrays.sort(array2);
        if (Arrays.equals(array1, array2)){
            msgid = currenuseruid+friendUID;
        }
        else{
            msgid = friendUID+currenuseruid;
        }

        Date sentMessageTime = Calendar.getInstance().getTime();
        DateFormat newFormat = new SimpleDateFormat("hh:mm a");
        String time = newFormat.format(sentMessageTime);


        SharedPreferences preferences = getSharedPreferences("msgpref",MODE_PRIVATE);
        String fullname = preferences.getString("name",null);
        final String imageurl = preferences.getString("img",null);

        HashMap<String,String> msgmap = new HashMap<>();
        msgmap.put("time",time);
        msgmap.put("message",strmessage);
        msgmap.put("name",fullname);
        msgmap.put("imgurl",imageurl);

        messageReference.child(msgid).push().setValue(msgmap);

        friendReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("fullname").getValue().toString();
                String iurl = dataSnapshot.child("displayimage").getValue().toString();

                HashMap<String,String> viewmap = new HashMap<>();
                viewmap.put("name",name);
                viewmap.put("imgurl",iurl);
                viewmap.put("uid",friendUID);

                messageReference.child("View").child(currenuseruid).child(friendUID).setValue(viewmap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("fullname").getValue().toString();
                String iurl = dataSnapshot.child("displayimage").getValue().toString();

                HashMap<String,String> viewmap = new HashMap<>();
                viewmap.put("name",name);
                viewmap.put("imgurl",iurl);
                viewmap.put("uid",currenuseruid);

                messageReference.child("View").child(friendUID).child(currenuseruid).setValue(viewmap);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        message.setText("");

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);


    }

}


//Created by AkulSrivastava
//May 2019

