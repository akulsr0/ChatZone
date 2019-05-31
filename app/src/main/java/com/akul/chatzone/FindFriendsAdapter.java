package com.akul.chatzone;

//Created by AkulSrivastava
//May 2019

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//Created by AkulSrivastava
//May 2019

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FindFriendsAdapter extends RecyclerView.Adapter<FindFriendsAdapter.FFviewholder>
{

    Context context;
    List<UserInfo> usersList;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }


    public FindFriendsAdapter(Context context, List<UserInfo> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public FFviewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_user,viewGroup,false);
        FFviewholder holder = new FFviewholder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FFviewholder fFviewholder, int i) {

        UserInfo user = usersList.get(i);

        String uid1 = usersList.get(i).getUid();

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userReference = mDatabase.getReference("Users/"+uid1+"/");

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("seenstatus").exists()){
                    if(dataSnapshot.child("seenstatus").getValue().toString().equals("online")){
                        fFviewholder.seenstatus.setImageResource(R.drawable.online);
                    }
                    else {
                        fFviewholder.seenstatus.setImageResource(0);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fFviewholder.userfullname.setText(user.getFullname());

        setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, Boolean isLongClick) {

                String uid = usersList.get(position).getUid();

                Intent i = new Intent(context, FriendProfile.class);
                i.putExtra("uid",uid);
                context.startActivity(i);

            }
        });

        Glide.with(context).load(user.getDisplayimage()).into(fFviewholder.userDP);


    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public void filterList(ArrayList<UserInfo> filteredList)
    {
        usersList = filteredList;
        notifyDataSetChanged();
    }


    class FFviewholder extends RecyclerView.ViewHolder{

        ImageView userDP, seenstatus;
        TextView userfullname;

        public FFviewholder(@NonNull final View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onClick(itemView, getAdapterPosition(), false);
                }
            });

            seenstatus = itemView.findViewById(R.id.seenstatus);


            userDP = itemView.findViewById(R.id.ffuserdp);
            userfullname = itemView.findViewById(R.id.ffuserfullname);


        }
    }


}

//Created by AkulSrivastava
//May 2019
