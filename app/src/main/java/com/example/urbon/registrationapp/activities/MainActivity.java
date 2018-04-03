package com.example.urbon.registrationapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CalendarView;
import android.widget.EditText;

import com.example.urbon.registrationapp.Firebase;
import com.example.urbon.registrationapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements CalendarView.OnDateChangeListener, View.OnClickListener {
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.navigationView)
    NavigationView navigationView;
    @BindView(R.id.calendarView)
    CalendarView calendarView;
    @BindView(R.id.floatingActionButtonAdd)
    FloatingActionButton floatingActionButtonAdd;

    ActionBarDrawerToggle drawerToggle;
    Intent intent;
    private Animation showRotateFab;
    private Animation hideRotateFab;
    private Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        drawerToggle = setUpDrawerToggle();
        drawerLayout.addDrawerListener(drawerToggle);
        setUpDrawerContent(navigationView);

        calendarView.setOnDateChangeListener(this);
        floatingActionButtonAdd.setOnClickListener(this);

        showRotateFab = AnimationUtils.loadAnimation(this, R.anim.show_add_fab_button);
        hideRotateFab = AnimationUtils.loadAnimation(this, R.anim.hide_add_fab_button);

        firebase = new Firebase(this);
    }

    private ActionBarDrawerToggle setUpDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.right_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.getItemId() == R.id.logOut) {
            firebase.signOut();
            intent = new Intent(this, SigninActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
        //return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void setUpDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    private void selectDrawerItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.registerPet:
                intent = new Intent(this, RegisterOwnerActivity.class);
                startActivity(intent);
                break;
            case R.id.customers:
                intent = new Intent(this, CustomersActivity.class);
                startActivity(intent);
                break;
//            case R.id.nav_third_fragment:
//                break;
        }
        drawerLayout.closeDrawers();
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
        month++;
        String dateString = year + "/" + month + "/" + dayOfMonth;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendarView.setDate(date.getTime());
    }

    @Override
    public void onClick(View view) {
        floatingActionButtonAdd.startAnimation(showRotateFab);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_calendar, null);
        dialogBuilder.setView(dialogView)
                .setTitle("Register client")
                .setCancelable(true)
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        floatingActionButtonAdd.startAnimation(hideRotateFab);
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        floatingActionButtonAdd.startAnimation(hideRotateFab);
                    }
                });

        EditText date = dialogView.findViewById(R.id.date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String selectedDate = sdf.format(new Date(calendarView.getDate()));
        date.setText(selectedDate);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
