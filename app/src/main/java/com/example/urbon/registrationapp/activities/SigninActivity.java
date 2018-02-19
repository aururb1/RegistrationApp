package com.example.urbon.registrationapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.urbon.registrationapp.Firebase;
import com.example.urbon.registrationapp.R;
import com.example.urbon.registrationapp.utils.CustomToasts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by urbon on 2/14/2018.
 */

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.signIn)
    Button signInButton;

    Firebase firebase;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle("Sign in");
        firebase = new Firebase(this);
        setAuthStateListener();
        signInButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebase.getAuth().addAuthStateListener(authStateListener);
    }

    @Override
    public void onClick(View view) {
        firebase.signIn(email.getText().toString(), password.getText().toString());
    }

    private void setAuthStateListener() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startAnotherActivity();
                    finish();
                }
            }
        };
    }

    private void startAnotherActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
