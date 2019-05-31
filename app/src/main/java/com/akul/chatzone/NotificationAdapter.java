package com.akul.chatzone;

//Created by AkulSrivastava
//May 2019

import android.content.Context;
import android.media.Image;
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

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.nHolder>
{

    Context mcontext;
    List<Notification> notificationList;

    public NotificationAdapter(Context mcontext, List<Notification> notificationList) {
        this.mcontext = mcontext;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public nHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mcontext).inflate(R.layout.layout_notification,viewGroup,false);
        nHolder holder = new nHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull nHolder nHolder, int i) {

        Notification notification = notificationList.get(i);

        nHolder.Notification.setText(notification.getNotification());
        Glide.with(mcontext).load(notification.getImage()).into(nHolder.userimage);

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    class nHolder extends RecyclerView.ViewHolder{

        ImageView userimage;
        TextView Notification;

        public nHolder(@NonNull View itemView) {
            super(itemView);

            userimage = itemView.findViewById(R.id.userimage);
            Notification = itemView.findViewById(R.id.Notification);
        }
    }

}

//Created by AkulSrivastava
//May 2019
