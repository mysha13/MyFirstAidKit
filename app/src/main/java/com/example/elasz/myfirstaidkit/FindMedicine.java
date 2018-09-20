package com.example.elasz.myfirstaidkit;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBFormAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBMedicamentInfoAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBPurposeAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBUserMedicamentsAdapter;
import com.example.elasz.myfirstaidkit.DatabaseImplement.DatabaseConstantInformation;
import com.example.elasz.myfirstaidkit.Medicaments.ShortMedInfoItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindMedicine extends AppCompatActivity {


    private DBUserMedicamentsAdapter dbUserMed;
    private DBFormAdapter dbForm;
    private DBPurposeAdapter dbPurpose;

    private ArrayList<String> medicamentsName;
    private ArrayAdapter<String> adapterMedicamentsName;
    private ArrayList<ShortMedInfoItem> meds = new ArrayList<>();

    @BindView(R.id.autoCompletetv_findbyname)
    AutoCompleteTextView autoComTV_findname;

    @BindView(R.id.et_findbycode)
    EditText et_findcode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_medicine);
        ButterKnife.bind(this);
        autoCompleteFindByName();
    }

    private void autoCompleteFindByName() {
        getListOfMedicamentsNames();
        autoComTV_findname.setThreshold(1);
        autoComTV_findname.setAdapter(adapterMedicamentsName);
        autoComTV_findname.setTextColor(Color.BLACK);
    }

    private void getListOfMedicamentsNames() {
        dbUserMed = new DBUserMedicamentsAdapter(this);
        medicamentsName = new ArrayList<>();
        medicamentsName.clear();
        addNameToList();
    }

    private void addNameToList() {
        dbUserMed.OpenDB();
        Cursor cursor = dbUserMed.getNames();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                medicamentsName.add(name);
            }
        }
        dbUserMed.CloseDB();
    }
    
    @OnClick(R.id.btn_searchbyname)
    void searchMed() {
        setAdapters();
        meds.clear();
        Cursor cursor;
        cursor=dbUserMed.FindUserMedicamentByName(autoComTV_findname.getText().toString(), DatabaseConstantInformation.NAME);
        ClearFields();
        CreateMedList(cursor, dbUserMed,dbForm,dbPurpose, meds);

    }

    private void setAdapters() {
        dbUserMed= new DBUserMedicamentsAdapter(this) ;
        dbForm= new DBFormAdapter(this) ;
        dbPurpose = new DBPurposeAdapter(this) ;
    }

    private void ClearFields() {
        et_findcode.getText().clear();
        autoComTV_findname.getText().clear();
    }


    public void CreateMedList(Cursor cursor, DBUserMedicamentsAdapter dbUserMed, DBFormAdapter dbForm, DBPurposeAdapter dbPurpose, ArrayList<ShortMedInfoItem> medicaments) {
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String expdate = cursor.getString(2);
                //int formInt = cursor.getInt(3);
                String form=cursor.getString(3);

                String purpose = cursor.getString(4);
                String amount = cursor.getString(5);
                //double amount = cursor.getDouble(5);
                String amountform=cursor.getString(6);
                String power= cursor.getString(7);

                dbUserMed.CloseDB();

               // String form = getFormName(dAForm, formInt);

                ShortMedInfoItem shortmed = new ShortMedInfoItem(id, name, expdate, form, purpose,amount, amountform, power);
                medicaments.add(shortmed);

            }
        }
    }
}
