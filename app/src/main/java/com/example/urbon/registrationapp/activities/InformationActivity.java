package com.example.urbon.registrationapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.urbon.registrationapp.Const;
import com.example.urbon.registrationapp.R;
import com.example.urbon.registrationapp.models.Owner;
import com.example.urbon.registrationapp.models.Pet;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by urbon on 2/19/2018.
 */

public class InformationActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.petInformation)
    TextView petInformation;
    @BindView(R.id.petName)
    EditText petName;
    @BindView(R.id.petAge)
    EditText petAge;
    @BindView(R.id.petBreed)
    EditText petBreed;
    @BindView(R.id.petType)
    EditText petType;
    @BindView(R.id.ownerInformation)
    TextView ownerInformation;
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
    @BindView(R.id.namePetText)
    TextView petNameText;
    @BindView(R.id.petTypeText)
    TextView petTypeText;
    @BindView(R.id.petBreedText)
    TextView petBreedText;
    @BindView(R.id.petAgeText)
    TextView petAgeText;
    @BindView(R.id.ownerNameText)
    TextView ownerNameText;
    @BindView(R.id.ownerSurnameText)
    TextView ownerSurnameText;
    @BindView(R.id.ownerPhoneText)
    TextView ownerPhoneText;
    @BindView(R.id.ownerEmailText)
    TextView ownerEmailText;
    @BindView(R.id.ownerAddressText)
    TextView ownerAddressText;
    @BindView(R.id.save)
    Button save;

    private Pet pet;
    private Owner owner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.information));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        getDataFromIntent(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    private void getDataFromIntent(Intent intent) {
        if (intent.getStringExtra(Const.OWNER) != null) {
            owner = new Gson().fromJson(intent.getStringExtra(Const.OWNER), Owner.class);
            changePetVisibility();
            changeOwnerFieldsValues();
        } else if (intent.getStringExtra(Const.PET) != null) {
            pet = new Gson().fromJson(intent.getStringExtra(Const.PET), Pet.class);
            changeOwnerVisibility();
            changePetFieldsValues();
        }
    }

    private void changePetVisibility() {
        petInformation.setVisibility(View.GONE);
        petName.setVisibility(View.GONE);
        petType.setVisibility(View.GONE);
        petBreed.setVisibility(View.GONE);
        petAge.setVisibility(View.GONE);

        petNameText.setVisibility(View.GONE);
        petTypeText.setVisibility(View.GONE);
        petBreedText.setVisibility(View.GONE);
        petAgeText.setVisibility(View.GONE);
    }

    private void changeOwnerVisibility() {
        ownerInformation.setVisibility(View.GONE);
        ownerName.setVisibility(View.GONE);
        ownerSurname.setVisibility(View.GONE);
        ownerPhone.setVisibility(View.GONE);
        ownerEmail.setVisibility(View.GONE);
        ownerAddress.setVisibility(View.GONE);

        ownerNameText.setVisibility(View.GONE);
        ownerSurnameText.setVisibility(View.GONE);
        ownerPhoneText.setVisibility(View.GONE);
        ownerEmailText.setVisibility(View.GONE);
        ownerAddressText.setVisibility(View.GONE);
    }

    private void changeOwnerFieldsValues() {
        ownerName.setText(owner.getName());
        ownerSurname.setText(owner.getSurname());
        ownerPhone.setText(owner.getPhone());
        ownerEmail.setText(owner.getEmail());
        ownerAddress.setText(owner.getAddress());

        ownerName.setEnabled(false);
        ownerSurname.setEnabled(false);
        ownerPhone.setEnabled(false);
        ownerEmail.setEnabled(false);
        ownerAddress.setEnabled(false);
    }

    private void changePetFieldsValues() {
        petName.setText(pet.getName());
        petType.setText(pet.getType());
        petBreed.setText(pet.getBreed());
        petAge.setText(String.valueOf(pet.getAge()));

        petName.setEnabled(false);
        petType.setEnabled(false);
        petBreed.setEnabled(false);
        petAge.setEnabled(false);
    }
}
