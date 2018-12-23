package com.example.elasz.myfirstaidkit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBAmountFormAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBFormAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBMedicamentInfoAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBPersonAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBPurposeAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBUserMedicamentsAdapter;
import com.example.elasz.myfirstaidkit.DatabaseImplement.DatabaseConstantInformation;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewAllInfoMedicine extends AppCompatActivity {

    @BindView(R.id.imageView_allinfo)
    ImageView image;
    @BindView(R.id.tv_name_allinfo)
    TextView name;
    @BindView(R.id.tv_expdate_allinfo)
    TextView expdate;
    @BindView(R.id.tv_opendate_allinfo)
    TextView opendate;
    @BindView(R.id.tv_form_allinfo)
    TextView form;
    @BindView(R.id.tv_purpose_allinfo)
    TextView purpose;
    @BindView(R.id.tv_amount_allinfo)
    TextView amount;
    @BindView(R.id.tv_amountform_allinfo)
    TextView amountform;
    @BindView(R.id.tv_person_allinfo)
    TextView person;
    @BindView(R.id.tv_power_allinfo)
    TextView power;
    @BindView(R.id.tv_subsActive_allinfo)
    TextView subsactive;
    @BindView(R.id.tv_code_allinfo)
    TextView code;
    @BindView(R.id.tv_producer_allinfo)
    TextView producer;
    @BindView(R.id.tv_note_allinfo)
    TextView note;


    private DBUserMedicamentsAdapter dbUser;
    private DBFormAdapter dbFrom;
    private DBPurposeAdapter dbPurpose;
    private DBAmountFormAdapter dbAmountForm;
    private DBMedicamentInfoAdapter dbMed;
    private DBPersonAdapter dbPerson;

   // private ArrayList<String> formList;
   private static final String TAG = "ViewAllInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_info_medicine);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String id = intent.getStringExtra("MedId");
        setDBAdapter();
        setTextView();

    }

    private void setDBAdapter() {
        dbUser = new DBUserMedicamentsAdapter(this);
        dbFrom = new DBFormAdapter(this);
        dbPurpose = new DBPurposeAdapter(this);
        dbAmountForm = new DBAmountFormAdapter(this);
        dbMed = new DBMedicamentInfoAdapter(this);
        dbPerson = new DBPersonAdapter(this);
    }

    private void setTextView() {
        int id = getID();
        int personid=0;
        dbUser.openDB();
        Bitmap imageBitmap = convertByteArrayToImage(dbUser.getImageByteArray(id));
        image.setImageBitmap(imageBitmap);
        name.setText(dbUser.getColumnContent(DatabaseConstantInformation.NAME, id));
        int idmed = Integer.valueOf(dbUser.getColumnContent(DatabaseConstantInformation.ID_MEDICAMENT, id));
        //image.setImageBitmap(dbUser.getColumnContent(DatabaseConstantInformation.IMAGE, id));

        expdate.setText(dbUser.getColumnContent(DatabaseConstantInformation.EXPDATE, id));
        opendate.setText(dbUser.getColumnContent(DatabaseConstantInformation.OPENDATE, id));
        int formid = Integer.valueOf(dbUser.getColumnContent(DatabaseConstantInformation.FORM, id));
        int purposeid = Integer.valueOf(dbUser.getColumnContent(DatabaseConstantInformation.PURPOSE, id));
        amount.setText(dbUser.getColumnContent(DatabaseConstantInformation.AMOUNT,id));
        int amountformid = Integer.valueOf(dbUser.getColumnContent(DatabaseConstantInformation.AMOUNT_FORM, id));

        try{
            personid = Integer.valueOf(dbUser.getColumnContent(DatabaseConstantInformation.PERSON, id));
        }catch(Exception e){
            Log.e(TAG,e.toString());
        }
       //int personid = Integer.valueOf(dbUser.getColumnContent(DatabaseConstantInformation.PERSON, id));
        note.setText(dbUser.getColumnContent(DatabaseConstantInformation.NOTE, id));

        dbUser.closeDB();


        dbFrom.openDB();   // tak dla kazdej tabeli
        form.setText(dbFrom.getFormName(formid));
        dbFrom.closeDB();

        dbMed.openDB();
        power.setText(dbMed.getPower(idmed));
        subsactive.setText(dbMed.getSubsActive(idmed));
        code.setText(dbMed.getCode(idmed));
        producer.setText(dbMed.getProducer(idmed));
        dbMed.closeDB();

        if(purposeid == 0){
            purpose.setText("-");
        }else{
            dbPurpose.openDB();
            purpose.setText(dbPurpose.getPurposeName(purposeid));
            dbPurpose.closeDB();
        }


        dbAmountForm.openDB();
        amountform.setText(dbAmountForm.getAmountFormName(amountformid));
        dbAmountForm.closeDB();

        if(personid == 0){
            person.setText("-");
        }
        else{
            dbPerson.openDB();
            person.setText(dbPerson.getPersonName(personid));
            dbPerson.closeDB();
        }


    }
    private Bitmap convertByteArrayToImage(byte[] byteImage) {

        if (byteImage!= null) {
            return BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
        } else {
            return null;
        }/*
        dbUser.openDB();
        byte[] imagebyte = dbUser.getImageByteArray(id);
        if (imagebyte != null) {
            return BitmapFactory.decodeByteArray(imagebyte, 0, imagebyte.length);
        } else {
            return null;
        }*/
    }

    private int getID() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            return bundle.getInt("MedId");
        } else {
            return 0;

        }
    }

}
