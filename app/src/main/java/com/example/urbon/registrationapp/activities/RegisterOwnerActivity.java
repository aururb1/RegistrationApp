package com.example.urbon.registrationapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.urbon.registrationapp.Firebase;
import com.example.urbon.registrationapp.R;
import com.example.urbon.registrationapp.models.Owner;
import com.example.urbon.registrationapp.utils.Const;
import com.example.urbon.registrationapp.utils.CustomToasts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by urbon on 3/8/2018.
 */

public class RegisterOwnerActivity extends AppCompatActivity
        implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ownerName)
    EditText ownerName;
    @BindView(R.id.ownerSurname)
    EditText ownerSurname;
    @BindView(R.id.ownerEmail)
    EditText ownerEmail;
    @BindView(R.id.ownerPhone)
    EditText ownerPhone;
    @BindView(R.id.ownerAddress)
    EditText ownerAddress;
    @BindView(R.id.save)
    Button next;

    private Firebase firebase;
    private List<Owner> ownerList;
    private Owner owner;
    private String path;
    private LinkedHashMap<String, Owner> linkedHashMap = new LinkedHashMap<>();
    private CustomToasts toasts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(R.string.owner_information);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        next.setOnClickListener(this);
        owner = new Owner();
        firebase = new Firebase(this);
        toasts = new CustomToasts(this);
        setAddValueEventListener();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        if (validateOwnerInputs()) {
            searchExistingOwner();
        }
    }

    private void startAnotherActivity() {
        Intent intent = new Intent(this, RegisterAnimalActivity.class);
        intent.putExtra(Const.OWNER, new Gson().toJson(owner));
        intent.putExtra(Const.PATH, path);
        startActivity(intent);
    }

    private void setAddValueEventListener() {
        firebase.getDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        ownerList = new ArrayList<>();
        Owner owner;
        for (DataSnapshot children : dataSnapshot.getChildren()) {
            owner = children.getValue(Owner.class);
            linkedHashMap.put(children.getKey(), owner);
        }
    }

    private boolean validateOwnerInputs() {
        boolean isValid = true;
        if (ownerName.getText().toString().isEmpty()) {
            ownerName.setError("Empty field!");
            isValid = false;
        } else {
            owner.setName(ownerName.getText().toString());
        }
        if (ownerSurname.getText().toString().isEmpty()) {
            ownerSurname.setError("Empty field!");
            isValid = false;
        } else {
            owner.setSurname(ownerSurname.getText().toString());
        }
        if (ownerPhone.getText().toString().isEmpty()) {
            ownerPhone.setError("Empty field!");
            isValid = false;
        } else {
            owner.setPhone(ownerPhone.getText().toString());
        }
        if (ownerEmail.getText().toString().isEmpty()) {
            ownerEmail.setError("Empty field!");
            isValid = false;
        } else {
            owner.setEmail(ownerEmail.getText().toString());
        }
        if (ownerAddress.getText().toString().isEmpty()) {
            ownerAddress.setError("Empty field!");
            isValid = false;
        } else {
            owner.setAddress(ownerAddress.getText().toString());
        }
        return isValid;
    }

    private void searchExistingOwner() {
        for (Map.Entry<String, Owner> ownerEntry : linkedHashMap.entrySet()) {
            if (owner.getName().equals(ownerEntry.getValue().getName()) && owner.getSurname().equals(ownerEntry.getValue().getSurname())) {
                confirmDialog(getString(R.string.same_surname_name, owner.getName(), owner.getSurname()), ownerEntry.getValue(), ownerEntry.getKey());
                break;
            } else if (owner.getEmail().equals(ownerEntry.getValue().getEmail())) {
                confirmDialog(getString(R.string.same_email, owner.getEmail()), ownerEntry.getValue(), ownerEntry.getKey());
                break;
            } else if (owner.getPhone().equals(ownerEntry.getValue().getPhone())) {
                confirmDialog(getString(R.string.same_phone, owner.getPhone()), ownerEntry.getValue(), ownerEntry.getKey());
                break;
            }
        }
    }

    private void confirmDialog(String title, final Owner existingOwner, final String ownerPath) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        owner = existingOwner;
                        path = ownerPath;
                        startAnotherActivity();
                    }
                });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
