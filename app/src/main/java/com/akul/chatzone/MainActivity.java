package com.akul.chatzone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

//Created by AkulSrivastava
//May 2019

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
{

    EditText logemail, logpassword, regusername, regemail,regpassword;

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference userReference;

    ProgressBar loginProgressBar, registerProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        logemail = findViewById(R.id.logemail);
        logpassword = findViewById(R.id.logpassword);
        regusername = findViewById(R.id.regusername);
        regemail = findViewById(R.id.regemail);
        regpassword = findViewById(R.id.regpassword);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();


        userReference = mDatabase.getReference("Users/");

        loginProgressBar = findViewById(R.id.loginprogressbar);
        registerProgressBar = findViewById(R.id.registrationprogressbar);


    }


    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser()!=null){
            Intent i = new Intent(MainActivity.this, UserActivity.class);
            startActivity(i);
            finish();
        }

    }

    public void registerUser(View view) {

        final String username = regusername.getText().toString();
        final String email = regemail.getText().toString();
        final String password = regpassword.getText().toString();

        registerProgressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            registerProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, "User Registered", Toast.LENGTH_SHORT).show();

                            String uid = mAuth.getUid();

                            HashMap<String,String> userData = new HashMap<>();
                            userData.put("username", username);
                            userData.put("email",email);
                            userData.put("password",password);
                            userData.put("displayimage","null");
                            userData.put("seenstatus","null");
                            userData.put("uid",uid);
                            userData.put("fullname",username);
                            userData.put("displayimage","https://firebasestorage.googleapis.com/v0/b/chatzone-549a5.appspot.com/o/user.png?alt=media&token=e60f4b10-b7b0-422e-b70b-e259300a5699");

                            userReference.child(uid).setValue(userData);

                        }
                        else{
                            registerProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

    public void loginUser(View view) {

        String email = logemail.getText().toString();
        String password = logpassword.getText().toString();

        loginProgressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            loginProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, "Login Succesuful", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(MainActivity.this,UserActivity.class);
                            startActivity(i);

                        }
                        else {
                            loginProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

}

//Created by AkulSrivastava
//May 2019
