package com.example.urbon.registrationapp.activities;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.ProgressBar;
import android.widget.TimePicker;

import com.example.urbon.registrationapp.AdapterCalendar;
import com.example.urbon.registrationapp.Firebase;
import com.example.urbon.registrationapp.R;
import com.example.urbon.registrationapp.models.Day;
import com.example.urbon.registrationapp.models.Hour;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    ActionBarDrawerToggle drawerToggle;
    Intent intent;
    private Animation showRotateFab;
    private Animation hideRotateFab;
    private Firebase firebase;
    private List<Day> dayList;
    private List<Hour> hourList;
    private AdapterCalendar adapter;
    private SimpleDateFormat sdf;
    EditText time;
    EditText date;
    EditText phone;
    EditText petName;
    EditText ownerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        showProgressBar();
        setSupportActionBar(toolbar);
        drawerToggle = setUpDrawerToggle();
        drawerLayout.addDrawerListener(drawerToggle);
        setUpDrawerContent(navigationView);

        calendarView.setOnDateChangeListener(this);
        floatingActionButtonAdd.setOnClickListener(this);

        showRotateFab = AnimationUtils.loadAnimation(this, R.anim.show_add_fab_button);
        hideRotateFab = AnimationUtils.loadAnimation(this, R.anim.hide_add_fab_button);

        dayList = new ArrayList<>();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        firebase = new Firebase(this);
        setAddValueEventListener();
    }

    private void initRecyclerAdapter() {
        String selectedDate = sdf.format(new Date(calendarView.getDate()));
        for (Day day : dayList) {
            if (selectedDate.equals(day.getDate())){
                hourList = day.getHours();
            }
        }
        adapter = new AdapterCalendar(hourList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private ActionBarDrawerToggle setUpDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setAddValueEventListener() {
        firebase.getDatabaseReferenceCalendar().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressBar();
                showData(dataSnapshot);
                initRecyclerAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressBar();
            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        Day day;
        Hour hour;
        dayList = new ArrayList<>();
        for (DataSnapshot children : dataSnapshot.getChildren()) {
            day = new Day();
            day.setDate(children.getKey());
            for (DataSnapshot hourChildren : children.getChildren()) {
                hour = hourChildren.getValue(Hour.class);
                day.getHours().add(hour);
            }
            dayList.add(day);
        }
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
        }
        drawerLayout.closeDrawers();
    }

    @Override
    public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayOfMonth) {
        month++;
        String dateString = year + "-" + month + "-" + dayOfMonth;

        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendarView.setDate(date.getTime());
        String selectedDate = sdf.format(new Date(calendarView.getDate()));
        hourList = new ArrayList<>();
        for (Day day : dayList) {
            if (selectedDate.equals(day.getDate())){
                hourList = day.getHours();
            }
        }
        adapter.updateList(hourList);
    }


    @Override
    public void onClick(View view) {
        floatingActionButtonAdd.startAnimation(showRotateFab);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_calendar, null);
        time = dialogView.findViewById(R.id.hour);
        date = dialogView.findViewById(R.id.date);
        phone = dialogView.findViewById(R.id.phone);
        petName = dialogView.findViewById(R.id.petName);
        ownerName = dialogView.findViewById(R.id.ownerName);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Locale locale = new Locale("en");
                Locale.setDefault(locale);
                Configuration config = getBaseContext().getResources().getConfiguration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());

                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);

                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                timePicker.setTitle("Select Time");
                timePicker.show();
            }
        });
        dialogBuilder.setView(dialogView)
                .setTitle("Register client")
                .setCancelable(true)
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        floatingActionButtonAdd.startAnimation(hideRotateFab);
                        saveCalendar();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        floatingActionButtonAdd.startAnimation(hideRotateFab);
                    }
                });

        String selectedDate = sdf.format(new Date(calendarView.getDate()));
        date.setText(selectedDate);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void saveCalendar() {
        Day day = new Day();
        day.setDate(date.getText().toString());

        Hour hour = new Hour();
        hour.setTime(time.getText().toString());
        hour.setOwnerName(ownerName.getText().toString());
        hour.setPetName(petName.getText().toString());
        hour.setPhone(phone.getText().toString());
        day.addHour(hour);
        firebase.getDatabaseReferenceCalendar().child(day.getDate()).child(hour.getTime()).setValue(hour);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}
