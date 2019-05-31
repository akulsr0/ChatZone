package com.akul.chatzone;

//Created by AkulSrivastava
//May 2019

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//Created by AkulSrivastava
//May 2019

import com.bumptech.glide.Glide;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.vholder>
{

    Context mcontext;
    List<Post> postList;

    public PostAdapter(Context mcontext, List<Post> postList) {
        this.mcontext = mcontext;
        this.postList = postList;
    }

    @NonNull
    @Override
    public vholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mcontext).inflate(R.layout.layout_post,viewGroup,false);
        vholder vholder = new vholder(view);

        return vholder;
    }

    @Override
    public void onBindViewHolder(@NonNull vholder vholder, int i) {

        Post post = postList.get(i);

        Glide.with(mcontext).load(post.getImgurl()).into(vholder.ppostimage);
        Glide.with(mcontext).load(post.getUurl()).into(vholder.puserimage);
        vholder.pposttitle.setText(post.getTitle());
        vholder.pusername.setText(post.getUname());

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class vholder extends RecyclerView.ViewHolder{

        ImageView puserimage, ppostimage;
        TextView pusername, pposttitle;


        public vholder(@NonNull View itemView) {
            super(itemView);

            puserimage = itemView.findViewById(R.id.Puserimg);
            ppostimage = itemView.findViewById(R.id.PpostImage);
            pusername = itemView.findViewById(R.id.Pusername);
            pposttitle = itemView.findViewById(R.id.PpostTitle);


        }
    }

}

//Created by AkulSrivastava
//May 2019
