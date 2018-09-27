package com.example.elasz.myfirstaidkit;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elasz.myfirstaidkit.Medicaments.ShortMedInfoItem;
import com.facebook.stetho.Stetho;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

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

    private DownloadManager downloadManager;
    private long refid;
    private Uri Download_Uri;


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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        simpleList = (ListView)findViewById(R.id.listviewinformation);
        Stetho.initializeWithDefaults(this);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_list_view__information_list, R.id.txtInfoView_infoName, countryList);
        simpleList.setAdapter(arrayAdapter);
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        cv_take=(CardView) findViewById(R.id.btn_takeMedicine);
        cv_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenTakeMedActivity();

                }
        });


        txtDownload=(TextView) findViewById(R.id.txt_download_file);
        cv_download=(CardView) findViewById(R.id.btn_download_file);
        cv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();

    }
});

        imagebtnSearch=(ImageButton) findViewById(R.id.imagebtn_search);
        imagebtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityFindMedicine();
            }
        });
        txt_search=(TextView) findViewById(R.id.txt_search);
        txt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityFindMedicine();
            }
        });
        cv_search=(CardView) findViewById(R.id.btn_search);
        cv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityFindMedicine();
            }
        });


        imagebtnAdd=(ImageButton) findViewById(R.id.imagebtn_add);
        imagebtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAddMedicine();
            }
        });
        txt_add=(TextView) findViewById(R.id.txt_add);
        txt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAddMedicine();
            }
        });
        cv_add=(CardView) findViewById(R.id.btn_addMedicine);
        cv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAddMedicine();
            }
        });

        imagebtnLists=(ImageButton) findViewById(R.id.imagebtn_lists);
        imagebtnLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityListOfMedicines();
            }
        });
        txt_lists=(TextView) findViewById(R.id.txt_lists);
        txt_lists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityListOfMedicines();
            }
        });
        cv_listOfMedicines=(CardView) findViewById(R.id.btn_listOfMedicines);
        cv_listOfMedicines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openActivityListOfMedicines();
            }
        });

        cv_addAlarm=(CardView) findViewById(R.id.btn_addAlarms);
        cv_addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenActivityAlarms();
            }
        });
    }

    private void OpenTakeMedActivity() {
        Intent intent=new Intent(this, TakeMedicine.class);
        startActivity(intent);
    }

    public void OpenActivityAlarms(){
    Intent intent=new Intent(this, Alarms.class);
    startActivity(intent);
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
        Intent intent=new Intent(this, OneMedicineInformation.class);//ListOfMedicines
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

    private void checkPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
                if (isConnectingToInternet())
                    new DownloadTasks(MainActivity.this, cv_download, txtDownload, HttpLink.MedicalsRegister);
                else
                    Toast.makeText(MainActivity.this, "Oops!! There is no internet connection. Please enable internet connection and try again.", Toast.LENGTH_SHORT).show();

            } else {
                Log.e(TAG,"Permission is revoked");

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0:
                boolean permissionsGranted = true;
                if (grantResults.length > 0 && permissions.length==grantResults.length) {
                    for (int i = 0; i < permissions.length; i++){
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            permissionsGranted=false;
                        }
                    }

                } else {
                    permissionsGranted=false;
                }
                if(permissionsGranted){
                    //createFile();
                    if (isConnectingToInternet())
                    new DownloadTasks(MainActivity.this, cv_download, txtDownload, HttpLink.MedicalsRegister);
                else
                    Toast.makeText(MainActivity.this, "Oops!! There is no internet connection. Please enable internet connection and try again.", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }


   /* public void DownloadFiles(){

        try {
            URL url = new URL("http://pub.rejestrymedyczne.csioz.gov.pl/pobieranie_WS/Pobieranie.ashx?filetype=XLS&regtype=RPL_FILES");
            URLConnection conexion = url.openConnection();
            conexion.connect();
            int lenghtOfFile = conexion.getContentLength();
            InputStream is = url.openStream();
            File testDirectory = new File(Environment.getExternalStorageDirectory() + "/Folder");
            if (!testDirectory.exists()) {
                testDirectory.mkdir();
            }
            FileOutputStream fos = new FileOutputStream(testDirectory);
            byte data[] = new byte[1024];
            int count = 0;
            long total = 0;
            int progress = 0;
            while ((count = is.read(data)) != -1) {
                total += count;
                int progress_temp = (int) total * 100 / lenghtOfFile;
                if (progress_temp % 10 == 0 && progress != progress_temp) {
                    progress = progress_temp;
                }
                fos.write(data, 0, count);
            }
            is.close();
            fos.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }*/


}

