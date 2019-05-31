package com.akul.chatzone;

//Created by AkulSrivastava
//May 2019

import android.content.Context;
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

import java.util.List;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.vholder>
{

    Context mctx;
    List<Following> followingList;

    public FollowingAdapter(Context mctx, List<Following> followingList) {
        this.mctx = mctx;
        this.followingList = followingList;
    }

    @NonNull
    @Override
    public vholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mctx).inflate(R.layout.layout_following,viewGroup,false);
        vholder vholder = new vholder(view);
        return vholder;

    }

    @Override
    public void onBindViewHolder(@NonNull vholder vholder, int i) {

        Following following = followingList.get(i);

        vholder.fuserfullname.setText(following.getFullname());
        Glide.with(mctx).load(following.getDisplayimage()).into(vholder.fuserdp);

    }

    @Override
    public int getItemCount() {
        return followingList.size();
    }

    class vholder extends RecyclerView.ViewHolder{

        ImageView fuserdp;
        TextView fuserfullname;

        public vholder(@NonNull View itemView) {
            super(itemView);

            fuserdp = itemView.findViewById(R.id.Fuserdp);
            fuserfullname = itemView.findViewById(R.id.Fuserfullname);


        }
    }

}

//Created by AkulSrivastava
//May 2019
