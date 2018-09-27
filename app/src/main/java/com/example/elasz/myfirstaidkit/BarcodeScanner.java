package com.example.elasz.myfirstaidkit;

import android.Manifest;
import android.drm.DrmStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.net.URL;

import static android.Manifest.permission.CAMERA;

public class BarcodeScanner extends AppCompatActivity  implements ZXingScannerView.ResultHandler{


    private static final int request_camera=1;
    private ZXingScannerView scannerView;

    public static String getCode() {
        return code;
    }

    public static void setCode(String code) {
        BarcodeScanner.code = code;
    }

    public static String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView=new ZXingScannerView(this);
        setContentView(scannerView);
        // setContentView(R.layout.activity_barcode_scanner);

        if(Build.VERSION.SDK_INT >=  Build.VERSION_CODES.M)
        {
            if(checkPermission())
            {
                //this is a message
                Toast.makeText(BarcodeScanner.this, "Permission already granted!", Toast.LENGTH_LONG).show();
            }
            else
            {
                requestPermission();
            }
        }
    }

    private boolean checkPermission(){
        return ContextCompat.checkSelfPermission(BarcodeScanner.this, Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);

    }
    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{CAMERA}, request_camera);
    }

    public void onRequestPermissionResult(final int requestCode, String permission[], int grantResults[]){
        switch (requestCode){
            case request_camera :
                if (grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] ==PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        //this is a message
                        Toast.makeText(BarcodeScanner.this, "Permission granted", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(BarcodeScanner.this, "Permission denied", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                            if (shouldShowRequestPermissionRationale(CAMERA)){
                                displayAlertMessage("Zezwól na dostęp",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA}, request_camera);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                if(scannerView == null){
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }
            else{
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(BarcodeScanner.this)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Anuluj", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(final Result result) {
        final String scannerResult = result.getText();
        code=result.getText().toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Wynik skanowania");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //scannerView.resumeCameraPreview(BarcodeScanner.this);
                finish();
                Intent in= new Intent(BarcodeScanner.this, AddMedicine.class);
                startActivity(in);
                AddMedicine addmed=new AddMedicine();
                addmed.setCodecode(result.getText());

            }
        });
        /*builder.setNegativeButton("Idź do leku", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(scannerResult));
                startActivity(intent);
            }
        });*/
        builder.setMessage(scannerResult);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


}
