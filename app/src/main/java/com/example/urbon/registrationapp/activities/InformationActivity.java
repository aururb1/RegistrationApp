package com.example.urbon.registrationapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.urbon.registrationapp.Firebase;
import com.example.urbon.registrationapp.utils.Const;
import com.example.urbon.registrationapp.R;
import com.example.urbon.registrationapp.models.Owner;
import com.example.urbon.registrationapp.models.Pet;
import com.example.urbon.registrationapp.utils.CustomToasts;
import com.google.gson.Gson;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by urbon on 2/19/2018.
 */

public class InformationActivity extends AppCompatActivity
        implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.petInformation)
    TextView petInformation;
    @BindView(R.id.petName)
    EditText petName;
    @BindView(R.id.petBirth)
    EditText petAge;
    @BindView(R.id.petBreed)
    EditText petBreed;
    @BindView(R.id.petType)
    EditText petType;
    @BindView(R.id.ownerInformation)
    TextView ownerInformation;
    @BindView(R.id.hour)
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
    @BindView(R.id.edit)
    Button edit;
    @BindView(R.id.addFab)
    FloatingActionButton addFab;
    @BindView(R.id.callFab)
    FloatingActionButton callFab;
    @BindView(R.id.smsFab)
    FloatingActionButton smsFab;
    @BindView(R.id.mailFab)
    FloatingActionButton mailFab;

    private Pet pet;
    private Owner owner;
    private String path;
    private Firebase firebase;
    private Animation showAddFabButton;
    private Animation hideAddFabButton;
    private Animation showFabButtons;
    private Animation hideFabButtons;
    private CustomToasts toasts;

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
        edit.setOnClickListener(this);
        addFab.setOnClickListener(this);
        smsFab.setOnClickListener(this);
        callFab.setOnClickListener(this);
        mailFab.setOnClickListener(this);

        showAddFabButton = AnimationUtils.loadAnimation(this, R.anim.show_add_fab_button);
        hideAddFabButton = AnimationUtils.loadAnimation(this, R.anim.hide_add_fab_button);
        showFabButtons = AnimationUtils.loadAnimation(this, R.anim.show_fab_buttons);
        hideFabButtons = AnimationUtils.loadAnimation(this, R.anim.hide_fab_buttons);

        firebase = new Firebase(this);
        toasts = new CustomToasts(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.information_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder
                    .setTitle(R.string.delete_item)
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            firebase.getDatabaseReferenceOwners().child(path).removeValue();
                            toasts.longToast(getString(R.string.successfully_deleted));
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
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
        path = intent.getStringExtra(Const.PATH);
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
        addFab.setVisibility(View.GONE);
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
    }

    private void changePetFieldsValues() {
        petName.setText(pet.getName());
        petType.setText(pet.getType());
        petBreed.setText(pet.getBreed());
        petAge.setText(String.valueOf(pet.getBirth()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit:
                editOwnerPetInformation();
                break;
            case R.id.addFab:
                hideShowOtherFabs();
                break;
            case R.id.callFab:
                phoneCall();
                break;
            case R.id.smsFab:
                smsMessage();
                break;
            case R.id.mailFab:
                sendEmail();
                break;
        }
    }

    private void editOwnerPetInformation() {
        if (owner != null) {
            owner.setName(ownerName.getText().toString());
            owner.setSurname(ownerSurname.getText().toString());
            owner.setEmail(ownerEmail.getText().toString());
            owner.setPhone(ownerPhone.getText().toString());
            owner.setAddress(ownerAddress.getText().toString());
            firebase.getDatabaseReferenceOwners().child(path).setValue(owner);

        } else {
            pet.setName(petName.getText().toString());
            pet.setBreed(petBreed.getText().toString());
            pet.setType(petType.getText().toString());
            pet.setBirth(new Date());
            firebase.getDatabaseReferenceOwners().child(path).setValue(pet);
        }
        toasts.shortToast("Successfully changed");
        onBackPressed();
    }

    private void hideShowOtherFabs() {
        if (smsFab.getVisibility() == View.VISIBLE) {
            smsFab.setVisibility(View.GONE);
            callFab.setVisibility(View.GONE);
            mailFab.setVisibility(View.GONE);
            smsFab.startAnimation(hideFabButtons);
            callFab.startAnimation(hideFabButtons);
            mailFab.startAnimation(hideFabButtons);
            addFab.startAnimation(hideAddFabButton);
        } else {
            smsFab.setVisibility(View.VISIBLE);
            callFab.setVisibility(View.VISIBLE);
            mailFab.setVisibility(View.VISIBLE);
            smsFab.startAnimation(showFabButtons);
            callFab.startAnimation(showFabButtons);
            mailFab.startAnimation(showFabButtons);
            addFab.startAnimation(showAddFabButton);
        }
    }

    private void phoneCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + owner.getPhone()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            new CustomToasts(this).longToast(getString(R.string.no_phone_call));
        }
    }

    private void smsMessage() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + owner.getPhone()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            new CustomToasts(this).longToast(getString(R.string.no_sms_sending));
        }
    }

    private void sendEmail() {
        String[] TO = {owner.getEmail()};
//        String[] CC = {"xyz@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            new CustomToasts(this).longToast(getString(R.string.no_email_sending));
        }
    }
}
