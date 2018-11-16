package com.example.elasz.myfirstaidkit;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBAmountFormAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBFormAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBMedicamentInfoAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBPersonAdapter;
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
    private DBAmountFormAdapter dbAmountForm;
    private DBPersonAdapter dbPerson;

    private ArrayList<String> medicamentsName;
    private ArrayAdapter<String> adapterMedicamentsName;

    private ArrayList<ShortMedInfoItem> meds = new ArrayList<>();
    ShortMedInfoItemAdapter shortmedadapter;
    private ArrayList<String> medNames;
   // ArrayList<ShortMedInfoItem> medicaments;
    private ArrayAdapter<String> adapterMedName;
    public static int codefromscanner;
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
        setDBAdapters();
        setRecyclerView();

        autoCompleteFindByName();
        getBundle();
        initialize();


        autoComTV_findname.addTextChangedListener(mQueryWatcher);

        et_findcode.addTextChangedListener(mQueryWatcher1);

    }
    private TextWatcher mQueryWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            shortmedadapter.filter(s.toString());
        }
    };

    private TextWatcher mQueryWatcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            shortmedadapter.filterCode(s.toString());
        }
    };

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
            UpdateMedButton(id);
        } else if (btn_nb == 2) {
            DeleteMedButton(id);
        }
        else if (btn_nb == 3){
            MoreInfoButton(id);
        }
    }

    private void UpdateMedButton(String id) {
        Intent intent = new Intent(FindMedicine.this, UpdateMedicine.class);
        Bundle bundle = new Bundle();
        bundle.putInt("MedIdUpdate", Integer.parseInt(id));
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private void MoreInfoButton(String id) {
        Intent intent = new Intent(FindMedicine.this, ViewAllInfoMedicine.class);
        Bundle bundle = new Bundle();
        bundle.putInt("MedId", Integer.parseInt(id));
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private void DeleteMedButton(String id) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_delete, null);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        Button bNo = (Button) mView.findViewById(R.id.bDoNotDelete);
        bNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        Button bYes = (Button) mView.findViewById(R.id.bDeleteContent);
        bYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMed(id);
                dialog.dismiss();
                getMed();
            }
        });
        dialog.show();

    }
    private void deleteMed(String id) {
        DBUserMedicamentsAdapter dbMed = new DBUserMedicamentsAdapter(getBaseContext());
        dbMed.OpenDB();
        dbMed.deleteMed(String.valueOf(id));
        dbMed.CloseDB();
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
        //setDBAdapters();
        dbUserMed.OpenDB();
        Cursor cursor = dbUserMed.GetAllUserMedicamentInfoData();
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
        CreateMedList(cursor, dbUserMed, dbForm, dbPurpose, dbAmountForm, dbMedInfo, meds);
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

    @OnClick(R.id.btn_scanBarcode_find)
    void searchByCode(){
        Intent intent= new Intent(this, BarcodeScanner.class);
        startActivityForResult(intent, codefromscanner);
        BarcodeScanner bar= new BarcodeScanner();
        et_findcode.setText(bar.getCode().toString());
    }

    /*@OnClick(R.id.btn_searchbyname)
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

    }*/

    private void ShowMedsList() {

        
    }

    public String[] CheckName(){

        String[] nameOrCode = new String[10];
        int nbArg = 0;
        if (!(autoComTV_findname.getText().toString().matches(""))) {
            nbArg += 1;
            addVal(nameOrCode, autoComTV_findname.getText().toString(), DatabaseConstantInformation.NAME);
        } else if (!(et_findcode.getText().toString().matches(""))){
            nbArg +=1;
            addVal(nameOrCode, et_findcode.getText().toString(), DatabaseConstantInformation.CODE);
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
        } //else if (Integer.valueOf(byWhich[0]) == 2) {
            //cursor = dbUserMed.FindUserMedicamentByCode(byWhich[4], byWhich[3]);}
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
        dbAmountForm = new DBAmountFormAdapter(this);
        dbPerson = new DBPersonAdapter(this);
    }
    private void ClearFields() {
        et_findcode.getText().clear();
        autoComTV_findname.getText().clear();
    }

    private void getMedicamentsList(Cursor cursor) {
        CreateMedList(cursor, dbUserMed, dbForm, dbPurpose, dbAmountForm, dbMedInfo, meds);
    }

    public void CreateMedList(Cursor cursor, DBUserMedicamentsAdapter dbUserMed,DBFormAdapter dbForm,DBPurposeAdapter dbPurpose,DBAmountFormAdapter dbAmountForm,DBMedicamentInfoAdapter dbMedInfo ,  ArrayList<ShortMedInfoItem> medicaments) {
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String expdate = cursor.getString(3);
                int formid = cursor.getInt(5);
                //String form = cursor.getString(3);

                int purposeid = cursor.getInt(6);
                //String purpose = cursor.getString(6);
                String amount = cursor.getString(7);
                //double amount = cursor.getDouble(5);
                int amountformid = cursor.getInt(8);
                // String amountform = cursor.getString(8);
                int idmedinfo = cursor.getInt(2);

                //String power = cursor.getString(7);
                Bitmap image = ConvertByteArrayToImage(cursor);
                dbUserMed.CloseDB();

                String form = getFormName(dbForm, formid);
                String amountform = getAmountFormName(dbAmountForm, amountformid);
                String power = getPowerName(dbMedInfo, idmedinfo);
                String purpose = getPurposeName(dbPurpose, purposeid);
                String code = getCode(dbMedInfo, idmedinfo);

                ShortMedInfoItem shortmed = new ShortMedInfoItem(id, name, expdate, form, purpose, amount, amountform, power, image, code);
                medicaments.add(shortmed);

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
    }

    public String getFormName(DBFormAdapter dbFormAdapter, int id){
        dbFormAdapter.OpenDB();
        String form= dbFormAdapter.GetFormName(id);
        dbFormAdapter.CloseDB();
        return form;
    }

    public String getAmountFormName(DBAmountFormAdapter dAForm, int id) {
        dAForm.OpenDB();
        String amountform = dAForm.GetAmountFormName(id);
        dAForm.CloseDB();
        return amountform;
    }

    public String getPowerName(DBMedicamentInfoAdapter dAForm, int id) {
        dAForm.OpenDB();
        String power = dAForm.GetPower(id);
        dAForm.CloseDB();
        return power;
    }

    public String getCode(DBMedicamentInfoAdapter dAForm, int id) {
        dAForm.OpenDB();
        String power = dAForm.GetCode(id);
        dAForm.CloseDB();
        return power;
    }

    public String getPurposeName(DBPurposeAdapter dAForm, int id) {
        dAForm.OpenDB();
        String purpose = dAForm.GetPurposeName(id);
        dAForm.CloseDB();
        return purpose;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == codefromscanner) {
            if (resultCode == Activity.RESULT_OK) {
                BarcodeScanner bar = new BarcodeScanner();
                String newText = data.getStringExtra(bar.PUBLIC_STATIC_STRING_IDENTIFIER);
                et_findcode.setText(newText);
            }
        }
    }
}
