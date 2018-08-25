package com.example.elasz.myfirstaidkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

private CardView cv_search;
private CardView cv_add;
private CardView cv_take;
private CardView cv_listOfMedicines;
private CardView cv_addAlarm;

    @BindView(R.id.btn_search)
    CardView btnForSearchActivity;

    @BindView(R.id.btn_addMedicine)
    CardView btnForAddMedicineActivity;

    @BindView(R.id.btn_listOfMedicines)
    CardView btnForListOfMedicinesActivity;
    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Array of strings...
    ListView simpleList;
    String countryList[] = {"Leki przeterminowane", "Leki terminowe", "Wszystkie leki"};

    @Override   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);      setContentView(R.layout.activity_main);
        simpleList = (ListView)findViewById(R.id.listviewinformation);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_list_view__information_list, R.id.txtInfoView_infoName, countryList);
        simpleList.setAdapter(arrayAdapter);




        cv_search=(CardView) findViewById(R.id.btn_search);
        cv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityFindMedicine();
            }
        });

        cv_add=(CardView) findViewById(R.id.btn_addMedicine);
        cv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAddMedicine();
            }
        });

        cv_listOfMedicines=(CardView) findViewById(R.id.btn_listOfMedicines);
        cv_listOfMedicines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openActivityListOfMedicines();
            }
        });

    }

    public void openActivityFindMedicine(){
        Intent intent=new Intent(this, FindMedicine.class);
        startActivity(intent);
    }

    public void openActivityAddMedicine(){
        Intent intent=new Intent(this, AddMedicine.class);
        startActivity(intent);
    }
    public void openActivityListOfMedicines(){
        Intent intent=new Intent(this, ListOfMedicines.class);
        startActivity(intent);
    }


    /*@OnClick(R.id.btn_search) void openActivityFindMedicine() {
    Intent intent = new Intent(this, FindMedicine.class);
    startActivity(intent);
}
    @OnClick(R.id.btn_search) void openActivityListOfMedicines(){
        Intent intent=new Intent(this, ListOfMedicines.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_search) void openActivityAddMedicine(){
        Intent intent=new Intent(this, AddMedicine.class);
        startActivity(intent);
    }

    public void OpenNextActvities(int id)
    {
        if(id==R.id.btn_search) {
            Intent intent=new Intent(this, FindMedicine.class);
            startActivity(intent);
        }
        else if(id==R.id.btn_addMedicine){
            Intent intent=new Intent(this, AddMedicine.class);
            startActivity(intent);
        }
        else if(id==R.id.btn_listOfMedicines){
            Intent intent=new Intent(this, ListOfMedicines.class);
            startActivity(intent);
        }


    }*/

}


