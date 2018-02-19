package com.example.urbon.registrationapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.urbon.registrationapp.R;
import com.example.urbon.registrationapp.models.Owner;
import com.example.urbon.registrationapp.models.Pet;
import com.example.urbon.registrationapp.utils.CustomToasts;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    @BindView(R.id.ownerAdsress)
    EditText ownerAdsress;
    @BindView(R.id.save)
    Button save;


    DatabaseReference databaseReference;

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
        databaseReference = FirebaseDatabase.getInstance().getReference("z5bKBNMQj2POjbcEKjkeJ1AgOUJ3/owners");
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
        owner.setAddress(ownerAdsress.getText().toString());
        pets.add(pet);
        owner.setPets(pets);
        databaseReference.child(databaseReference.push().getKey()).setValue(owner);
        new CustomToasts(this).shortToast("customer added: " + owner.getName());
        startAnotherActivity();
    }

    private void startAnotherActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
