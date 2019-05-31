package com.akul.chatzone;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


//Created by AkulSrivastava
//May 2019


import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.vholder>
{

    Context mctx;
    List<ChatUserGet> userGetList;

    public ChatAdapter(Context mctx, List<ChatUserGet> userGetList) {
        this.mctx = mctx;
        this.userGetList = userGetList;
    }

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public vholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mctx).inflate(R.layout.layout_userchat,viewGroup,false);
        vholder holder = new vholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final vholder vholder, int i) {

        ChatUserGet user = userGetList.get(i);

        vholder.cfuserfullname.setText(user.getName());
        Glide.with(mctx).load(user.getImgurl()).into(vholder.cfuserdp);


        String uid1 = userGetList.get(i).getUid();

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userReference = mDatabase.getReference("Users/"+uid1+"/");

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("seenstatus").exists()){
                    if(dataSnapshot.child("seenstatus").getValue().toString().equals("online")){
                        vholder.cfseenstatus.setImageResource(R.drawable.online);
                    }
                    else {
                        vholder.cfseenstatus.setImageResource(0);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, Boolean isLongClick) {

                String uid = userGetList.get(position).getUid();

                Intent i = new Intent(mctx, ChatActivity.class);
                i.putExtra("frienduid",uid);
                mctx.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return userGetList.size();
    }

    class vholder extends RecyclerView.ViewHolder{

        ImageView cfuserdp, cfseenstatus;
        TextView cfuserfullname;

        public vholder(@NonNull final View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onClick(itemView, getAdapterPosition(), false);
                }
            });

            cfuserdp = itemView.findViewById(R.id.CFuserdp);
            cfseenstatus = itemView.findViewById(R.id.CFseenstatus);
            cfuserfullname = itemView.findViewById(R.id.CFuserfullname);


        }
    }

}


//Created by AkulSrivastava
//May 2019
