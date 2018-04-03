package com.example.urbon.registrationapp.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.urbon.registrationapp.Firebase;
import com.example.urbon.registrationapp.R;
import com.example.urbon.registrationapp.models.Owner;
import com.example.urbon.registrationapp.models.Pet;
import com.example.urbon.registrationapp.utils.Const;
import com.example.urbon.registrationapp.utils.CustomToasts;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by urbon on 3/8/2018.
 */

public class RegisterAnimalActivity extends AppCompatActivity
        implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.petName)
    EditText petName;
    @BindView(R.id.petBirth)
    EditText petBirth;
    @BindView(R.id.petBreed)
    EditText petBreed;
    @BindView(R.id.petType)
    EditText petType;
    @BindView(R.id.save)
    Button save;


    private Firebase firebase;
    private Pet pet;
    private Owner owner;
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener date;
    private String path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(R.string.pet_information);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        save.setOnClickListener(this);
        petBirth.setOnClickListener(this);
        petBirth.setOnClickListener(this);
        firebase = new Firebase(this);
        Intent intent = getIntent();
        path = intent.getStringExtra(Const.PATH);
        owner = new Gson().fromJson(intent.getStringExtra(Const.OWNER), Owner.class);
        pet = new Pet();
        calendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:
                if (validatePetInputs()) {
                    addNewPet();
                    startAnotherActivity();
                }
                break;
            case R.id.petBirth:
                datePicker();
                break;
        }
    }

    private void addNewPet() {
        if (path != null) {
            owner.getPets().add(pet);
            firebase.getDatabaseReference().child(path).setValue(owner);
        } else {
            List<Pet> pets = new ArrayList<>();
            pets.add(pet);
            owner.setPets(pets);
            firebase.getDatabaseReference().child(firebase.getDatabaseReference().push().getKey()).setValue(owner);
        }

    }

    private void startAnotherActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validatePetInputs() {
        boolean isValid = true;
        if (petName.getText().toString().isEmpty()) {
            petName.setError("Empty field!");
            isValid = false;
        } else {
            pet.setName(petName.getText().toString());
        }
        if (petType.getText().toString().isEmpty()) {
            petType.setError("Empty field!");
            isValid = false;
        } else {
            pet.setType(petType.getText().toString());
        }
        if (petBreed.getText().toString().isEmpty()) {
            petBreed.setError("Empty field!");
            isValid = false;
        } else {
            pet.setBreed(petBreed.getText().toString());
        }
        if (petBirth.getText().toString().isEmpty()) {
            petBirth.setError("Empty field!");
            isValid = false;
        } else {
            pet.setBirth(new Date());
        }
        return isValid;
    }

    private void updateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd", Locale.US);
        petBirth.setText(sdf.format(calendar.getTime()));
        petBirth.setError(null);
    }

    private void datePicker() {
        new DatePickerDialog(this,
                date,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
