package com.example.urbon.registrationapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.urbon.registrationapp.activities.SigninActivity;
import com.example.urbon.registrationapp.utils.CustomToasts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by urbon on 2/16/2018.
 */

public class Firebase {
    private FirebaseAuth auth;
    private Activity activity;
    private FirebaseUser currentUser;
    private CustomToasts toasts;

    public Firebase(Activity activity) {
        auth = FirebaseAuth.getInstance();
        this.activity = activity;
        currentUser = auth.getCurrentUser();
        toasts = new CustomToasts(activity);
    }

    public void signIn(String email, String password) {
        if (!email.isEmpty() && !password.isEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                currentUser = auth.getCurrentUser();
                            } else {
                                toasts.longToast(activity.getString(R.string.wrong_email_password));
                            }
                        }
                    });
        } else {
            toasts.longToast(activity.getString(R.string.empty_fields));
        }
    }

    public void signOut() {
        auth.signOut();
    }

    public FirebaseAuth getAuth() {
        return auth;
    }
}
