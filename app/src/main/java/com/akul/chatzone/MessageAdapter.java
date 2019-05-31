package com.akul.chatzone;

//Created by AkulSrivastava
//May 2019

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//Created by AkulSrivastava
//May 2019

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.vholder>
{

    Context mctx;
    List<Message> messageList;


    public MessageAdapter(Context mctx, List<Message> messageList) {
        this.mctx = mctx;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public vholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view;

        view = LayoutInflater.from(mctx).inflate(R.layout.layout_message,viewGroup,false);
        vholder vholder = new vholder(view);

        return vholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final vholder vholder, int i) {

        final Message message = messageList.get(i);

        vholder.LMmessage.setText(message.getMessage());
        vholder.LMmsgtime.setText(message.getTime());
        Glide.with(mctx).load(message.getImgurl()).into(vholder.LMmsgimg);

        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getUid();
        FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();
        DatabaseReference userref = mdatabase.getReference("Users/"+uid+"/");
        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("fullname").getValue().toString();
                if(name.equals(message.getName())){

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)vholder.msgcard.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    vholder.msgcard.setLayoutParams(params);

                }

                else {


                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)vholder.msgcard.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    vholder.msgcard.setLayoutParams(params);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }


    class vholder extends RecyclerView.ViewHolder{

        ImageView LMmsgimg;
        TextView LMmessage, LMmsgtime;
        RelativeLayout msgcard;

        public vholder(@NonNull View itemView) {
            super(itemView);

            msgcard = itemView.findViewById(R.id.laymessage);
            LMmessage = itemView.findViewById(R.id.LMmessage);
            LMmsgimg = itemView.findViewById(R.id.LMmsgimg);
            LMmsgtime = itemView.findViewById(R.id.LMmsgtime);


        }
    }
}

//Created by AkulSrivastava
//May 2019
