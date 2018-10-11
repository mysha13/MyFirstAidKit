package com.example.elasz.myfirstaidkit;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBAmountFormAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBFormAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBMedicamentInfoAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBPurposeAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBUserMedicamentsAdapter;
import com.example.elasz.myfirstaidkit.Interfaces.RecyclerViewClickListener;
import com.example.elasz.myfirstaidkit.Medicaments.ShortMedInfoItem;
import com.example.elasz.myfirstaidkit.Medicaments.TakeMedItem;
import com.example.elasz.myfirstaidkit.Medicaments.TakeMedItemAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TakeMedicine extends AppCompatActivity {


    private DBUserMedicamentsAdapter dbUserMed;
    private DBFormAdapter dbForm;
    private DBPurposeAdapter dbPurpose;
    private DBMedicamentInfoAdapter dbMedInfo;
    private DBAmountFormAdapter dbAmountForm;

    private ArrayList<String> medicamentsName;
    private ArrayAdapter<String> adapterMedicamentsName;

    private ArrayList<TakeMedItem> meds = new ArrayList<>();
    TakeMedItemAdapter takemedadapter;
    private ArrayList<String> medNames;
    // ArrayList<ShortMedInfoItem> medicaments;
    private ArrayAdapter<String> adapterMedName;

    Context context = this;

    @BindView(R.id.autoCompletetv_find_takeMed)
    AutoCompleteTextView autoComTV_findname;

    @BindView(R.id.recyclerView_takeMed)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_medicine);
        ButterKnife.bind(this);
        setRecyclerView();
        autoCompleteFindByName();
        getBundle();
        initialize();
    }

    public void initialize() {
        /*RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position, String id, int bNumber) {
                OneMedicineInformation.this.ButtonNumber(id, bNumber);
            }
        };*/
        RecyclerViewClickListener listener = (view, position, id, bNumber) -> {
            ButtonNumber(id, bNumber);
        };
        takemedadapter = new TakeMedItemAdapter(meds, listener);
        recyclerView.setAdapter(takemedadapter);
    }
    private void ButtonNumber(String id, int btn_nb) {
        if (btn_nb == 1) {
            //TakeMedButton(id);
        } else if (btn_nb == 2) {
            //CancelMedButton(id);
        }
    }
    private void setRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void autoCompleteFindByName() {
        getListOfMedicamentsNames();
        adapterMedName = new ArrayAdapter<String>
                (this, R.layout.spin_item, medicamentsName);
        autoComTV_findname.setThreshold(1);
        autoComTV_findname.setAdapter(adapterMedName);
        autoComTV_findname.setTextColor(Color.BLACK);
    }

    private void getListOfMedicamentsNames() {
        dbUserMed = new DBUserMedicamentsAdapter(this);
        medicamentsName = new ArrayList<>();
        medicamentsName.clear();
        CreateMedicamentsList();
    }
    private void CreateMedicamentsList() {
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
    private void getBundle() {
        meds = new ArrayList<>();
        //numberOfMeds.setText("Wszystkie leki dodane");
        getMed();
    }

    private void getMed() {
        meds.clear();
        getMedicamentItem();

        if (!(meds.size() < 1)) {
            recyclerView.setAdapter(takemedadapter);
        }
    }
    private void getMedicamentItem() {
        setDBAdapters();
        dbUserMed.OpenDB();
        Cursor cursor = dbUserMed.GetAllUserMedicamentInfoData();
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
        CreateMedList(cursor, dbUserMed,dbMedInfo,dbAmountForm, meds);
    }
    private void setDBAdapters() {
        dbUserMed = new DBUserMedicamentsAdapter(this);
        dbMedInfo = new DBMedicamentInfoAdapter(this);
        dbForm = new DBFormAdapter(this);
        dbPurpose = new DBPurposeAdapter( this);
    }

    public void CreateMedList(Cursor cursor, DBUserMedicamentsAdapter dbUserMed, DBMedicamentInfoAdapter dbMedInfo, DBAmountFormAdapter dbAmountForm, ArrayList<TakeMedItem> medicaments) {
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int powerid = cursor.getInt(2);

                String amount = cursor.getString(7);
                int amountformid = cursor.getInt(8);

                Bitmap image=ConvertByteArrayToImage(cursor);
                dbUserMed.CloseDB();

                String amountform = getAmountFormName(dbAmountForm,amountformid);

                String power = getPowerName(dbMedInfo, powerid);
                TakeMedItem shortmed = new TakeMedItem(id, name, power, amountform, amount, image);
                medicaments.add(shortmed);
                /*if(name==autoComTV_findname.getText().toString())
                {
                    medicaments.add(shortmed);
                }*/

            }
        }

    }
    private Bitmap ConvertByteArrayToImage(Cursor cur){
        if (cur.getBlob(12)!= null) {
            byte[] imgByte = cur.getBlob(12);
            return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        } else {
            return null;
        }
       // byte[] imgByte = cur.getBlob(12);
        //return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
    }

    public String getAmountFormName(DBAmountFormAdapter dAForm, int id) {
        dAForm.OpenDB();
        String form = dAForm.GetAmountFormName(id);
        dAForm.CloseDB();
        return form;
    }

    public String getPowerName(DBMedicamentInfoAdapter dAForm, int id) {
        dAForm.OpenDB();
        String form = dAForm.GetPower(id);
        dAForm.CloseDB();
        return form;
    }
}
