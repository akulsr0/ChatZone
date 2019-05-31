package com.akul.chatzone;

//Created by AkulSrivastava
//May 2019


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import java.util.Stack;

public class FollowersFragment extends Fragment {

    View view;

    RecyclerView followerRecycler;
    List<Follower> followerList;
    FollowersAdapter adapter;

//Created by AkulSrivastava
//May 2019

    FirebaseAuth mAuth;
    String uid;
    FirebaseDatabase mDatabase;
    DatabaseReference followerReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_followers, container, false);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();

        followerRecycler = view.findViewById(R.id.followerRecycler);
        followerRecycler.setHasFixedSize(true);
        followerRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        mDatabase = FirebaseDatabase.getInstance();
        followerReference = mDatabase.getReference("Users/"+uid+"/Followers/");

        followerList = new ArrayList<>();

        followerReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for(DataSnapshot ds: dataSnapshot.getChildren()){

                        Follower follower = ds.getValue(Follower.class);
                        followerList.add(follower);

                    }

                    adapter = new FollowersAdapter(getContext(),followerList);
                    followerRecycler.setAdapter(adapter);

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
