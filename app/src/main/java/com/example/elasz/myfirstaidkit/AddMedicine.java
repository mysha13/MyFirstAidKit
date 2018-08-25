package com.example.elasz.myfirstaidkit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddMedicine extends AppCompatActivity {
//butterknife nie działa!!!
    @BindView(R.id.btn_scanBarcode)
    Button btnScanBarcode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        ButterKnife.bind(this);


        btnScanBarcode = (Button) findViewById(R.id.btn_scanBarcode);
        btnScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenBarcodeScannerActivity();
            }
        });
    }

    @OnClick(R.id.btn_scanBarcode)
    public void OpenBarcodeScannerActivity(){
        Intent intent= new Intent(this, BarcodeScanner.class);
        startActivity(intent);
    }

}
