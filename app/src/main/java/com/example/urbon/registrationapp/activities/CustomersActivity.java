package com.example.urbon.registrationapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.example.urbon.registrationapp.Const;
import com.example.urbon.registrationapp.Firebase;
import com.example.urbon.registrationapp.R;

import com.example.urbon.registrationapp.adapters.HeaderItem;
import com.example.urbon.registrationapp.adapters.SectionItem;
import com.example.urbon.registrationapp.models.Owner;
import com.example.urbon.registrationapp.models.Pet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

/**
 * Created by urbon on 2/16/2018.
 */

public class CustomersActivity extends AppCompatActivity implements FlexibleAdapter.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    Firebase firebase;
    List<Owner> owners = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private FlexibleAdapter<AbstractFlexibleItem> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);
        ButterKnife.bind(this);
        showProgressBar();
        setSupportActionBar(toolbar);
        setTitle(R.string.customers);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        firebase = new Firebase(this);
        setAddValueEventListener();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setAddValueEventListener() {
        firebase.getDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressBar();
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressBar();
            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter = new FlexibleAdapter<>(null, this, true);

        recyclerView.setAdapter(adapter);

        adapter
                .setDisplayHeadersAtStartUp(true) //Show Headers at startUp!
                .setStickyHeaders(true); //Make headers sticky

        List<AbstractFlexibleItem> newItems = new ArrayList<>();
        owners = new ArrayList<>();
        Owner owner;
        for (DataSnapshot children : dataSnapshot.getChildren()) {
            owner = children.getValue(Owner.class);
            HeaderItem header = new HeaderItem(owner);
            for (Pet pet : owner.getPets()) {
                SectionItem sectionItem = new SectionItem(header, pet, this);
                newItems.add(sectionItem);
            }

        }
        adapter.onLoadMoreComplete(newItems);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public boolean onItemClick(int position) {
        Intent intent = new Intent(this, InformationActivity.class);
        if (adapter.getItem(position) instanceof HeaderItem) {
            HeaderItem item = (HeaderItem) adapter.getItem(position);
            intent.putExtra(Const.OWNER, new Gson().toJson(item.getOwner()));
        } else if (adapter.getItem(position) instanceof SectionItem) {
            SectionItem item = (SectionItem) adapter.getItem(position);
            intent.putExtra(Const.PET, new Gson().toJson(item.getPet()));
        }
        this.startActivity(intent);
        return false;
    }
}
