package com.example.elasz.myfirstaidkit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBAmountFormAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBFormAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBMedicamentInfoAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBPersonAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBPurposeAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBUserMedicamentsAdapter;
import com.example.elasz.myfirstaidkit.DatabaseImplement.DatabaseConstantInformation;

import java.util.ArrayList;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_info_medicine);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String id = intent.getStringExtra("MedId");
        setAllTextView();

    }

    private void setAllTextView() {
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
        dbUser.OpenDB();
        name.setText(dbUser.GetColumnContent(DatabaseConstantInformation.NAME, id));
        //image.setImageBitmap(dbUser.GetColumnContent(DatabaseConstantInformation.IMAGE, id));
        amount.setText(dbUser.GetColumnContent(DatabaseConstantInformation.AMOUNT, id));
        expdate.setText(dbUser.GetColumnContent(DatabaseConstantInformation.EXPDATE, id));
        opendate.setText(dbUser.GetColumnContent(DatabaseConstantInformation.OPENDATE, id));
        int formid = Integer.valueOf(dbUser.GetColumnContent(DatabaseConstantInformation.FORM, id));
        //int place = Integer.valueOf(dbUser.GetColumnContent(DatabaseConstantInformation.PLACE, id));
        dbUser.CloseDB();

        dbFrom.OpenDB();   // tak dla kazdej tabeli
        form.setText(dbFrom.GetFormName(formid));
        dbFrom.CloseDB();


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
