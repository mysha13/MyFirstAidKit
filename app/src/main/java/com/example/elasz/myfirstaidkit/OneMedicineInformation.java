package com.example.elasz.myfirstaidkit;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

    ShortMedInfoItemAdapter shortmedadapter;
    ArrayList<ShortMedInfoItem> medicaments;
    Context context = this;
    DBUserMedicamentsAdapter dbUserMed;
    DBMedicamentInfoAdapter dbMedInfo;
    DBFormAdapter dbForm;
    DBPurposeAdapter dbPurpose;

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
    }

    private void setDBAdapters() {
        dbUserMed = new DBUserMedicamentsAdapter(this);
        dbMedInfo = new DBMedicamentInfoAdapter(this);
        dbForm = new DBFormAdapter(this);
        dbPurpose = new DBPurposeAdapter( this);
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
        }
        else if (btn_nb == 3){
            //MoreInfoButton(id);
        }
    }



}
