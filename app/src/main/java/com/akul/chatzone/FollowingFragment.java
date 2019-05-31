package com.akul.chatzone;


//Created by AkulSrivastava
//May 2019

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

//Created by AkulSrivastava
//May 2019

public class FollowingFragment extends Fragment {

    View view;


    RecyclerView followingRecycler;
    List<Following> followingList;
    FollowingAdapter followingAdapter;

    FirebaseAuth firebaseAuth;
    String currentuid;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference followingRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_following, container, false);

        followingRecycler = view.findViewById(R.id.followingRecycler);
        followingRecycler.setHasFixedSize(true);
        followingRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseAuth = FirebaseAuth.getInstance();
        currentuid = firebaseAuth.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();

        followingList = new ArrayList<>();

        followingRef = firebaseDatabase.getReference("Users/"+currentuid+"/Following/");

        followingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    for(DataSnapshot ds: dataSnapshot.getChildren()){

                        Following following = ds.getValue(Following.class);
                        followingList.add(following);

                    }

                    followingAdapter = new FollowingAdapter(getContext(),followingList);
                    followingRecycler.setAdapter(followingAdapter);

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
