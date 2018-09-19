package com.example.elasz.myfirstaidkit;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBMedicamentInfoAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBUserMedicamentsAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddMedicine extends AppCompatActivity {

    private static final String TAG = "AddMedicine";
    private DBMedicamentInfoAdapter dbMedInfo;
    private DBUserMedicamentsAdapter dbUserMed;
   // private DatabaseFormAdapter dAForm;
    @BindView(R.id.et_name_add)
    EditText name;

    @BindView(R.id.tv_expdate_add)
    TextView expdate;

    @BindView(R.id.tv_opendate_add)
    TextView opendate;

    @BindView(R.id.spin_form_add)
    Spinner form;

    @BindView(R.id.spin_purpose_add)
    Spinner purpose;

    @BindView(R.id.et_amount_add)
    EditText amount;

    @BindView(R.id.spin_amountform_add)
    Spinner amountForm;

    @BindView(R.id.spin_person_add)
    Spinner person;

    @BindView(R.id.et_power_add)
    EditText power;

    @BindView(R.id.et_subsActive_add)
    EditText subsActive;

    @BindView(R.id.et_code_add)
    EditText code;

    @BindView(R.id.et_producer_add)
    EditText producer;

    @BindView(R.id.et_note_add)
    EditText note;

    @BindView(R.id.btn_addPhoto)
    FloatingActionButton addPhoto;

    @BindView(R.id.btn_removePhoto)
    FloatingActionButton removePhoto;

    /*@BindView(R.id.imageView2)
    ImageView imageView;*/



    //private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListenerForOpen;

    //to make camera works
   /* private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;*/


   /* public static final int MY_REQUEST_CAMERA   = 10;
    public static final int MY_REQUEST_WRITE_CAMERA   = 11;
    public static final int CAPTURE_CAMERA   = 12;

    public static final int MY_REQUEST_READ_GALLERY   = 13;
    public static final int MY_REQUEST_WRITE_GALLERY   = 14;
    public static final int MY_REQUEST_GALLERY   = 15;
    public File file = null;
    private ImageView imageView;*/

//butterknife nie działa!!!
    @BindView(R.id.btn_scanBarcode_add)
    Button btnScanBarcode;


   /* private Button btn;
    private ImageView imageview;*/
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;
    private File filename = null;


    boolean isImageFitToScreen;
    private String fullScreenInd;
    private ImageView imageView;

    private Button saveMedicine;


    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        ButterKnife.bind(this);

        name=(EditText) findViewById(R.id.et_name_add);
        amount=(EditText) findViewById(R.id.et_amount_add);

        saveMedicine=(Button)findViewById(R.id.btn_saveAddedMedicine_add);
        saveMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMedicineClick();
            }
        });


        imageView=(ImageView) findViewById(R.id.imageView_add);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddMedicine.this, FullScreenPhoto.class);

                imageView.buildDrawingCache();
                Bitmap image= imageView.getDrawingCache();

                Bundle extras = new Bundle();
                extras.putParcelable("imagebitmap", image);
                intent.putExtras(extras);
                startActivity(intent);
               /* Intent intent = new Intent(AddMedicine.this,
                        AddMedicine.class);

                if("y".equals(fullScreenInd)){
                    intent.putExtra("fullScreenIndicator", "");
                }else{
                    intent.putExtra("fullScreenIndicator", "y");
                }
                AddMedicine.this.startActivity(intent);*/
            }
        });

        removePhoto=(FloatingActionButton) findViewById(R.id.btn_removePhoto);
        removePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearImageView();
            }
        });

        addPhoto=(FloatingActionButton) this.findViewById(R.id.btn_addPhoto);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
               // Toast.makeText(AddMedicine.this, "Kliknięto", Toast.LENGTH_SHORT).show();
            }
        });

        expdate = (TextView) findViewById(R.id.tv_expdate_add);
        expdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getExpDate();
            }
        });

        opendate=(TextView) findViewById(R.id.tv_opendate_add);
        opendate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOpenDate();
            }
        });


        btnScanBarcode = (Button) findViewById(R.id.btn_scanBarcode_add);
        //mDisplayDate = (TextView) findViewById(R.id.txt_expdate);
        btnScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenBarcodeScannerActivity();

            }
        });


        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
                expdate.setText(date);

            }
        };

        mDateSetListenerForOpen = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
                opendate.setText(date);

            }
        };

    }

    @OnClick(R.id.btn_saveAddedMedicine_add)
    void AddMedicineClick(){
        if(name.getText().toString().matches("")){
            Toast.makeText(AddMedicine.this, "Nazwa jest pusta", Toast.LENGTH_LONG).show();
        }else if(amount.getText().toString().matches("")){
            Toast.makeText(AddMedicine.this, "Ilość jest puste", Toast.LENGTH_LONG).show();
        }else{
            SetDatabaseAdapters();
            CheckResult();
        }
    }


    private void CheckResult() {
        long work = TryToAdd();
        if (work > 0) {
            Toast.makeText(AddMedicine.this, "Sukces", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(AddMedicine.this,"Porażka", Toast.LENGTH_LONG).show();
        }
    }
    private long TryToAdd() {
        dbUserMed.OpenDB();
        long work = addMedToDB();
        dbUserMed.CloseDB();
        return work;
    }

    private long addMedToDB() {
        return dbUserMed.AddUserMedicamentData(name.getText().toString(),
                0,
                null,//expdate.getText().toString(),
                null,//opendate.getText().toString(),
                null,
                null,
                Double.parseDouble(amount.getText().toString().replaceAll(",",".")),
                null,
                null,
                null);//note.getText().toString());
    }

    private void SetDatabaseAdapters() {
        dbMedInfo = new DBMedicamentInfoAdapter(this);
        dbUserMed = new DBUserMedicamentsAdapter(this);
        //dAForm = new DatabaseFormAdapter(this);
    }

    //for a better photos quality
   /* private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  *//* prefix *//*
                ".jpg",         *//* suffix *//*
                storageDir      *//* directory *//*
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }*/

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Wybierz czynność");
        String[] pictureDialogItems = {
                "Wybierz zdjęcia z galerii",
                "Zrób zdjęcie aparatem",
                "Anuluj"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery();
                                //Toast.makeText(AddMedicine.this, "Kliknięto o galeria", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                //Toast.makeText(AddMedicine.this, "Kliknięto 1 camera", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                dialog.dismiss();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA);

            // Create the File where the photo should go
           /* File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, CAMERA);
            }*/
        }
        //startActivityForResult(intent, CAMERA);
    }

    private  void ClearImageView(){
       // imageView.setImageBitmap(null);
        //imageView.setImageDrawable(null);
        imageView.setImageResource(0);
        imageView.setImageURI(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    //String path = saveImage(bitmap);
                    Toast.makeText(AddMedicine.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    imageView.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(AddMedicine.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(AddMedicine.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    /*private void setUI(){
        addPhoto=(FloatingActionButton) findViewById(R.id.btn_addPhoto);
        imageView=(ImageView) findViewById(R.id.imageView2);
        addPhoto.setOnClickListener(this);
    }*/

    /*public void takePhoto(View view){
        Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file=new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), //where is picture saved
                "test.jpg"); //name of file
        Uri temp = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, temp);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); //1 means high quality
        startActivityForResult(intent, 0);

    }*/

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0){
            switch (resultCode){
                case Activity.RESULT_OK:
                    if(file.exists()){
                        Toast.makeText(this, "Plik zapisany w "+file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(this, "Plik NIE został zapisany "+file.getAbsolutePath(), Toast.LENGTH_LONG).show();

                    }
                    break;
                case Activity.RESULT_CANCELED:
                    break;
                    default:
                        break;
            }
        }
    }*/
    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == AddMedicine.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }*/

    @OnClick(R.id.tv_opendate_add)
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

    @OnClick(R.id.tv_expdate_add)
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

    @OnClick(R.id.btn_scanBarcode_add)
    public void OpenBarcodeScannerActivity(){
        Intent intent= new Intent(this, BarcodeScanner.class);
        startActivity(intent);
    }

}
