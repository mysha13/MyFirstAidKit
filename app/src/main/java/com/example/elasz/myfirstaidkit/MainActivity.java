package com.example.elasz.myfirstaidkit;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBAmountFormAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBFormAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBMedicamentInfoAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBPurposeAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBUserMedicamentsAdapter;
import com.example.elasz.myfirstaidkit.Medicaments.ShortMedInfoItem;
import com.facebook.stetho.Stetho;
import com.karan.churi.PermissionManager.PermissionManager;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolBar;
    private DrawerLayout drawerLayout;


    private DBMedicamentInfoAdapter dbMedInfo;
    private DBUserMedicamentsAdapter dbUserMed;

    private CardView cv_search;
    private CardView cv_add;
    private CardView cv_take;
    private CardView cv_listOfMedicines;
    private CardView cv_addAlarm;

    private ImageButton imagebtnAdd;
    private ImageButton imagebtnSearch;
    private ImageButton imagebtnLists;
    private TextView txt_add;
    private TextView txt_search;
    private TextView txt_lists;

    private ImageButton imagebtnDownload;
    private TextView txtDownload;
    private CardView cv_download;
    private static final String TAG = "Download MainActivity";

    private ArrayList<ShortMedInfoItem> medicines = new ArrayList<>();

    @BindView(R.id.btn_search)
    CardView btnForSearchActivity;

    @BindView(R.id.btn_addMedicine)
    CardView btnForAddMedicineActivity;

    @BindView(R.id.btn_listOfMedicines)
    CardView btnForListOfMedicinesActivity;

    @BindView(R.id.tv_nbAllMed)
    TextView nballmed;

    @BindView(R.id.tv_nbTimelyMed)
    TextView nbtimelymed;

    @BindView(R.id.tv_nbOverdueMed)
    TextView nboverduemed;


    PermissionManager permissionManager;
    private DownloadManager downloadManager;
    private long refid;
    private Uri Download_Uri;
    private DBFormAdapter dbForm;

    SQLiteDatabase db;

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

   /* @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_download) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_edit_forms) {
            openNextActivity();

        } else if (id == R.id.nav_edit_amountforms) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/

    private void openNextActivity() {
        Intent intent = new Intent(MainActivity.this, ListView_InformationList.class);
        startActivity(intent);
    }

    // Array of strings...
    ListView simpleList;
    String countryList[] = {"Leki przeterminowane", "Leki terminowe", "Wszystkie leki"};

  /*  int nb= medicines.size();
    ListView secondList;
    String numberList[] = { " " + 0," "+ nb," " + nb };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        drawerLayout = findViewById(R.id.drawer_layout);

        toolbarInitialize();

        //simpleList = (ListView) findViewById(R.id.listviewinformation);
        Stetho.initializeWithDefaults(this);
        permissionManager = new PermissionManager() {
        };
        permissionManager.checkAndRequestPermissions(this);

        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_list_view__information_list, R.id.txtInfoView_infoName, countryList);
        //secondList= (ListView) findViewById(R.id.listviewinformation);
        //ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, R.layout.activity_list_view__information_list, R.id.txtInfoView_number, (List<String>) secondList);

        //secondList.setAdapter(arrayAdapter1);
        //simpleList.setAdapter(arrayAdapter);
        //GetMedicamentCount();

        setDBAdapters();
        getAllNumbersToSet();
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DBFormAdapter dbForm = new DBFormAdapter(this);
        DBPurposeAdapter dbPurpose = new DBPurposeAdapter(this);
        DBAmountFormAdapter dbAmountForm = new DBAmountFormAdapter(this);
        //CreateSpinnerLists(dbForm, dbPurpose, dbAmountForm);
        cv_take = (CardView) findViewById(R.id.btn_takeMedicine);
        cv_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenTakeMedActivity();

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawer.closeDrawers();

                        int id = menuItem.getItemId();

                        if (id == R.id.nav_download) {
                            // Handle the camera action
                        } /*else if (id == R.id.nav_gallery) {

                        } else if (id == R.id.nav_slideshow) {

                        } else if (id == R.id.nav_manage) {

                        }*/ else if (id == R.id.nav_edit_forms) {
                            //openNextActivity();
                            Intent intent = new Intent(MainActivity.this, EditFormsList.class);
                            startActivity(intent);

                        } else if (id == R.id.nav_edit_amountforms) {
                            Intent intent = new Intent(MainActivity.this, EditAmountFormsList.class);
                            startActivity(intent);
                        } else if (id == R.id.nav_edit_purpose) {
                            Intent intent = new Intent(MainActivity.this, EditPurposeList.class);
                            startActivity(intent);
                        } else if (id == R.id.nav_edit_person) {
                            Intent intent = new Intent(MainActivity.this, EditPersonList.class);
                            startActivity(intent);

                        }
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });


        txtDownload = (TextView) findViewById(R.id.txt_download_file);
        cv_download = (CardView) findViewById(R.id.btn_download_file);
        cv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNextActivity();
                //checkPermission();

            }
        });

        imagebtnSearch = (ImageButton) findViewById(R.id.imagebtn_search);
        imagebtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityFindMedicine();
            }
        });
        txt_search = (TextView) findViewById(R.id.txt_search);
        txt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityFindMedicine();
            }
        });
        cv_search = (CardView) findViewById(R.id.btn_search);
        cv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityFindMedicine();
            }
        });


        imagebtnAdd = (ImageButton) findViewById(R.id.imagebtn_add);
        imagebtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAddMedicine();
            }
        });
        txt_add = (TextView) findViewById(R.id.txt_add);
        txt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAddMedicine();
            }
        });
        cv_add = (CardView) findViewById(R.id.btn_addMedicine);
        cv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAddMedicine();
            }
        });

        imagebtnLists = (ImageButton) findViewById(R.id.imagebtn_lists);
        imagebtnLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityListOfMedicines();
            }
        });
        txt_lists = (TextView) findViewById(R.id.txt_lists);
        txt_lists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityListOfMedicines();
            }
        });
        cv_listOfMedicines = (CardView) findViewById(R.id.btn_listOfMedicines);
        cv_listOfMedicines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityListOfMedicines();
            }
        });

        cv_addAlarm = (CardView) findViewById(R.id.btn_addAlarms);
        cv_addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenActivityAlarms();
            }
        });


    }

    private void toolbarInitialize() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);
    }








    private void setDBAdapters() {
        dbUserMed = new DBUserMedicamentsAdapter(this);
        dbMedInfo = new DBMedicamentInfoAdapter(this);
    }

    private void getAllNumbersToSet() {
        dbUserMed.OpenDB();
        long nball = dbUserMed.medCount();
        dbUserMed.CloseDB();
        String nbText = Long.toString(nball);
        nballmed.setText(nbText);


        Calendar cal= Calendar.getInstance();
        Date currentTime = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //"dd/MM/yyyy_HHmmss");
        String currentDateandTime = sdf.format(currentTime);
        cal.add(Calendar.DAY_OF_MONTH, 7);
        String newdate = sdf.format(cal.getTime());

        dbUserMed.OpenDB();
        ArrayList<String> a =dbUserMed.medNear(newdate);
        dbUserMed.CloseDB();
        nbtimelymed.setText(String.valueOf(a.size()));




    }

    private void CreateSpinnerLists(DBFormAdapter dbF, DBPurposeAdapter dbP, DBAmountFormAdapter dbAF) {
        dbF.OpenDB();
        ContentValues cv = new ContentValues();
        String[] forms = {"czopki","inne","kapsułki","maść","pastylki","saszetki","syrop","tabletki", "tabletki musujące", "zawiesina"};
        if(dbF.GetAllForms().getCount() == 0){
            for(int i=0; i<forms.length;i++) {
                // cv.put(DatabaseConstantInformation.FORM_NAME, forms[i]);
                dbF.AddForm(forms[i].toString());
                // db.insert(DatabaseConstantInformation.FORMTABLE, null, cv);
                cv.clear();
            }
            dbF.CloseDB();
        }
        dbP.OpenDB();
        ContentValues cv1 = new ContentValues();
        String[] purposes = {"alergia","katar","kaszel/gardło", "gorączka","przeciwbólwo", "witaminy", "od specjalisty", "inne"};
        if(dbP.GetAllPurposes().getCount() == 0){
            for(int i=0; i<purposes.length;i++) {
                // cv.put(DatabaseConstantInformation.FORM_NAME, forms[i]);
                dbP.AddPurpose(purposes[i].toString());
                // db.insert(DatabaseConstantInformation.FORMTABLE, null, cv);
                cv1.clear();
            }
            dbP.CloseDB();
        }
        dbAF.OpenDB();
        ContentValues cv2 = new ContentValues();
        String[] amuntForms = {"ml" ,"g", "tabletek","opakowań","sztuk"};
        if(dbAF.GetAllAmountForms().getCount() == 0){
            for(int i=0; i<amuntForms.length;i++) {
                // cv.put(DatabaseConstantInformation.FORM_NAME, forms[i]);
                dbAF.AddAmountForm(amuntForms[i].toString());
                // db.insert(DatabaseConstantInformation.FORMTABLE, null, cv);
                cv2.clear();
            }
            dbAF.CloseDB();
        }

    }

    private void OpenTakeMedActivity() {
        Intent intent = new Intent(this, TakeMedicine.class);
        startActivity(intent);
    }

    public void OpenActivityAlarms() {
        Intent intent = new Intent(this, Alarms.class);
        startActivity(intent);
    }

    public void openActivityFindMedicine() {
        Intent intent = new Intent(this, FindMedicine.class);
        startActivity(intent);
    }

    public void openActivityAddMedicine() {
        Intent intent = new Intent(this, AddMedicine.class);
        startActivity(intent);
    }

    public void openActivityListOfMedicines() {
        Intent intent = new Intent(this, OneMedicineInformation.class);//ListOfMedicines
        Bundle bundle = new Bundle();
        bundle.putSerializable("ListOfMedicines", (Serializable) medicines);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //Check if internet is present or not
    private boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                if (isConnectingToInternet())
                    new DownloadTasks(MainActivity.this, cv_download, txtDownload, HttpLink.MedicalsRegister);
                else
                    Toast.makeText(MainActivity.this, "Oops!! There is no internet connection. Please enable internet connection and try again.", Toast.LENGTH_SHORT).show();

            } else {
                Log.e(TAG, "Permission is revoked");

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        permissionManager.checkResult(requestCode, permissions, grantResults);

        ArrayList<String> granted = permissionManager.getStatus().get(0).granted;
        ArrayList<String> denied = permissionManager.getStatus().get(0).denied;

        if(denied.size() > 0){
            Toast.makeText(MainActivity.this, "Aplikacja nie będzie działać bez pozwoleń, przyznaj pozwolenia", Toast.LENGTH_SHORT).show();
        }
        switch (requestCode) {
            case 0:
                boolean permissionsGranted = true;
                if (grantResults.length > 0 && permissions.length == grantResults.length) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            permissionsGranted = false;
                        }
                    }

                } else {
                    permissionsGranted = false;
                }
                if (permissionsGranted) {
                    //createFile();
                    if (isConnectingToInternet())
                        new DownloadTasks(MainActivity.this, cv_download, txtDownload, HttpLink.MedicalsRegister);
                    else
                        Toast.makeText(MainActivity.this, "Oops!! There is no internet connection. Please enable internet connection and try again.", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }



}

