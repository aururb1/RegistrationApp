package com.example.urbon.registrationapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;

import com.example.urbon.registrationapp.utils.CustomToasts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by urbon on 2/16/2018.
 */

public class Firebase {
    private FirebaseAuth auth;
    private Activity activity;
    private FirebaseUser currentUser;
    private CustomToasts toasts;
    private ProgressDialog progressDialog;
    DatabaseReference databaseReference;

    public Firebase(Activity activity) {
        auth = FirebaseAuth.getInstance();
        this.activity = activity;
        currentUser = auth.getCurrentUser();
        toasts = new CustomToasts(activity);
        databaseReference = FirebaseDatabase.getInstance().getReference("z5bKBNMQj2POjbcEKjkeJ1AgOUJ3/owners");
    }

    public void signIn(String email, String password) {
        if (!email.isEmpty() && !password.isEmpty()) {
            showProgressDialog();
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                currentUser = auth.getCurrentUser();
                            } else {
                                toasts.longToast(activity.getString(R.string.wrong_email_password));
                            }
                            hideProgressDialog();
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

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage(activity.getString(R.string.signed_in));
        }
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
