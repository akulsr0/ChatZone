package com.akul.chatzone;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;


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


public class FindFriends extends Fragment {

    View view;
    RecyclerView ffRecycler;
    FindFriendsAdapter adapter;
    List<UserInfo> users;

    EditText searchUser;

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference userReference;

    String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_find_friends, container, false);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        userReference = mDatabase.getReference("Users/");

        ffRecycler = view.findViewById(R.id.ffRecycler);
        ffRecycler.setHasFixedSize(true);
        ffRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        users = new ArrayList<>();

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                        UserInfo user = userSnapshot.getValue(UserInfo.class);
                        users.add(user);
                    }

                    adapter = new FindFriendsAdapter(getContext(),users);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        searchUser = view.findViewById(R.id.searchUser);
        searchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterRV(s.toString());
            }
        });

        return view;
    }


    private void filterRV(String text)
    {
        ArrayList<UserInfo> filteredList = new ArrayList<>();
        for(UserInfo i: users)
        {
            if(i.getFullname().toLowerCase().startsWith(text.toLowerCase()))
            {
                filteredList.add(i);
            }
        }
        adapter.filterList(filteredList);


        if(text.isEmpty()){
            filteredList.clear();
        }

        ffRecycler.setAdapter(adapter);


    }

}


//Created by AkulSrivastava
//May 2019
