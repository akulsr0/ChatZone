package com.akul.chatzone;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//Created by AkulSrivastava
//May 2019

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Notifications extends Fragment {

    View view;

    RecyclerView notificationRecycler;
    NotificationAdapter adapter;
    List<Notification> notificationList;

    FirebaseAuth mAuth;
    String uid;
    FirebaseDatabase mDatabase;
    DatabaseReference notificationReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notifications, container, false);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        notificationReference = mDatabase.getReference("Notifications/"+uid+"/");

        notificationRecycler = view.findViewById(R.id.notificationRecycler);
        notificationRecycler.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        layoutManager.setSmoothScrollbarEnabled(false);

        notificationRecycler.setLayoutManager(layoutManager);

        notificationList = new ArrayList<>();

        notificationReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot notificationsnap: dataSnapshot.getChildren()){
                        Notification notification = notificationsnap.getValue(Notification.class);
                        notificationList.add(notification);
                    }
                    adapter = new NotificationAdapter(getContext(),notificationList);
                    notificationRecycler.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

}

//Created by AkulSrivastava
//May 2019
