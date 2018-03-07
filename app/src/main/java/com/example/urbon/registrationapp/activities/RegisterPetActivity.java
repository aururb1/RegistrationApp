package com.example.urbon.registrationapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.urbon.registrationapp.Firebase;
import com.example.urbon.registrationapp.R;
import com.example.urbon.registrationapp.models.Owner;
import com.example.urbon.registrationapp.models.Pet;
import com.example.urbon.registrationapp.utils.CustomToasts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by urbon on 2/7/2018.
 */

public class RegisterPetActivity extends AppCompatActivity
        implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.petName)
    EditText petName;
    @BindView(R.id.petAge)
    EditText petAge;
    @BindView(R.id.petBreed)
    EditText petBreed;
    @BindView(R.id.petType)
    EditText petType;
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
    Button save;


    private Firebase firebase;
    private List<Owner> ownerList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pet);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(R.string.register_pet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        save.setOnClickListener(this);
        firebase = new Firebase(this);
        setAddValueEventListener();
        ownerEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() != 0){
                    for(Owner owner: ownerList){
                        if(owner.getEmail().equals(charSequence.toString())){
                            new CustomToasts(RegisterPetActivity.this).shortToast("customer founded: " + charSequence);
                        }
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        Pet pet = new Pet();
        pet.setName(petName.getText().toString());
        pet.setBreed(petBreed.getText().toString());
        pet.setType(petType.getText().toString());
        pet.setAge(Integer.parseInt(petAge.getText().toString()));

        List<Pet> pets = new ArrayList<>();
        Owner owner = new Owner();
        owner.setName(ownerName.getText().toString());
        owner.setSurname(ownerSurname.getText().toString());
        owner.setEmail(ownerEmail.getText().toString());
        owner.setPhone(ownerPhone.getText().toString());
        owner.setAddress(ownerAddress.getText().toString());
        pets.add(pet);
        owner.setPets(pets);
        firebase.getDatabaseReference().child(firebase.getDatabaseReference().push().getKey()).setValue(owner);
        new CustomToasts(this).shortToast("customer added: " + owner.getName());
        startAnotherActivity();
    }

    private void startAnotherActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
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
            ownerList.add(owner);
        }
    }
}
