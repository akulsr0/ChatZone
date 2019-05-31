package com.akul.chatzone;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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


public class ChatFragment extends Fragment {


//Created by AkulSrivastava
//May 2019


    View view;

    RecyclerView chatfragrecycler;
    ChatAdapter adapter;
    List<ChatUserGet> chatUserList;

    FirebaseAuth mAuth;
    String uid;
    FirebaseDatabase mDatabase;
    DatabaseReference messageviewReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);

        chatfragrecycler = view.findViewById(R.id.chatsfragrecycler);
        chatfragrecycler.setHasFixedSize(true);
        chatfragrecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        mDatabase = FirebaseDatabase.getInstance();

        messageviewReference = mDatabase.getReference("Messages/View/"+uid+"/");

        chatUserList = new ArrayList<>();

        messageviewReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                        ChatUserGet user = userSnapshot.getValue(ChatUserGet.class);
                        chatUserList.add(user);
                    }


                    adapter = new ChatAdapter(getContext(),chatUserList);
                    chatfragrecycler.setAdapter(adapter);

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
