package com.example.elasz.myfirstaidkit;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBAmountFormAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBFormAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBMedicamentInfoAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBPersonAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBPurposeAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBUserMedicamentsAdapter;
import com.example.elasz.myfirstaidkit.DatabaseImplement.DatabaseConstantInformation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddMedicine extends AppCompatActivity {

    private static final String TAG = "AddMedicine";
    private DBMedicamentInfoAdapter dbMedInfo;
    private DBUserMedicamentsAdapter dbUserMed;
    private DBFormAdapter dbForm;
    private DBAmountFormAdapter dbAmountForm;
    private DBPersonAdapter dbPerson;
    private DBPurposeAdapter dbPurpose;

   /* public static void setCodecode(String codecode) {
        AddMedicine.codecode = codecode;
    }*/

    public int requestcodefromscanner;
    public static String codecode;

   // private DatabaseFormAdapter dAForm;
    @BindView(R.id.et_name_add)
    EditText name;
    private String todb_name;

    @BindView(R.id.tv_expdate_add)
    TextView expdate;
    private String todb_expdate;

    @BindView(R.id.tv_opendate_add)
    TextView opendate;
    private String todb_opendate;

    @BindView(R.id.spin_form_add)
    Spinner spin_form;
    private String todb_form;


    @BindView(R.id.spin_purpose_add)
    Spinner spin_purpose;
    private String todb_purpose;

    @BindView(R.id.et_amount_add)
    EditText amount;
    private String todb_amount;

    @BindView(R.id.spin_amountform_add)
    Spinner spin_amountForm;
    private String todb_amountForm;

    @BindView(R.id.spin_person_add)
    Spinner spin_person;
    private String todb_person;

    @BindView(R.id.et_power_add)
    EditText power;
    private String todb_power;

    @BindView(R.id.et_subsActive_add)
    EditText subsActive;
    private String todb_subsActive;

    @BindView(R.id.et_code_add)
    EditText code;
    private String todb_code;

    @BindView(R.id.et_producer_add)
    EditText producer;
    private String todb_producer;

    @BindView(R.id.et_note_add)
    EditText note;
    private String todb_note;

    @BindView(R.id.btn_addPhoto)
    FloatingActionButton addPhoto;

    @BindView(R.id.btn_removePhoto)
    FloatingActionButton removePhoto;

    @BindView(R.id.cb_istaken)
    CheckBox istake;

    @BindView(R.id.imageView_add)
    ImageView imageView;
    private ArrayAdapter<String> adapterForm;
    private ArrayAdapter<String> adapterAmoutForm;
    private ArrayAdapter<String> adapterPurpose;
    private ArrayAdapter<String> adapterPerson;
    private ArrayList<String> formList;
    private ArrayList<String> purposeList;
    private ArrayList<String> amountFormList;
    private ArrayList<String> personList;

    private byte[] convertedimage;

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

    @BindView(R.id.btn_scanBarcode_add)
    Button btnScanBarcode;


   /* private Button btn;
    private ImageView imageview;*/
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;
    private File filename = null;

    boolean isImageFitToScreen;
    private String fullScreenInd;
    //private ImageView imageView;

    //private Button saveMedicine;

    private int formid;
    private int purposeid;
    private int amountformid;
    private int personid;
    private int medinfoid;

    private int medexistedid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        ButterKnife.bind(this);
        setDatabaseAdapters();
        spinnerForm(dbForm, formList, adapterForm, spin_form);
        spinnerPurpose(dbPurpose, purposeList, adapterPurpose, spin_purpose);
        spinnerAmountForm(dbAmountForm, amountFormList, adapterAmoutForm, spin_amountForm);
        spinnerPerson(dbPerson, personList, adapterPerson, spin_person);

        /*if(codecode!=null){
            code.setText(codecode);
        }*/
        //name=(EditText) findViewById(R.id.et_name_add);
        //amount=(EditText) findViewById(R.id.et_amount_add);

        //imageView=(ImageView) findViewById(R.id.imageView_add);
        /*imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddMedicine.this, FullScreenPhoto.class);

                imageView.buildDrawingCache();
                Bitmap image= imageView.getDrawingCache();

                Bundle extras = new Bundle();
                extras.putParcelable("imagebitmap", image);
                intent.putExtras(extras);
                startActivity(intent);
               *//* Intent intent = new Intent(AddMedicine.this,
                        AddMedicine.class);

                if("y".equals(fullScreenInd)){
                    intent.putExtra("fullScreenIndicator", "");
                }else{
                    intent.putExtra("fullScreenIndicator", "y");
                }
                AddMedicine.this.startActivity(intent);*//*
            }
        });*/

        /*removePhoto=(FloatingActionButton) findViewById(R.id.btn_removePhoto);
        removePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearImageView();
            }
        });*/

       /* addPhoto=(FloatingActionButton) this.findViewById(R.id.btn_addPhoto);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
                // Toast.makeText(AddMedicine.this, "Kliknięto", Toast.LENGTH_SHORT).show();
            }
        });*/

        //expdate = (TextView) findViewById(R.id.tv_expdate_add);
        /*expdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getExpDate();
            }
        });*/

        //opendate=(TextView) findViewById(R.id.tv_opendate_add);
        /*opendate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOpenDate();
            }
        });*/


        /*btnScanBarcode = (Button) findViewById(R.id.btn_scanBarcode_add);
        //mDisplayDate = (TextView) findViewById(R.id.txt_expdate);
        btnScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenBarcodeScannerActivity();

                *//*Cursor cursor;
                if(code.getText()!=null)
                {
                    cursor = dbMedInfo.findMedicamentByCode(code.getText().toString(), DatabaseConstantInformation.CODE);
                    if(cursor.getCount()>0){
                        dbMedInfo.openDB();
                        medinfoid=dbMedInfo.getMedIdFromCode(code.getText().toString());
                        name.setText(dbMedInfo.getNameFromCode(code.getText().toString()));
                        power.setText(dbMedInfo.getPowerFromCode(code.getText().toString()));
                        subsActive.setText(dbMedInfo.getSubsActiveFromCode(code.getText().toString()));
                        //code.setText(dbMedInfo.getCode(idmed));
                        producer.setText(dbMedInfo.getProducerFromCode(code.getText().toString()));
                        dbMedInfo.closeDB();
                    }
                    else{
                        Toast.makeText(AddMedicine.this, "Lek wcześniej nie istniał", Toast.LENGTH_SHORT).show();
                    }
                }*//*


                *//*if(cursor.getCount()>0){
                    dbMedInfo.openDB();
                    medinfoid=dbMedInfo.getMedIdFromCode(code.getText().toString());
                    name.setText(dbMedInfo.getNameFromCode(code.getText().toString()));
                    power.setText(dbMedInfo.getPowerFromCode(code.getText().toString()));
                    subsActive.setText(dbMedInfo.getSubsActiveFromCode(code.getText().toString()));
                    //code.setText(dbMedInfo.getCode(idmed));
                    producer.setText(dbMedInfo.getProducerFromCode(code.getText().toString()));
                    dbMedInfo.closeDB();
                }
                else{
                    Toast.makeText(AddMedicine.this, "Lek wcześniej nie istniał", Toast.LENGTH_SHORT).show();
                }*//*
            }
        });
*/

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String dayToShow= String.valueOf(day);
                String monthToShow = String.valueOf(month);
                if(String.valueOf(day).length()==1){
                    dayToShow= "0"+String.valueOf(day);
                }
                if(String.valueOf(month).length()==1){
                    monthToShow="0"+String.valueOf(month);
                }
                Log.d(TAG, "onDateSet: yyyy-MM-dd: " + year + "-" + month + "-" + day);

                String date = year + "-" + monthToShow + "-" + dayToShow;//+ "-" + month + "-" + year;

                expdate.setText(date);

            }
        };
        mDateSetListenerForOpen = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String dayToShow= String.valueOf(day);
                String monthToShow = String.valueOf(month);
                if(String.valueOf(day).length()==1){
                    dayToShow= "0"+String.valueOf(day);
                }
                if(String.valueOf(month).length()==1){
                    monthToShow="0"+String.valueOf(month);
                }
                //Log.d(TAG, "onDateSet: dd-MM-yyyy: " + day + "-" + month + "-" + year);
                Log.d(TAG, "onDateSet: yyyy-MM-dd: " + year + "-" + month + "-" + day);
                String date =  year + "-" + monthToShow + "-" + dayToShow;//day + "-" + month + "-" + year;
                opendate.setText(date);

            }
        };

    }

    @OnClick(R.id.btn_scanBarcode_add)
    public void OpenBarcodeScannerActivity(){
        BarcodeScanner bar= new BarcodeScanner();
        bar.setCode(null);
        Intent intent= new Intent(this, BarcodeScanner.class);
        startActivityForResult(intent, requestcodefromscanner);
        //BarcodeScanner bar= new BarcodeScanner();
        //code.setText(bar.getCode());
    }

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

    @OnClick(R.id.btn_addPhoto)
    public void addPhotoClick(){
        showPictureDialog();
    }

    @OnClick(R.id.imageView_add)
    public void viewFullScreenPhoto(){
        Intent intent = new Intent(AddMedicine.this, FullScreenPhoto.class);
        intent.removeExtra("imagebitmap");
        try{
            imageView.buildDrawingCache();
            Bitmap image= imageView.getDrawingCache();

            Bundle extras = new Bundle();
            extras.putParcelable("imagebitmap", image);
            intent.putExtras(extras);
            startActivity(intent);
        }catch (Exception e){
            Log.e(TAG,e.toString());
            Toast.makeText(AddMedicine.this, "Nie można wczytać zdjęcia", Toast.LENGTH_SHORT).show();

        }
    }

    @OnClick(R.id.btn_removePhoto)
    public void removePhoto(){
        imageView.setImageBitmap(null);
        //imageView.setImageResource(0);
        imageView.setImageDrawable(null);
    }

    @OnClick(R.id.btn_saveAddedMedicine_add)
    void AddMedicineClick(){
        if(name.getText().toString().matches("")){
            Toast.makeText(AddMedicine.this, "Nazwa jest pusta", Toast.LENGTH_LONG).show();
        }else if(amount.getText().toString().matches("")){
            Toast.makeText(AddMedicine.this, "Ilość jest puste", Toast.LENGTH_LONG).show();
        }else if(expdate.getText().toString().matches("")){
            Toast.makeText(AddMedicine.this, "Data ważności EXP jest pusta", Toast.LENGTH_LONG).show();
        }else if(code.getText().toString().matches("")){
            Toast.makeText(AddMedicine.this, "Kod jest pusty", Toast.LENGTH_LONG).show();
        }else if(spinnerSelection(spin_form) == null){
            Toast.makeText(AddMedicine.this, "Postać jest pusta", Toast.LENGTH_LONG).show();
        }else if(spinnerSelection(spin_amountForm) == null){
            Toast.makeText(AddMedicine.this, "Forma ilości jest pusta", Toast.LENGTH_LONG).show();
        }else{
            checkIfAddWorked();
        }
    }

    /*private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(AddMedicine.this, "Pozwolenie przyznane", Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "Pozwolenie odwołane");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
            }
        }
    }*/

    public void spinnerForm(DBFormAdapter dAForm, ArrayList<String> formList, ArrayAdapter<String> adapterForm, Spinner spinner) {
        dAForm = new DBFormAdapter(this);
        formList = new ArrayList<>();
        adapterForm = new ArrayAdapter<String>(this, R.layout.spin_item, formList);
        dAForm.openDB();
        formList.add("-");
        Cursor cursor = dAForm.getAllForms();
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            formList.add(name);
        }
        dAForm.closeDB();
        spinner.setAdapter(adapterForm);
    }
    public void spinnerPurpose(DBPurposeAdapter dbPurposeAdapter, ArrayList<String> purposeList, ArrayAdapter<String> adapterPurpose, Spinner spinner) {
        dbPurposeAdapter = new DBPurposeAdapter(this);
        purposeList = new ArrayList<>();
        adapterPurpose= new ArrayAdapter<String>(this, R.layout.spin_item, purposeList);
        dbPurposeAdapter.openDB();
        purposeList.add("-");
        Cursor cursor = dbPurposeAdapter.getAllPurposes();
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            purposeList.add(name);
        }
        dbPurposeAdapter.closeDB();
        spinner.setAdapter(adapterPurpose);
    }
    public void spinnerAmountForm(DBAmountFormAdapter dbAmountFormAdapter, ArrayList<String> amountformList, ArrayAdapter<String> adapterAmountForm, Spinner spinner) {
        dbAmountFormAdapter = new DBAmountFormAdapter(this);
        amountformList = new ArrayList<>();
        adapterAmountForm = new ArrayAdapter<String>(this, R.layout.spin_item, amountformList);
        dbAmountFormAdapter.openDB();
        amountformList.add("-");
        Cursor cursor = dbAmountFormAdapter.getAllAmountForms();
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            amountformList.add(name);
        }
        dbAmountFormAdapter.closeDB();
        spinner.setAdapter(adapterAmountForm);
    }
    public void spinnerPerson(DBPersonAdapter dbPersonAdapter, ArrayList<String> personList, ArrayAdapter<String> adapterPerson, Spinner spinner) {
        dbPersonAdapter = new DBPersonAdapter(this);
        personList = new ArrayList<>();
        adapterPerson = new ArrayAdapter<String>(this, R.layout.spin_item, personList);
        dbPersonAdapter.openDB();
        personList.add("-");
        Cursor cursor = dbPersonAdapter.getAllPeople();
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            personList.add(name);
        }
        dbPersonAdapter.closeDB();
        spinner.setAdapter(adapterPerson);
    }





    private void checkIfAddWorked() {
        long work2 = tryToAddToMedInfo();
        long work = tryToAddToUser();

        if (work > 0 && work2 >0 ) {
            Toast.makeText(AddMedicine.this, "Dodano lek", Toast.LENGTH_LONG).show();
            clearFields();
            clearImageView();
            spinnerForm(dbForm, formList, adapterForm, spin_form);
            spinnerPerson(dbPerson,personList, adapterPerson, spin_person);
            spinnerPurpose(dbPurpose, purposeList, adapterPurpose, spin_purpose);
            spinnerAmountForm(dbAmountForm, amountFormList, adapterAmoutForm, spin_amountForm);
            finish();
        } else {
            Toast.makeText(AddMedicine.this,"Nie udało się dodać leku", Toast.LENGTH_LONG).show();
        }
    }

    private void clearFields() {
        name.setText("");
        expdate.setText("");
        opendate.setText("");
        amount.setText("");
        power.setText("");
        subsActive.setText("");
        code.setText("");
        producer.setText("");
        note.setText("");
        istake.setChecked(false);
        imageView.setImageBitmap(null);
        //imageView.setImageResource(0);
        imageView.setImageDrawable(null);
    }

    private long tryToAddToMedInfo(){
        checkEmptyFields();
        dbMedInfo.openDB();
        long work2 = addMedToDBMedInfo();
        dbMedInfo.closeDB();
        return work2;
    }

    private long tryToAddToUser() {
        isThereAnyImage();
        checkEmptyFields();
        if(name.getText() != null){
            medinfoid = getMedInfoID(dbMedInfo, name.getText().toString());
        }
        if(spinnerSelection(spin_form) == null)
        {
            formid = 0;
        }
        else{
            formid = getFormID(dbForm, spinnerSelection(spin_form));
        }
        if(spinnerSelection(spin_purpose) == null) {
            purposeid = 0;
        }
        else{
            purposeid = getPurposeID(dbPurpose, spinnerSelection(spin_purpose));
        }
        if(spinnerSelection(spin_amountForm) == null) {
            amountformid = 0;
        }
        else{
            amountformid = getAmountFormID(dbAmountForm, spinnerSelection(spin_amountForm));
        }
        if(spinnerSelection(spin_person) == null) {
            personid = 0;
        }
        else{
            personid = getPersonID(dbPerson, spinnerSelection(spin_person));
        }

        dbUserMed.openDB();
        long work = addMedToDB();
        //long work2 = addMedToDBMedInfo();
        dbUserMed.closeDB();
        return work;
    }

    private String spinnerSelection(Spinner spinner) {
        if (spinner.getSelectedItem().toString().matches("-")) {
            return null;
        } else
            return spinner.getSelectedItem().toString();
    }

    public int getFormID(DBFormAdapter dAForm, String name) {
        dAForm.openDB();
        formid = dAForm.getFormId(name);
        dAForm.closeDB();
        return formid;
    }

    public int getMedInfoID(DBMedicamentInfoAdapter dbMed, String name){
        dbMed.openDB();
        medinfoid = dbMed.getMedId(name);
        dbMed.closeDB();
        return medinfoid;

    }
    public int getPurposeID(DBPurposeAdapter dbPurpose, String name) {
        dbPurpose.openDB();
        purposeid = dbPurpose.getPurposeId(name);
        dbPurpose.closeDB();
        return purposeid;
    }
    public int getAmountFormID(DBAmountFormAdapter dbAmountForm, String name) {
        dbAmountForm.openDB();
        amountformid = dbAmountForm.getAmountFormId(name);
        dbAmountForm.closeDB();
        return amountformid;
    }
    public int getPersonID(DBPersonAdapter dbPersonAdapter, String name) {

        dbPersonAdapter.openDB();
        personid = dbPersonAdapter.getPersonId(name);
        dbPersonAdapter.closeDB();
        return personid;
    }

    private void checkEmptyFields() {
        if(expdate.getText()==null){
            todb_expdate=null;
        }
        else{
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            todb_expdate=expdate.getText().toString();

        }

        if(opendate.getText()==null){
            todb_opendate=null;
        }
        else{
            todb_opendate=opendate.getText().toString();
        }

        if(power.getText()==null){
            todb_power=null;
        }
        else{
            todb_power=power.getText().toString();
        }

        if(subsActive.getText()==null){
            todb_subsActive=null;
        }
        else{
            todb_subsActive=subsActive.getText().toString();
        }

        if(producer.getText()==null){
            todb_producer=null;
        }
        else{
            todb_producer=producer.getText().toString();
        }

        if(code.getText().length() == 0){
            todb_code=null;
        }else{
            if(codecode!=null){
                todb_code=codecode;
            }else{
                todb_code=code.getText().toString();
            }
        }
        if(note.getText()==null){
            todb_note=null;
        }
        else{
            todb_note=note.getText().toString();
        }
    }

    private void isThereAnyImage() {
        try{
            Bitmap mybitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            if(mybitmap == null){
                convertedimage=null;
            }
            else{
                convertedimage=convertImageToByteArray(mybitmap);
            }
        }
        catch(Exception ex){
            Log.e("No Image", ex.toString());
        }/*finally {
            convertedimage=null;
        }*/

    }

    private long addMedToDB() {
        return dbUserMed.addUserMedicamentData(name.getText().toString(),
                medinfoid,
                todb_expdate,
                todb_opendate,
                formid,
                purposeid,
                Double.parseDouble(amount.getText().toString().replaceAll(",",".")),
                amountformid,
                personid,
                todb_note, //note.getText().toString());
                istake.isChecked(),
                convertedimage);
    }

    private long addMedToDBMedInfo() {
        if(medexistedid>0){
            return  dbMedInfo.updateRowMedInfo(medinfoid,name.getText().toString(),
                    todb_power,
                    todb_subsActive,
                    todb_code,
                    todb_producer);
        }
        else
        {   return dbMedInfo.addMedicamentInfoData(name.getText().toString(),
                    todb_power,
                    todb_subsActive,
                    todb_code,
                    todb_producer);
        }

    }

    public static byte[] convertImageToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    private void setDatabaseAdapters() {
        dbMedInfo = new DBMedicamentInfoAdapter(this);
        dbUserMed = new DBUserMedicamentsAdapter(this);
        dbForm = new DBFormAdapter(this);
        dbAmountForm = new DBAmountFormAdapter(this);
        dbPerson = new DBPersonAdapter(this);
        dbPurpose = new DBPurposeAdapter(this);
    }

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
                                imageView.setImageBitmap(null);
                                //imageView.setImageResource(0);
                                imageView.setImageDrawable(null);
                                choosePhotoFromGallery();
                                //Toast.makeText(AddMedicine.this, "Kliknięto o galeria", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                imageView.setImageBitmap(null);
                                //imageView.setImageResource(0);
                                imageView.setImageDrawable(null);
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
        try{
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, CAMERA);
            }
        }catch(Exception e){
            Toast.makeText(AddMedicine.this, "Brak pozwolenia na aparat", Toast.LENGTH_SHORT).show();
        }
       /* Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA);
        }*/
    }

    private  void clearImageView(){
        imageView.setImageBitmap(null);
        //imageView.setImageResource(0);
        imageView.setImageDrawable(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == requestcodefromscanner){
            if (resultCode == Activity.RESULT_OK) {
                BarcodeScanner bar= new BarcodeScanner();
                String scanCode = data.getStringExtra(bar.PUBLIC_STATIC_STRING_IDENTIFIER);
                code.setText(scanCode);

                Cursor cursor = checkIfMedExisted(scanCode);
                if(cursor.getCount()>0){
                    dbMedInfo.openDB();
                    medinfoid=dbMedInfo.getMedIdFromCode(scanCode);
                    medexistedid=medinfoid;
                    name.setText(dbMedInfo.getNameFromCode(scanCode));
                    power.setText(dbMedInfo.getPowerFromCode(scanCode));
                    subsActive.setText(dbMedInfo.getSubsActiveFromCode(scanCode));
                    producer.setText(dbMedInfo.getProducerFromCode(scanCode));
                    dbMedInfo.closeDB();
                    Toast.makeText(AddMedicine.this, "Lek wcześniej istniał, dane uzupełnione", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(AddMedicine.this, "Lek wcześniej nie istniał", Toast.LENGTH_SHORT).show();
                }

            }
        }
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
        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".png");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/png"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private Cursor checkIfMedExisted(String scancode) {
        dbMedInfo.openDB();
        Cursor cursor;
        if(code.getText()!=null)
        {
            cursor = dbMedInfo.findMedicamentByCode(scancode, DatabaseConstantInformation.CODE);
        }
        else{
            cursor=null;
        }
        return cursor;
    }

}
