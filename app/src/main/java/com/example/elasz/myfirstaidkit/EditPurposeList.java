package com.example.elasz.myfirstaidkit;

import android.app.AlertDialog;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBFormAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBMedicamentInfoAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBPurposeAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBUserMedicamentsAdapter;
import com.example.elasz.myfirstaidkit.FormsList.FormsList;
import com.example.elasz.myfirstaidkit.FormsList.FormsListAdapter;
import com.example.elasz.myfirstaidkit.Interfaces.RecyclerViewClickListener;
import com.example.elasz.myfirstaidkit.Medicaments.AllMedInfoItem;
import com.example.elasz.myfirstaidkit.PurposeFormsList.PurposeList;
import com.example.elasz.myfirstaidkit.PurposeFormsList.PurposeListadapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditPurposeList extends AppCompatActivity {



    @BindView(R.id.et_purposename_editpurposelist)
    EditText purposename;

    @BindView(R.id.recyclerView_editpurposelist)
    RecyclerView recyclerView_purposelist;

    RecyclerViewClickListener listener;

    private DBPurposeAdapter dbPurpose;
    private DBUserMedicamentsAdapter dbUserMed;
    private DBMedicamentInfoAdapter dbMedInfo;

    PurposeListadapter purposeListadapter;
    ArrayList<PurposeList> purpose = new ArrayList<>();
    private ArrayList<AllMedInfoItem> meds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_purpose_list);
        ButterKnife.bind(this);
        setRecyclerView();
        retrievePurposes();
        initialize();
    }

    private void setRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView_purposelist.setLayoutManager(layoutManager);
        recyclerView_purposelist.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_purposelist.setItemAnimator(new DefaultItemAnimator());
    }

    private void retrievePurposes() {
        purpose.clear();
        addToPurposeList(purpose);
        if (!(purpose.size() < 1)) {
            recyclerView_purposelist.setAdapter(purposeListadapter);
        }
    }


    public void addToPurposeList(ArrayList<PurposeList> purposeLists) {
        DBPurposeAdapter db = new DBPurposeAdapter(this);
        db.OpenDB();
        Cursor c = db.GetAllPurposes();
        while (c.moveToNext()) {
            int id = c.getInt(0);
            String name = c.getString(1);
            PurposeList purpose = new PurposeList(id, name);
            purposeLists.add(purpose);
        }
        db.CloseDB();
    }

    public void initialize() {
        listener = (view, position, id, bNumber) -> {
            whichButtonWasClicked(id, bNumber);
        };
        purposeListadapter = new PurposeListadapter(purpose, listener);
        recyclerView_purposelist.setAdapter(purposeListadapter);
    }

    private void whichButtonWasClicked(String id, int bNumber) {
        if (bNumber == 1) {
            //if (Integer.valueOf(id) != 1) {
                deletePurposeFromList(id);
           /* } else {
                Toast.makeText(EditPurposeList.this, "Nie można usunąć", Toast.LENGTH_LONG).show();
            }*/
        }
    }

    public void deletePurposeFromList(String id) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_delete, null);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        doNotDelete(mView, dialog);
        deletePurpose(id, mView, dialog);
        dialog.show();
    }


    private void deletePurpose(String id, View mView, AlertDialog dialog) {
        Button bYes = (Button) mView.findViewById(R.id.bDeleteContent);
        bYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePurposeWithId(id);
                retrievePurposes();
                dialog.dismiss();
            }
        });
    }
   /* private void retrievePlace() {
        purpose.clear();
        addToPurposeList(purpose);
        if (!(purpose.size() < 1)) {
            recyclerView_purposelist.setAdapter(purposeListadapter);
        }
    }*/
    public void deletePurposeWithId(String id) {
        dbUserMed = new DBUserMedicamentsAdapter(this);
        dbPurpose = new DBPurposeAdapter(this);
        dbUserMed.OpenDB();
        dbUserMed.renamePurpose(id);
        dbUserMed.CloseDB();

        dbPurpose.OpenDB();
        dbPurpose.deleteOnlyPurpose(id);
        dbPurpose.CloseDB();
    }

    private void doNotDelete(View mView, AlertDialog dialog) {
        Button bNo = (Button) mView.findViewById(R.id.bDoNotDelete);
        bNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.btn_add_editpurposelist)
    void ButtonAddPurpose() {
        if (purposename.getText().toString().matches("")) {
            Toast.makeText(EditPurposeList.this,"Nazwa schorzenia jest pusta", Toast.LENGTH_LONG).show();
        } else if (checkIfExist()){  //ifexist
            addPurpose();
        } else{
            Toast.makeText(EditPurposeList.this,"Podane schorzenie już istnieje", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkIfExist() {
        ArrayList<String> purposeList = createNamesList();
        return checkNameList(purposeList);
    }

    @NonNull
    private ArrayList<String> createNamesList() {
        dbPurpose = new DBPurposeAdapter(this);
        ArrayList<String> purposeList = new ArrayList<>();
        dbPurpose.OpenDB();
        Cursor c = dbPurpose.GetAllPurposes();
        while (c.moveToNext()) {
            String name = c.getString(1);
            purposeList.add(name);
        }
        dbPurpose.CloseDB();
        return purposeList;
    }

    private boolean checkNameList(ArrayList<String> purposeList) {
        for (int i = 0; i < purposeList.size(); i++) {
            if (purposename.getText().toString().matches(purposeList.get(i))) {
                return false;
            }
        }
        return true;
    }

    public void addPurpose() {
        checkAddResult();
        clearField();
        //setRecyclerView();
        retrievePurposes();
        initialize();
    }

    private void checkAddResult() {
        dbPurpose.OpenDB();
        long added = dbPurpose.AddPurpose(purposename.getText().toString());
        dbPurpose.CloseDB();
        if (added > 0) {
            Toast.makeText(EditPurposeList.this, "Schorzenie dodane", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(EditPurposeList.this, "Porażka", Toast.LENGTH_LONG).show();
        }
    }

    private void clearField() {
        purposename.getText().clear();
    }
}
