package com.example.elasz.myfirstaidkit;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
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
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateMedicine extends AppCompatActivity {

    private DBUserMedicamentsAdapter dbUserMed;
    private DBFormAdapter dbForm;
    private DBPurposeAdapter dbPurpose;
    private DBAmountFormAdapter dbAmountForm;
    private DBMedicamentInfoAdapter dbMedInfo;
    private DBPersonAdapter dbPerson;

    private ArrayAdapter<String> adapterForm;
    private ArrayAdapter<String> adapterAmoutForm;
    private ArrayAdapter<String> adapterPurpose;
    private ArrayAdapter<String> adapterPerson;
    private ArrayList<String> formList;
    private ArrayList<String> purposeList;
    private ArrayList<String> amountFormList;
    private ArrayList<String> personList;

    @BindView(R.id.imageView_update)
    ImageView imageView;

    @BindView(R.id.tv_id_update)
    TextView tv_id;
    private int idmed;


    @BindView(R.id.et_name_update)
    EditText name;
    private String todb_name;

    @BindView(R.id.tv_expdate_update)
    TextView expdate;
    private String todb_expdate;

    @BindView(R.id.tv_opendate_update)
    TextView opendate;
    private String todb_opendate;

    @BindView(R.id.spin_form_update)
    Spinner spin_form;
    private String todb_form;

    @BindView(R.id.tv_lastform_update)
    TextView lastform;

    @BindView(R.id.spin_purpose_update)
    Spinner spin_purpose;
    private String todb_purpose;

    @BindView(R.id.tv_lastpurpose_update)
    TextView lastpurpose;

    @BindView(R.id.et_amount_update)
    EditText amount;
    private String todb_amount;

    @BindView(R.id.tv_lastamount_update)
    TextView lastamount;

    @BindView(R.id.spin_amountform_update)
    Spinner spin_amountForm;
    private String todb_amountForm;

    @BindView(R.id.tv_lastamountform_update)
    TextView lastamountform;

    @BindView(R.id.spin_person_update)
    Spinner spin_person;
    private int todb_person;

    @BindView(R.id.tv_lastperson_update)
    TextView lastperson;

    @BindView(R.id.et_power_update)
    EditText power;
    private String todb_power;

    @BindView(R.id.et_subsActive_update)
    EditText subsActive;
    private String todb_subsActive;

    @BindView(R.id.et_code_update)
    EditText code;
    private String todb_code;

    @BindView(R.id.et_producer_update)
    EditText producer;
    private String todb_producer;

    @BindView(R.id.et_note_update)
    EditText note;
    private String todb_note;

    @BindView(R.id.btn_addPhoto_update)
    FloatingActionButton addPhoto;

    @BindView(R.id.btn_removePhoto_update)
    FloatingActionButton removePhoto;

    @BindView(R.id.cb_istaken_update)
    CheckBox istake;

    private int formid;
    private int purposeid;
    private int amountformid;
    private int personid;
    private int medinfoid;
    private byte[] convertedimage;

    public static String codecode;
    public static int codefromscanner;

    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListenerForOpen;
    private static final String TAG = "UpdateMedicine";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_medicine);
        ButterKnife.bind(this);
        int id=getID();
       // Intent intent = getIntent();
       // String id = intent.getStringExtra("MedIdUpdate");
        tv_id.setText(String.valueOf(id));
        setDBAdapter();
        idmed=id;
        dbUserMed.OpenDB();
        String idmedinfo = dbUserMed.GetColumnContent(DatabaseConstantInformation.ID_MEDICAMENT,id);
        dbUserMed.CloseDB();
        medinfoid = Integer.parseInt(idmedinfo);


        setSpinners();
        setAllLastedTexts();

        name.addTextChangedListener(mQueryWatcher); // dla kazdego edittexta tak


        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: yyyy-MM-dd: " + year + "-" + month + "-" + day);

                String date = year + "-" + month + "-" + day;//+ "-" + month + "-" + year;

                expdate.setText(date);

            }
        };
        mDateSetListenerForOpen = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                //Log.d(TAG, "onDateSet: dd-MM-yyyy: " + day + "-" + month + "-" + year);
                Log.d(TAG, "onDateSet: yyyy-MM-dd: " + year + "-" + month + "-" + day);
                String date =  year + "-" + month + "-" + day;//day + "-" + month + "-" + year;
                opendate.setText(date);

            }
        };

        //btn_saveMedicine_update
    }

    @OnClick(R.id.btn_scanBarcode_update)
    void scanBarcode(){
        Intent intent= new Intent(this, BarcodeScanner.class);
        startActivityForResult(intent, codefromscanner);
        BarcodeScanner bar= new BarcodeScanner();
        code.setText(bar.getCode().toString());
    }
    @OnClick(R.id.tv_opendate_update)
    public void getOpenDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(
                UpdateMedicine.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListenerForOpen,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @OnClick(R.id.tv_expdate_update)
    public void getExpDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(
                UpdateMedicine.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @OnClick(R.id.btn_addPhoto_update)
    public void addPhotoUpdate(){
        showPictureDialog();
    }

    @OnClick(R.id.btn_removePhoto_update)
    public void removePhotoUpdate(){
        ClearImageView();
    }

    private void setSpinners(){
        spinnerForm(dbForm, formList, adapterForm, spin_form);
        spinnerPurpose(dbPurpose, purposeList, adapterPurpose, spin_purpose);
        spinnerAmountForm(dbAmountForm, amountFormList, adapterAmoutForm, spin_amountForm);
        spinnerPerson(dbPerson, personList, adapterPerson, spin_person);
    }
    private TextWatcher mQueryWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            name.setTextColor(tv_id.getCurrentTextColor());

        }

        @Override
        public void afterTextChanged(Editable s) {
            //shortmedadapter.filter(s.toString());

        }
    };

    private void setDBAdapter() {
        dbUserMed = new DBUserMedicamentsAdapter(this);
        dbForm = new DBFormAdapter(this);
        dbPurpose = new DBPurposeAdapter(this);
        dbAmountForm = new DBAmountFormAdapter(this);
        dbMedInfo = new DBMedicamentInfoAdapter(this);
        dbPerson = new DBPersonAdapter(this);
    }

    private void setAllLastedTexts() {

        ColorStateList redColor=lastamount.getTextColors();
        int id = getID();
        dbUserMed.OpenDB();
        Bitmap imageBitmap = ConvertByteArrayToImage(id);
        imageView.setImageBitmap(imageBitmap);
        name.setText(dbUserMed.GetColumnContent(DatabaseConstantInformation.NAME, id));
        name.setTextColor(redColor);
        int idmedinfo = Integer.valueOf(dbUserMed.GetColumnContent(DatabaseConstantInformation.ID_MEDICAMENT, id));
        //image.setImageBitmap(dbUser.GetColumnContent(DatabaseConstantInformation.IMAGE, id));

        expdate.setText(dbUserMed.GetColumnContent(DatabaseConstantInformation.EXPDATE, id));
        expdate.setTextColor(redColor);
        opendate.setText(dbUserMed.GetColumnContent(DatabaseConstantInformation.OPENDATE, id));
        opendate.setTextColor(redColor);
        int formid = Integer.valueOf(dbUserMed.GetColumnContent(DatabaseConstantInformation.FORM, id));
        int purposeid = Integer.valueOf(dbUserMed.GetColumnContent(DatabaseConstantInformation.PURPOSE, id));
        amount.setText(dbUserMed.GetColumnContent(DatabaseConstantInformation.AMOUNT,id));
        amount.setTextColor(redColor);
        int amountformid = Integer.valueOf(dbUserMed.GetColumnContent(DatabaseConstantInformation.AMOUNT_FORM, id));
//        int personid = Integer.valueOf(dbUser.GetColumnContent(DatabaseConstantInformation.PERSON, id));
        note.setText(dbUserMed.GetColumnContent(DatabaseConstantInformation.NOTE, id));
        note.setTextColor(redColor);

        dbUserMed.CloseDB();


        dbForm.OpenDB();   // tak dla kazdej tabeli
        lastform.setText(dbForm.GetFormName(formid));
        lastform.setTextColor(redColor);
        dbForm.CloseDB();

        dbMedInfo.OpenDB();
        power.setText(dbMedInfo.GetPower(idmed));
        power.setTextColor(redColor);
        subsActive.setText(dbMedInfo.GetSubsActive(idmed));
        subsActive.setTextColor(redColor);
        code.setText(dbMedInfo.GetCode(idmed));
        code.setTextColor(redColor);
        producer.setText(dbMedInfo.GetProducer(idmed));
        producer.setTextColor(redColor);
        dbMedInfo.CloseDB();

        if(purposeid == 0){
            lastpurpose.setText("-");
        }else{
            dbPurpose.OpenDB();
            lastpurpose.setText(dbPurpose.GetPurposeName(purposeid));
            lastpurpose.setTextColor(redColor);
            dbPurpose.CloseDB();
        }


        dbAmountForm.OpenDB();
        lastamountform.setText(dbAmountForm.GetAmountFormName(amountformid));
        lastamountform.setTextColor(redColor);
        dbAmountForm.CloseDB();

        /*if(personid == 0){
            lastperson.setText("-");
        }
        else{
            dbPerson.OpenDB();
            lastperson.setText(dbPerson.GetPersonName(personid));
            dbPerson.CloseDB();
        }*/



        //to set spinner value
       /* String compareValueForm = dbFrom.GetFormName(formid);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.select_state, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_form.setAdapter(adapter);
        if (compareValueForm != null) {
            int spinnerPosition = adapter.getPosition(compareValueForm);
            spin_form.setSelection(spinnerPosition);
        }*/

    }

    public void spinnerForm(DBFormAdapter dAForm, ArrayList<String> formList, ArrayAdapter<String> adapterForm, Spinner spinner) {
        dAForm = new DBFormAdapter(this);
        formList = new ArrayList<>();
        adapterForm = new ArrayAdapter<String>(this, R.layout.spin_item, formList);
        dAForm.OpenDB();
        formList.add("-");
        Cursor cursor = dAForm.GetAllForms();
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            formList.add(name);
        }
        dAForm.CloseDB();
        spinner.setAdapter(adapterForm);
    }
    public void spinnerPurpose(DBPurposeAdapter dbPurposeAdapter, ArrayList<String> purposeList, ArrayAdapter<String> adapterPurpose, Spinner spinner) {
        dbPurposeAdapter = new DBPurposeAdapter(this);
        purposeList = new ArrayList<>();
        adapterPurpose= new ArrayAdapter<String>(this, R.layout.spin_item, purposeList);
        dbPurposeAdapter.OpenDB();
        purposeList.add("-");
        Cursor cursor = dbPurposeAdapter.GetAllPurposes();
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            purposeList.add(name);
        }
        dbPurposeAdapter.CloseDB();
        spinner.setAdapter(adapterPurpose);
    }
    public void spinnerAmountForm(DBAmountFormAdapter dbAmountFormAdapter, ArrayList<String> amountformList, ArrayAdapter<String> adapterAmountForm, Spinner spinner) {
        dbAmountFormAdapter = new DBAmountFormAdapter(this);
        amountformList = new ArrayList<>();
        adapterAmountForm = new ArrayAdapter<String>(this, R.layout.spin_item, amountformList);
        dbAmountFormAdapter.OpenDB();
        amountformList.add("-");
        Cursor cursor = dbAmountFormAdapter.GetAllAmountForms();
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            amountformList.add(name);
        }
        dbAmountFormAdapter.CloseDB();
        spinner.setAdapter(adapterAmountForm);
    }
    public void spinnerPerson(DBPersonAdapter dbPersonAdapter, ArrayList<String> personList, ArrayAdapter<String> adapterPerson, Spinner spinner) {
        dbPersonAdapter = new DBPersonAdapter(this);
        personList = new ArrayList<>();
        adapterPerson = new ArrayAdapter<String>(this, R.layout.spin_item, personList);
        dbPersonAdapter.OpenDB();
        personList.add("-");
        Cursor cursor = dbPersonAdapter.GetAllPeople();
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            personList.add(name);
        }
        dbPersonAdapter.CloseDB();
        spinner.setAdapter(adapterPerson);
    }

    private Bitmap ConvertByteArrayToImage(int id) {
        dbUserMed.OpenDB();
        byte[] imagebyte = dbUserMed.getImageByteArray(id);
        if (imagebyte != null) {
            return BitmapFactory.decodeByteArray(imagebyte, 0, imagebyte.length);
        } else {
            return null;
        }
    }

    private int getID() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            return bundle.getInt("MedIdUpdate");
        } else {
            return 0;

        }
    }

    @OnClick(R.id.btn_saveMedicine_update)
    void updateMed() {
        if(name.getText().toString().matches("")){
            Toast.makeText(UpdateMedicine.this, "Nazwa jest pusta", Toast.LENGTH_LONG).show();
        }else if(amount.getText().toString().matches("")){
            Toast.makeText(UpdateMedicine.this, "Ilość jest puste", Toast.LENGTH_LONG).show();
        }else if(expdate.getText().toString().matches("")){
            Toast.makeText(UpdateMedicine.this, "Data ważności EXP jest pusta", Toast.LENGTH_LONG).show();
        }else if(code.getText().toString().matches("")){
            Toast.makeText(UpdateMedicine.this, "Kod jest pusty", Toast.LENGTH_LONG).show();
        }else if(spinnerCorrection(spin_form) == null){
            Toast.makeText(UpdateMedicine.this, "Postać jest pusta", Toast.LENGTH_LONG).show();
        }else if(spinnerCorrection(spin_amountForm) == null){
            Toast.makeText(UpdateMedicine.this, "Forma ilości jest pusta", Toast.LENGTH_LONG).show();
        }else{
            setDatabaseAdapter();
            CheckResult();
        }
    }

    private void CheckResult() {
        long work2 = TryToUpdateMedInfo();
        long work = TryToUpdateMedUser();

        if (work > 0 && work2 >0 ) {
            Toast.makeText(UpdateMedicine.this, "Zaktualizowano lek", Toast.LENGTH_LONG).show();
            //spinnerForm(dbForm, formList, adapterForm, spin_form);
           // spinnerPerson(dbPerson,personList, adapterPerson, spin_person);
            //spinnerPurpose(dbPurpose, purposeList, adapterPurpose, spin_purpose);
            //spinnerAmountForm(dbAmountForm, amountFormList, adapterAmoutForm, spin_amountForm);
        } else {
            Toast.makeText(UpdateMedicine.this,"Nie udało się dodać leku", Toast.LENGTH_LONG).show();
        }
    }
    private long TryToUpdateMedInfo(){
        CheckEmptyFields();
        dbMedInfo.OpenDB();
        long work2 = updateMedInDBMedInfo();
        dbMedInfo.CloseDB();
        return work2;
    }

    private long TryToUpdateMedUser(){
        IsThereAnyImage();
        CheckEmptyFields();
        if(name.getText() != null){
            medinfoid = getMedInfoID(dbMedInfo, name.getText().toString());
        }
        if(spinnerCorrection(spin_form) == null)
        {
            formid = 0;
        }
        else{
            formid = getFormID(dbForm, spinnerCorrection(spin_form));
        }
        if(spinnerCorrection(spin_purpose) == null) {
            purposeid = 0;
        }
        else{
            purposeid = getPurposeID(dbPurpose, spinnerCorrection(spin_purpose));
        }
        if(spinnerCorrection(spin_amountForm) == null) {
            amountformid = 0;
        }
        else{
            amountformid = getAmountFormID(dbAmountForm, spinnerCorrection(spin_amountForm));
        }
        if(spinnerCorrection(spin_person) == null) {
            personid = 0;
        }
        else{
            personid = getPersonID(dbPerson, spinnerCorrection(spin_person));
        }

        dbUserMed.OpenDB();
        long work = updateMedInUserMed();
        dbUserMed.CloseDB();
        return work;
    }

    public int getMedInfoID(DBMedicamentInfoAdapter dbMed, String name){
        dbMed.OpenDB();
        medinfoid = dbMed.GetMedId(name);
        dbMed.CloseDB();
        return medinfoid;

    }
    public int getPurposeID(DBPurposeAdapter dbPurpose, String name) {
        dbPurpose.OpenDB();
        purposeid = dbPurpose.GetPurposeId(name);
        dbPurpose.CloseDB();
        return purposeid;
    }
    public int getAmountFormID(DBAmountFormAdapter dbAmountForm, String name) {
        dbAmountForm.OpenDB();
        amountformid = dbAmountForm.GetAmountFormId(name);
        dbAmountForm.CloseDB();
        return amountformid;
    }
    public int getPersonID(DBPersonAdapter dbPersonAdapter, String name) {

        dbPersonAdapter.OpenDB();
        personid = dbPersonAdapter.GetPersonId(name);
        dbPersonAdapter.CloseDB();
        return personid;
    }
    public int getFormID(DBFormAdapter dAForm, String name) {
        dAForm.OpenDB();
        int form = dAForm.GetFormId(name);
        dAForm.CloseDB();
        return form;
    }

    private long updateMedInUserMed() {
        return dbUserMed.UpdateRowUserMedInfo(idmed,name.getText().toString(),
                medinfoid,
                todb_expdate,//expdate.getText().toString(),
                todb_opendate,//opendate.getText().toString(),
                formid,
                purposeid,
                Double.parseDouble(amount.getText().toString().replaceAll(",",".")),
                amountformid,
                null,
                todb_note, //note.getText().toString());
                istake.isChecked(),
                convertedimage);
    }

    private long updateMedInDBMedInfo() {
        return dbMedInfo.UpdateRowMedInfo(medinfoid,name.getText().toString(),
                todb_power,
                todb_subsActive,
                codecode,
                todb_producer);
    }

    private void CheckEmptyFields() {
        if(expdate.getText()==null){
            todb_expdate=null;
        }
        else{
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
                if(code.getText().toString().length()!=13){
                    Toast.makeText(UpdateMedicine.this, "Nieprawidłowa długość kodu", Toast.LENGTH_LONG).show();
                }else{
                    todb_code=code.getText().toString();
                }
            }
        }
        if(note.getText()==null){
            todb_note=null;
        }
        else{
            todb_note=note.getText().toString();
        }
    }

    private void IsThereAnyImage() {
        try{
            Bitmap mybitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            if(mybitmap == null){
                convertedimage=null;
            }
            else{
                convertedimage=ConvertImageToByteArray(mybitmap);
            }
        }
        catch(Exception ex){
            Log.e("No Image", ex.toString());
        }

    }

    public static byte[] ConvertImageToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    private String spinnerCorrection(Spinner spinner) {
        if (spinner.getSelectedItem().toString().matches("-")) {
            return null;
        } else
            return spinner.getSelectedItem().toString();
    }

    private void setDatabaseAdapter() {
        dbUserMed = new DBUserMedicamentsAdapter(this);
        dbForm = new DBFormAdapter(this);
        dbPurpose = new DBPurposeAdapter(this);
        dbAmountForm = new DBAmountFormAdapter(this);
        dbMedInfo = new DBMedicamentInfoAdapter(this);
        dbPerson = new DBPersonAdapter(this);
    }

    private void checkIfUpdateWorked(int id, int idmed, String todb_name, String todb_expdate, String todb_opendate, int todb_form, int todb_purpose, String todb_amount, int todb_amountForm, int todb_person, String todb_note, boolean istake, byte[] image) {
        long didItWork;
        if (id != 0) {
            didItWork = getDidItWork(id, idmed, todb_name, todb_expdate, todb_opendate, todb_form, todb_purpose, todb_amount, todb_amountForm, todb_person, todb_note, istake, image);
        } else {
            didItWork = 0;
        }
        if (didItWork > 0) {
            Toast.makeText(UpdateMedicine.this, "Sukces, zaktualizowano lek", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(UpdateMedicine.this, "Nie udało się, porazka", Toast.LENGTH_LONG).show();
        }
    }

    private long getDidItWork(int id, int idmed, String name, String expdate, String opendate, int form, int purpose, String amount, int amountform, int person, String note, boolean istake, byte[] image ) {
        long didItWork;
        String per = String.valueOf(person);
        didItWork = dbUserMed.UpdateRowUserMedInfo(id, name,idmed,
                expdate,
                opendate,
                form,
                purpose,
                Double.parseDouble(amount.replaceAll(",", ".")),
                amountform,
                per,
                note,
                istake,
                image);
        return didItWork;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == codefromscanner){
            if (resultCode == Activity.RESULT_OK) {
                BarcodeScanner bar= new BarcodeScanner();
                String newText = data.getStringExtra(bar.PUBLIC_STATIC_STRING_IDENTIFIER);
                code.setText(newText);
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
                    Toast.makeText(UpdateMedicine.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    imageView.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(UpdateMedicine.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(UpdateMedicine.this, "Image Saved!", Toast.LENGTH_SHORT).show();
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
        }
    }

    private  void ClearImageView(){
        // imageView.setImageBitmap(null);
        //imageView.setImageDrawable(null);
        imageView.setImageBitmap(null);
        imageView.setImageResource(0);
        //imageView.setImageURI(null);
    }


}
