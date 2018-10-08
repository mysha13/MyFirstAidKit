package com.example.elasz.myfirstaidkit;

import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;

import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBFormAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBMedicamentInfoAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBPurposeAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBUserMedicamentsAdapter;
import com.example.elasz.myfirstaidkit.DatabaseImplement.DatabaseConstantInformation;
import com.example.elasz.myfirstaidkit.Interfaces.RecyclerViewClickListener;
import com.example.elasz.myfirstaidkit.Medicaments.ShortMedInfoItem;
import com.example.elasz.myfirstaidkit.Medicaments.ShortMedInfoItemAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindMedicine extends AppCompatActivity {


    private DBUserMedicamentsAdapter dbUserMed;
    private DBFormAdapter dbForm;
    private DBPurposeAdapter dbPurpose;
    private DBMedicamentInfoAdapter dbMedInfo;

    private ArrayList<String> medicamentsName;
    private ArrayAdapter<String> adapterMedicamentsName;

    private ArrayList<ShortMedInfoItem> meds = new ArrayList<>();
    ShortMedInfoItemAdapter shortmedadapter;
    private ArrayList<String> medNames;
   // ArrayList<ShortMedInfoItem> medicaments;
   private ArrayAdapter<String> adapterMedName;

    Context context = this;

    @BindView(R.id.autoCompletetv_findbyname)
    AutoCompleteTextView autoComTV_findname;

    @BindView(R.id.et_findbycode)
    EditText et_findcode;

    @BindView(R.id.recyclerView_findMed)
    RecyclerView recyclerView_find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_medicine);
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
        shortmedadapter = new ShortMedInfoItemAdapter(meds, listener);
        recyclerView_find.setAdapter(shortmedadapter);
    }

    private void ButtonNumber(String id, int btn_nb) {
        if (btn_nb == 1) {
            //UpdateMedButton(id);
        } else if (btn_nb == 2) {
            //DeleteMedButton(id);
        }
        else if (btn_nb == 3){
            MoreInfoButton(id);
        }
    }

    private void MoreInfoButton(String id) {
        Intent intent = new Intent(FindMedicine.this, ViewAllInfoMedicine.class);
        intent.putExtra("MedId", id);
        startActivity(intent);
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
            recyclerView_find.setAdapter(shortmedadapter);
        }
    }

    private void getMedicamentItem() {
        setDBAdapters();
        dbUserMed.OpenDB();
        Cursor cursor = dbUserMed.GetAllUserMedicamentInfoData();
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
        CreateMedList(cursor, dbUserMed,dbForm,dbPurpose, meds);
    }

    private void setRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView_find.setLayoutManager(layoutManager);
        recyclerView_find.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_find.setItemAnimator(new DefaultItemAnimator());
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
    
    @OnClick(R.id.btn_searchbyname)
    void searchMed() {
        setDBAdapters();
        meds.clear();
        getMedicamentsList(getCursorContent(CheckName()));
        //Cursor cursor;
        //cursor=dbUserMed.FindUserMedicamentByName(autoComTV_findname.getText().toString(), DatabaseConstantInformation.NAME);
        ClearFields();
        //show in recyler view
        ShowMedsList();
       // CreateMedList(cursor, dbUserMed,dbForm,dbPurpose, meds);

    }

    private void ShowMedsList() {

        
    }

    public String[] CheckName(){

        String[] nameOrCode = new String[10];
        int nbArg = 0;
        if (!(autoComTV_findname.getText().toString().matches(""))) {
            nbArg += 1;
            addVal(nameOrCode, autoComTV_findname.getText().toString(), DatabaseConstantInformation.NAME);
        }
        nameOrCode[0] = String.valueOf(nbArg);
        return nameOrCode;
    }

    public String[] addVal(String[] whichOnes, String content, String columnName) {
        for (int i = 1; i < whichOnes.length; i++) {
            if (whichOnes[i] == null) {
                whichOnes[i] = content;
                whichOnes[i + 1] = columnName;
                break;
            }
        }
        return whichOnes;
    }


    public Cursor getCursorContent(String[] byWhich) {

        dbUserMed.OpenDB();
        Cursor cursor;
        if (Integer.valueOf(byWhich[0]) == 1) {
            cursor = dbUserMed.FindUserMedicamentByName(byWhich[1], byWhich[2]);
        } /*else if (Integer.valueOf(byWhich[0]) == 2) {
            cursor = dbUserMed.FindUserMedicamentByCode(byWhich[4], byWhich[3]);}*/
         else {
            cursor = null;
        }
        return cursor;
    }
    private void setDBAdapters() {
        dbUserMed = new DBUserMedicamentsAdapter(this);
        dbMedInfo = new DBMedicamentInfoAdapter(this);
        dbForm = new DBFormAdapter(this);
        dbPurpose = new DBPurposeAdapter( this);
    }
    private void ClearFields() {
        et_findcode.getText().clear();
        autoComTV_findname.getText().clear();
    }

    private void getMedicamentsList(Cursor cursor) {
        CreateMedList(cursor, dbUserMed, dbForm, dbPurpose, meds);
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
                Bitmap image=ConvertByteArrayToImage(cursor);
                dbUserMed.CloseDB();

               // String form = getFormName(dAForm, formInt);

                ShortMedInfoItem shortmed = new ShortMedInfoItem(id, name, expdate, form, purpose,amount, amountform, power, image);
                medicaments.add(shortmed);
                /*if(name==autoComTV_findname.getText().toString())
                {
                    medicaments.add(shortmed);
                }*/

            }
        }
    }

    private Bitmap ConvertByteArrayToImage(Cursor cur){
        byte[] imgByte = cur.getBlob(13);
        return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
    }
}
