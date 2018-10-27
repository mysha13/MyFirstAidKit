package com.example.elasz.myfirstaidkit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBAmountFormAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBFormAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBMedicamentInfoAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBPersonAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBPurposeAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBUserMedicamentsAdapter;
import com.example.elasz.myfirstaidkit.Interfaces.RecyclerViewClickListener;
import com.example.elasz.myfirstaidkit.Medicaments.ShortMedInfoItem;
import com.example.elasz.myfirstaidkit.Medicaments.ShortMedInfoItemAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListView_InformationList extends AppCompatActivity {

    private DBUserMedicamentsAdapter dbUserMed;
    private DBFormAdapter dbForm;
    private DBPurposeAdapter dbPurpose;
    private DBMedicamentInfoAdapter dbMedInfo;
    private DBAmountFormAdapter dbAmountForm;
    private DBPersonAdapter dbPerson;

    ShortMedInfoItemAdapter shortmedadapter;
    ArrayList<ShortMedInfoItem> medicamentsNear;
    ArrayList<ShortMedInfoItem> medicamentsOver;
    Context context = this;

    @BindView(R.id.recView_nearOverdueMed)
    RecyclerView recView_near;

    @BindView(R.id.tv_numberNearOverdueMeds)
    TextView nbNear;

    @BindView(R.id.recView_overdueMed)
    RecyclerView recView_over;

    @BindView(R.id.tv_numberOverdueMeds)
    TextView nbOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view__information_list);
        ButterKnife.bind(this);
        setRecyclerViews();
        medicamentsNear = new ArrayList<>();
        //medicamentsOver = new ArrayList<>();
        getMed();
        initialize();
    }


    @OnClick(R.id.btn_dropdown_nearMed)
    void openNearMedsList(){
        if(recView_near.getVisibility()==View.GONE){
            recView_near.setVisibility(View.VISIBLE);
        } else if (recView_near.getVisibility()==View.VISIBLE){
            recView_near.setVisibility(View.GONE);
        }
    }

    private void setDBAdapters() {
        dbUserMed = new DBUserMedicamentsAdapter(this);
        dbMedInfo = new DBMedicamentInfoAdapter(this);
        dbForm = new DBFormAdapter(this);
        dbPurpose = new DBPurposeAdapter( this);
        dbAmountForm = new DBAmountFormAdapter(this);
        dbPerson = new DBPersonAdapter(this);
    }

    public void initialize() {

        RecyclerViewClickListener listener = (view, position, id, bNumber) -> {
            ButtonNumber(id, bNumber);
        };
        shortmedadapter = new ShortMedInfoItemAdapter(medicamentsNear, listener);
        recView_near.setAdapter(shortmedadapter);
    }

    private void ButtonNumber(String id, int btn_nb) {
        if (btn_nb == 1) {
            UpdateMedButton(id);
        } else if (btn_nb == 2) {
            DeleteMedButton(id);
        } else if (btn_nb == 3) {
            MoreInfoButton(id);
        }
    }

    private void UpdateMedButton(String id) {
        Intent intent = new Intent(ListView_InformationList.this, UpdateMedicine.class);
        Bundle bundle = new Bundle();
        bundle.putInt("MedIdUpdate", Integer.parseInt(id));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void MoreInfoButton(String id) {
        Intent intent = new Intent(ListView_InformationList.this, ViewAllInfoMedicine.class);
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

    private void setRecyclerViews() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recView_near.setLayoutManager(layoutManager);
        recView_near.setLayoutManager(new LinearLayoutManager(this));
        recView_near.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(context);
        recView_over.setLayoutManager(layoutManager1);
        recView_over.setLayoutManager(new LinearLayoutManager(this));
        recView_over.setItemAnimator(new DefaultItemAnimator());


    }

    private void getBundle() {
        medicamentsNear = new ArrayList<>();
        //numberOfMeds.setText("Wszystkie leki dodane");
        getMed();
    }

    private void getMed() {
        medicamentsNear.clear();
        getMedicamentItem();

        if (!(medicamentsNear.size() < 1)) {
            recView_near.setAdapter(shortmedadapter);
        }
    }

    private void getMedicamentItem() {
        setDBAdapters();
        dbUserMed.OpenDB();
        Cursor cursor = dbUserMed.GetAllUserMedicamentInfoData();
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
        CreateMedList(cursor, dbUserMed, dbForm, dbPurpose, dbAmountForm, dbMedInfo, medicamentsNear);
    }

    public void CreateMedList(Cursor cursor, DBUserMedicamentsAdapter dbUserMed, DBFormAdapter dbForm, DBPurposeAdapter dbPurpose, DBAmountFormAdapter dbAmountForm, DBMedicamentInfoAdapter dbMedInfo, ArrayList<ShortMedInfoItem> medicaments) {
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

    public String getPurposeName(DBPurposeAdapter dAForm, int id) {
        dAForm.OpenDB();
        String purpose = dAForm.GetPurposeName(id);
        dAForm.CloseDB();
        return purpose;
    }

    public String getCode(DBMedicamentInfoAdapter dAForm, int id) {
        dAForm.OpenDB();
        String power = dAForm.GetCode(id);
        dAForm.CloseDB();
        return power;
    }

}
