package com.example.elasz.myfirstaidkit;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBAmountFormAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBFormAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBMedicamentInfoAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBPurposeAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBUserMedicamentsAdapter;
import com.example.elasz.myfirstaidkit.Interfaces.RecyclerViewClickListener;
import com.example.elasz.myfirstaidkit.Medicaments.ShortMedInfoItem;
import com.example.elasz.myfirstaidkit.Medicaments.ShortMedInfoItemAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OneMedicineInformation extends AppCompatActivity {


    private DBUserMedicamentsAdapter dbUserMed;
    private DBFormAdapter dbForm;
    private DBPurposeAdapter dbPurpose;
    private DBMedicamentInfoAdapter dbMedInfo;
    private DBAmountFormAdapter dbAmountForm;

    ShortMedInfoItemAdapter shortmedadapter;
    ArrayList<ShortMedInfoItem> medicaments;
    Context context = this;
    //DBUserMedicamentsAdapter dbUserMed;
    //DBMedicamentInfoAdapter dbMedInfo;
    //DBFormAdapter dbForm;
    //DBPurposeAdapter dbPurpose;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.tv_numberOfMedicaments)
    TextView numberOfMeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_medicine_information);
        ButterKnife.bind(this);
        setRecyclerView();
        getBundle();
        initialize();
    }

    private void setRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void getBundle() {
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            getBundleContent(bundle);
//        } else {
        medicaments = new ArrayList<>();
        numberOfMeds.setText("Wszystkie leki dodane");
        getMed();
        //}
    }

    private void getBundleContent(Bundle bundle) {
        medicaments = (ArrayList<ShortMedInfoItem>) bundle.getSerializable("ListOfMedicines");
        if (!(medicaments.size() < 1)) {
            recyclerView.setAdapter(shortmedadapter);
            numberOfMeds.setText(String.valueOf(medicaments.size()));
        } else {
            numberOfMeds.setText("Nie znaleziono zadnych leków");
        }
    }

    private void getMed() {
        medicaments.clear();
        getMedicamentItem();

        if (!(medicaments.size() < 1)) {
            recyclerView.setAdapter(shortmedadapter);
        }
    }

    private void getMedicamentItem() {
        setDBAdapters();
        dbUserMed.OpenDB();
        Cursor cursor = dbUserMed.GetAllUserMedicamentInfoData();
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
        CreateMedList(cursor, dbUserMed, dbForm, dbPurpose, dbAmountForm, dbMedInfo, medicaments);
    }

    public void CreateMedList(Cursor cursor, DBUserMedicamentsAdapter dbUserMed, DBFormAdapter dbForm, DBPurposeAdapter dbPurpose, DBAmountFormAdapter dbAmountForm, DBMedicamentInfoAdapter dbMedInfo, ArrayList<ShortMedInfoItem> medicaments) {
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String expdate = cursor.getString(2);
                int formid = cursor.getInt(5);
                //String form = cursor.getString(3);

                int purposeid = cursor.getInt(6);
                //String purpose = cursor.getString(6);
                String amount = cursor.getString(7);
                //double amount = cursor.getDouble(5);
                int amountformid = cursor.getInt(8);
               // String amountform = cursor.getString(8);
                int powerid = cursor.getInt(2);

                //String power = cursor.getString(7);
                Bitmap image = ConvertByteArrayToImage(cursor);
                dbUserMed.CloseDB();

                String form = getFormName(dbForm, formid);
                String amountform = getAmountFormName(dbAmountForm, amountformid);
                String power = getPowerName(dbMedInfo, powerid);
                String purpose = getPurposeName(dbPurpose, purposeid);

                ShortMedInfoItem shortmed = new ShortMedInfoItem(id, name, expdate, form, purpose, amount, amountform, power, image);
                medicaments.add(shortmed);

            }
        }
    }

    private Bitmap ConvertByteArrayToImage(Cursor cur) {
        if (cur.getBlob(12)!= null) {
            byte[] imgByte = cur.getBlob(12);
            return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        } else {
            return null;
        }
    }

    private void setDBAdapters() {
        dbUserMed = new DBUserMedicamentsAdapter(this);
        dbMedInfo = new DBMedicamentInfoAdapter(this);
        dbForm = new DBFormAdapter(this);
        dbPurpose = new DBPurposeAdapter(this);
        dbAmountForm = new DBAmountFormAdapter(this);
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
        shortmedadapter = new ShortMedInfoItemAdapter(medicaments, listener);
        recyclerView.setAdapter(shortmedadapter);
    }

    private void ButtonNumber(String id, int btn_nb) {
        if (btn_nb == 1) {
            //UpdateMedButton(id);
        } else if (btn_nb == 2) {
            //DeleteMedButton(id);
        } else if (btn_nb == 3) {
            //MoreInfoButton(id);
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
    public String getPurposeName(DBPurposeAdapter dAForm, int id) {
        dAForm.OpenDB();
        String purpose = dAForm.GetPurposeName(id);
        dAForm.CloseDB();
        return purpose;
    }

}
