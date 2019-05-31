package com.akul.chatzone;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//Created by AkulSrivastava
//May 2019

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

//Created by AkulSrivastava
//May 2019


public class Home extends Fragment {

    View view;

    public static final int SELECTED=101;

    EditText PpostTitle;
    ImageView postGallery, post, togglepostlay;
    TextView layhelp;
    RelativeLayout postContainer;
    Uri postURI;

    String uid;

    RecyclerView postsrecycler;
    PostAdapter postAdapter;
    List<Post> postList;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference postReference, userReference;

    FirebaseStorage firebaseStorage;
    StorageReference postStorageReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_home, container, false);

        PpostTitle = view.findViewById(R.id.postEditText);

        postContainer = view.findViewById(R.id.postContainer);

        layhelp = view.findViewById(R.id.layhelp);

        togglepostlay = view.findViewById(R.id.togglepostlay);
        togglepostlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(postContainer.getVisibility()==View.VISIBLE){
                    layhelp.setVisibility(View.VISIBLE);
                    togglepostlay.setImageResource(R.drawable.downbtn);
                    postContainer.setVisibility(View.GONE);
                }
                else {
                    layhelp.setVisibility(View.INVISIBLE);
                    togglepostlay.setImageResource(R.drawable.upbtn);
                    postContainer.setVisibility(View.VISIBLE);
                }
            }
        });

        postGallery = view.findViewById(R.id.postGallery);
        postGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImg();
            }
        });

        post = view.findViewById(R.id.post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }
        });

        postsrecycler = view.findViewById(R.id.postsRecycler);
        postsrecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        postsrecycler.setLayoutManager(layoutManager);


        //Fireabase

        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        postReference = firebaseDatabase.getReference("Posts/");
        postList = new ArrayList<>();

        postReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    postList.clear();
                    for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                        Post post = postSnapshot.getValue(Post.class);
                        postList.add(post);
                    }
                    postAdapter = new PostAdapter(getContext(),postList);
                    postsrecycler.setAdapter(postAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        firebaseStorage = FirebaseStorage.getInstance();
        postStorageReference = firebaseStorage.getReference("Posts/"+uid+"/");


        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        return view;
    }

    private void chooseImg()
    {
        Intent picPick = new Intent();
        picPick.setType("image/*");
        picPick.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(picPick,SELECTED);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==SELECTED && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            postURI = data.getData();

            try{
                Bitmap postBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),postURI);
            }
            catch (IOException e){
                e.printStackTrace();
            }


        }

    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void post(){

        final String postTitle = PpostTitle.getText().toString();





        if(postURI!=null){
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            final StorageReference postRef = postStorageReference.child(System.currentTimeMillis()+"."+getFileExtension(postURI));

            postRef.putFile(postURI).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                }
            })
                    .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                        {

                            if(!task.isSuccessful())
                            {
                                throw task.getException();
                            }
                            return postRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    progressDialog.dismiss();
                    Uri downuri = task.getResult();
                    final String url = downuri.toString();

                    userReference = firebaseDatabase.getReference("Users/"+uid+"/");

                    userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String uname = dataSnapshot.child("fullname").getValue().toString();
                            String uurl = dataSnapshot.child("displayimage").getValue().toString();

                            HashMap<String,String> map = new HashMap<>();
                            map.put("imgurl",url);
                            map.put("uuid",uid);
                            map.put("title",postTitle);
                            map.put("uname",uname);
                            map.put("uurl",uurl);

                            postReference.push().setValue(map);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });


        }


    }
//Created by AkulSrivastava
//May 2019




}
