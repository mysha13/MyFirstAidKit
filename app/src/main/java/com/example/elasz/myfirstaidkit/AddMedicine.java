package com.example.elasz.myfirstaidkit;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddMedicine extends AppCompatActivity {

    private static final String TAG = "AddMedicine";

    @BindView(R.id.txt_expdate)
    TextView displayExpDate;

    @BindView(R.id.txt_opendate)
    TextView displayOpenDate;

    //private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListenerForOpen;

//butterknife nie działa!!!
    @BindView(R.id.btn_scanBarcode)
    Button btnScanBarcode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        ButterKnife.bind(this);
        displayExpDate = (TextView) findViewById(R.id.txt_expdate);
        displayExpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getExpDate();
            }
        });

        displayOpenDate=(TextView) findViewById(R.id.txt_opendate);
        displayOpenDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOpenDate();
            }
        });


        btnScanBarcode = (Button) findViewById(R.id.btn_scanBarcode);
        //mDisplayDate = (TextView) findViewById(R.id.txt_expdate);
        btnScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenBarcodeScannerActivity();

            }
        });

            /*@Override
            public void onClick(View view){
                GetCurrentDateData();
            }
*/
            /*mDisplayDate = (TextView) findViewById(R.id.txt_expdate);
            mDisplayDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog dialog = new DatePickerDialog(
                            AddMedicine.this,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            mDateSetListener,
                            year,month,day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();*/





        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
                displayExpDate.setText(date);

            }
        };

        mDateSetListenerForOpen = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
                displayOpenDate.setText(date);

            }
        };


    }
    @OnClick(R.id.txt_expdate)
    public void getOpenDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(
                AddMedicine.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListenerForOpen,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @OnClick(R.id.txt_expdate)
    public void getExpDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(
                AddMedicine.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }



    @OnClick(R.id.btn_scanBarcode)
    public void OpenBarcodeScannerActivity(){
        Intent intent= new Intent(this, BarcodeScanner.class);
        startActivity(intent);
    }

}
