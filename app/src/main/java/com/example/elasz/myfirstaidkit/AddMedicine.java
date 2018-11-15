package com.example.elasz.myfirstaidkit;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBAmountFormAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBFormAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBMedicamentInfoAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBPersonAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBPurposeAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBUserMedicamentsAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    public static void setCodecode(String codecode) {
        AddMedicine.codecode = codecode;
    }

    public static int codefromscanner;
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

    /*@BindView(R.id.imageView2)
    ImageView imageView;*/
    private ArrayAdapter<String> adapterForm;
    private ArrayAdapter<String> adapterAmoutForm;
    private ArrayAdapter<String> adapterPurpose;
    private ArrayAdapter<String> adapterPerson;
    private ArrayList<String> formList;
    private ArrayList<String> purposeList;
    private ArrayList<String> amountFormList;
    private ArrayList<String> personList;

    private byte[] convertedimage;

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

    private int formid;
    private int purposeid;
    private int amountformid;
    private int personid;
    private int medinfoid;

    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        ButterKnife.bind(this);
        spinnerForm(dbForm, formList, adapterForm, spin_form);
        spinnerPurpose(dbPurpose, purposeList, adapterPurpose, spin_purpose);
        spinnerAmountForm(dbAmountForm, amountFormList, adapterAmoutForm, spin_amountForm);
        spinnerPerson(dbPerson, personList, adapterPerson, spin_person);

        if(codecode!=null){
            code.setText(codecode);
        }
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

    }
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(AddMedicine.this, "Pozwolenie przyznane", Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "Pozwolenie odwołane");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
            }
        }
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
        }else if(spinnerCorrection(spin_form) == null){
            Toast.makeText(AddMedicine.this, "Postać jest pusta", Toast.LENGTH_LONG).show();
        }else if(spinnerCorrection(spin_amountForm) == null){
            Toast.makeText(AddMedicine.this, "Forma ilości jest pusta", Toast.LENGTH_LONG).show();
        }else{
            SetDatabaseAdapters();
            CheckResult();

        }

    }


    private void CheckResult() {
        long work2 = TryToAddToMedInfo();
        long work = TryToAddToUser();

        if (work > 0 && work2 >0 ) {
            Toast.makeText(AddMedicine.this, "Dodano lek", Toast.LENGTH_LONG).show();
            clearTextViews();
            spinnerForm(dbForm, formList, adapterForm, spin_form);
            spinnerPerson(dbPerson,personList, adapterPerson, spin_person);
            spinnerPurpose(dbPurpose, purposeList, adapterPurpose, spin_purpose);
            spinnerAmountForm(dbAmountForm, amountFormList, adapterAmoutForm, spin_amountForm);
        } else {
            Toast.makeText(AddMedicine.this,"Nie udało się dodać leku", Toast.LENGTH_LONG).show();
        }
    }

    private void clearTextViews() {
        name.setText("");
        expdate.setText("rrrr-MM-dd");
        opendate.setText("rrrr-MM-dd");
        amount.setText("");
        power.setText("");
        subsActive.setText("");
        code.setText("");
        producer.setText("");
        note.setText("");
        istake.setChecked(false);
        imageView.setImageBitmap(null);
        imageView.setImageResource(0);
    }

    private long TryToAddToMedInfo(){
        CheckEmptyFields();
        dbMedInfo.OpenDB();
        long work2 = addMedToDBMedInfo();
        dbMedInfo.CloseDB();
        return work2;
    }

    private long TryToAddToUser() {
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
        long work = addMedToDB();
        //long work2 = addMedToDBMedInfo();
        dbUserMed.CloseDB();
        return work;
    }

    private String spinnerCorrection(Spinner spinner) {
        if (spinner.getSelectedItem().toString().matches("-")) {
            return null;
        } else
            return spinner.getSelectedItem().toString();
    }

    public int getFormID(DBFormAdapter dAForm, String name) {
        dAForm.OpenDB();
        formid = dAForm.GetFormId(name);
        dAForm.CloseDB();
        return formid;
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

    private void CheckEmptyFields() {
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
        }/*finally {
            convertedimage=null;
        }*/

    }

    private long addMedToDB() {
        return dbUserMed.AddUserMedicamentData(name.getText().toString(),
                medinfoid,
                todb_expdate,//expdate.getText().toString(),
                todb_opendate,//opendate.getText().toString(),
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
        return dbMedInfo.AddMedicamentInfoData(name.getText().toString(),
                todb_power,
                todb_subsActive,
                codecode,
                todb_producer);
    }

    public static byte[] ConvertImageToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    private void SetDatabaseAdapters() {
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

    private  void ClearImageView(){
        imageView.setImageBitmap(null);
        imageView.setImageResource(0);
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
        startActivityForResult(intent, codefromscanner);
        BarcodeScanner bar= new BarcodeScanner();
        code.setText(bar.getCode().toString());
    }

}
