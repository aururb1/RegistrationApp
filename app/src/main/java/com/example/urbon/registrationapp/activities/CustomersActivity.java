package com.example.urbon.registrationapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.urbon.registrationapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by urbon on 2/16/2018.
 */

public class CustomersActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(R.string.customers);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
