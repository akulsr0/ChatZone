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

import org.w3c.dom.Text;

import java.util.List;

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.followersholder>
{

    Context mcontext;
    List<Follower> followersList;

    public FollowersAdapter(Context mcontext, List<Follower> followersList) {
        this.mcontext = mcontext;
        this.followersList = followersList;
    }

    @NonNull
    @Override
    public followersholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.layout_follower,viewGroup,false);
        followersholder holder = new followersholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull followersholder followersholder, int i) {

        Follower follower = followersList.get(i);

        followersholder.LFuserfullname.setText(follower.getFullname());
        Glide.with(mcontext).load(follower.getDisplayimage()).into(followersholder.LFuserdp);
    }

    @Override
    public int getItemCount() {
        return followersList.size();
    }

    class followersholder extends RecyclerView.ViewHolder{

        ImageView LFuserdp, LFseenstatus;
        TextView LFuserfullname;

        public followersholder(@NonNull View itemView) {
            super(itemView);

            LFuserdp = itemView.findViewById(R.id.LFuserdp);
            LFseenstatus = itemView.findViewById(R.id.LFseenstatus);
            LFuserfullname = itemView.findViewById(R.id.LFuserfullname);

        }
    }
}

//Created by AkulSrivastava
//May 2019
