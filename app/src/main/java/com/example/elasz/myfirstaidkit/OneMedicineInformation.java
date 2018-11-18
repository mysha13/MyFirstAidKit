package com.example.elasz.myfirstaidkit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class OneMedicineInformation extends AppCompatActivity {


    private DBUserMedicamentsAdapter dbUserMed;
    private DBFormAdapter dbForm;
    private DBPurposeAdapter dbPurpose;
    private DBMedicamentInfoAdapter dbMedInfo;
    private DBAmountFormAdapter dbAmountForm;
    private DBPersonAdapter dbPerson;

    ShortMedInfoItemAdapter shortmedadapter;
    ArrayList<ShortMedInfoItem> medicaments;
    Context context = this;

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

   /* @Override
    protected void onRestart(){
        super.onRestart();
        this.onCreate(null);
    }*/

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
        setDBAdapters();
        dbUserMed.OpenDB();
        long nball = dbUserMed.medCount();
        dbUserMed.CloseDB();
        String nbText = Long.toString(nball);
        numberOfMeds.setText(nbText);
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
            numberOfMeds.setText(String.valueOf(medicaments.size()));
        }
        else {
            numberOfMeds.setText("Nie znaleziono zadnych leków");
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

                if(image==null)
                {
                    image=BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_photo_camera_black_24dp);
                }

                ShortMedInfoItem shortmed = new ShortMedInfoItem(id, name, expdate, form, purpose, amount, amountform, power, image, code);
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
        dbPurpose = new DBPurposeAdapter(this);
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
            UpdateMedButton(id);
        } else if (btn_nb == 2) {
            DeleteMedButton(id);
        } else if (btn_nb == 3) {
            MoreInfoButton(id);
        }
    }

    private void UpdateMedButton(String id) {
        Intent intent = new Intent(OneMedicineInformation.this, UpdateMedicine.class);
        Bundle bundle = new Bundle();
        bundle.putInt("MedIdUpdate", Integer.parseInt(id));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void MoreInfoButton(String id) {
        Intent intent = new Intent(OneMedicineInformation.this, ViewAllInfoMedicine.class);
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
