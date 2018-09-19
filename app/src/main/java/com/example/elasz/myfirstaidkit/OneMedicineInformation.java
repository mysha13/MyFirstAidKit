package com.example.elasz.myfirstaidkit;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
        setRecycler();
        retriveBundle();
        inicialize();

    }

    private void setRecycler() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void retriveBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            getBundleContent(bundle);
        } else {
            medicaments = new ArrayList<>();
            numberOfMeds.setText("Wszystkie leki dodane");
            getMed();
        }
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
        getMedObject();

        if (!(medicaments.size() < 1)) {
            recyclerView.setAdapter(shortmedadapter);
        }
    }

    private void getMedObject() {
        setDatabaseAdapters();
        dbUserMed.OpenDB();
        Cursor cursor = dbUserMed.GetAllUserMedicamentInfoData();
        //addMedToList(cursor,dAMed,dAForm,dAPlace,meds);
    }
    private void setDatabaseAdapters() {
        dbUserMed = new DBUserMedicamentsAdapter(this);
        dbMedInfo = new DBMedicamentInfoAdapter(this);
        dbForm = new DBFormAdapter(this);
        dbPurpose = new DBPurposeAdapter( this);
    }

    public void inicialize() {
        /*RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position, String id, int bNumber) {
                OneMedicineInformation.this.ButtonNumber(id, bNumber);
            }
        };*/
        RecyclerViewClickListener listener = (view, position, id, bNumber) -> {
            ButtonNumber(id, bNumber);
        };
        shortmedadapter = new ShortMedInfoItem(medicaments, listener);
        recyclerView.setAdapter(shortmedadapter);
    }

    private void ButtonNumber(String id, int bNumber) {
        if (bNumber == 1) {
            UpdateMedButton(id);
        } else if (bNumber == 2) {
            //DeleteMedButton(id);
        }
        else if (bNumber == 3){
            MoreInfoButton(id);
        }
    }

    private void MoreInfoButton(String id) {
        Intent intent = new Intent(OneMedicineInformation.this, ViewAllInfoMedicine.class);
        Bundle bundle = new Bundle();
        bundle.putInt("Id", Integer.parseInt(id));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void UpdateMedButton(String id) {

        Intent intent = new Intent(OneMedicineInformation.this, UpdateMedicine.class);
        Bundle bundle = new Bundle();
        bundle.putInt("Id", Integer.parseInt(id));
        intent.putExtras(bundle);
        startActivity(intent);

    }

   /* public void DeleteMedButton(String id) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
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
                retrieveMed();
            }
        });
        dialog.show();

    }*/

    /*private void deleteMed(String id) {
        DatabaseMedAdapter dbMed = new DatabaseMedAdapter(getBaseContext());
        dbMed.openDB();
        dbMed.deleteMed(String.valueOf(id));
        dbMed.closeDB();

    }*/
}
