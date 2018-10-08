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

import com.example.elasz.myfirstaidkit.AmountFormsList.AmountFormsList;
import com.example.elasz.myfirstaidkit.AmountFormsList.AmountFormsListAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBAmountFormAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBFormAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBMedicamentInfoAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBUserMedicamentsAdapter;
import com.example.elasz.myfirstaidkit.FormsList.FormsList;
import com.example.elasz.myfirstaidkit.FormsList.FormsListAdapter;
import com.example.elasz.myfirstaidkit.Interfaces.RecyclerViewClickListener;
import com.example.elasz.myfirstaidkit.Medicaments.AllMedInfoItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditAmountFormsList extends AppCompatActivity {

    @BindView(R.id.et_amountformsname_editamountformslist)
    EditText amountformname;

    @BindView(R.id.recyclerView_editamountformslist)
    RecyclerView recyclerView_amountformslist;

    RecyclerViewClickListener listener;
    private DBAmountFormAdapter dbAmountForm;
    private DBUserMedicamentsAdapter dbUserMed;
    private DBMedicamentInfoAdapter dbMedInfo;
    AmountFormsListAdapter amountformsListAdapter;
    ArrayList<AmountFormsList> amountforms = new ArrayList<>();
    private ArrayList<AllMedInfoItem> meds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_amount_forms_list);
        ButterKnife.bind(this);
        setRecyclerView();
        retrieveAmountForms();
        inicialize();
    }
    private void setRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView_amountformslist.setLayoutManager(layoutManager);
        recyclerView_amountformslist.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_amountformslist.setItemAnimator(new DefaultItemAnimator());
    }

    private void retrieveAmountForms() {
        amountforms.clear();
        addToAmountFormsList(amountforms);
        if (!(amountforms.size() < 1)) {
            recyclerView_amountformslist.setAdapter(amountformsListAdapter);
        }
    }
    public void addToAmountFormsList(ArrayList<AmountFormsList> formsLists) {
        DBAmountFormAdapter db = new DBAmountFormAdapter(this);
        db.OpenDB();
        Cursor c = db.GetAllAmountForms();
        while (c.moveToNext()) {
            int id = c.getInt(0);
            String name = c.getString(1);
            AmountFormsList form = new AmountFormsList(id, name);
            formsLists.add(form);
        }
        db.CloseDB();
    }
    public void inicialize() {
        listener = (view, position, id, bNumber) -> {
            whichOneWasClicked(id, bNumber);
        };
        amountformsListAdapter = new AmountFormsListAdapter(amountforms, listener);
        recyclerView_amountformslist.setAdapter(amountformsListAdapter);
    }

    private void whichOneWasClicked(String id, int bNumber) {
        if (bNumber == 2) {
            if (Integer.valueOf(id) != 2) {
                deleteAmountFormFromList(id);
            } else {
                Toast.makeText(EditAmountFormsList.this, "Nie można usunąć", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void deleteAmountFormFromList(String id) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_delete, null);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        doNotDelete(mView, dialog);
        deleteForm(id, mView, dialog);
        dialog.show();
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
    private void deleteForm(String id, View mView, AlertDialog dialog) {
        Button bYes = (Button) mView.findViewById(R.id.bDeleteContent);
        bYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dialogDeletePlaceContent(id);
                deleteWithoutContent(id);
                //dialog.dismiss();
                retrievePlace();
                dialog.dismiss();
            }
        });
    }

    public void deleteWithoutContent(String id) {
        dbUserMed = new DBUserMedicamentsAdapter(this);
        dbAmountForm = new DBAmountFormAdapter(this);
        dbUserMed.OpenDB();
        dbUserMed.renameForm(id);
        dbUserMed.CloseDB();

        dbAmountForm.OpenDB();
        dbAmountForm.deleteAmountForm(id);
        dbAmountForm.CloseDB();
    }
    private void retrievePlace() {
        amountforms.clear();
        addToAmountFormsList(amountforms);
        if (!(amountforms.size() < 1)) {
            recyclerView_amountformslist.setAdapter(amountformsListAdapter);
        }
    }

    @OnClick(R.id.btn_add_editamountformslist)
    void ButtonAddPlace() {
        if (amountformname.getText().toString().matches("")) {
            Toast.makeText(EditAmountFormsList.this,"Nazwa formy jest pusta", Toast.LENGTH_LONG).show();
        } else if (checkIfExist()){
            addAmountForm();
        } else{
            Toast.makeText(EditAmountFormsList.this,"Podana forma już istanieje", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkIfExist() {
        ArrayList<String> amountformsList = createNamesList();
        return checkNameList(amountformsList);
    }

    @NonNull
    private ArrayList<String> createNamesList() {
        dbAmountForm = new DBAmountFormAdapter(this);
        ArrayList<String> amountformsList = new ArrayList<>();
        dbAmountForm.OpenDB();
        Cursor c = dbAmountForm.GetAllAmountForms();
        while (c.moveToNext()) {
            String name = c.getString(1);
            amountformsList.add(name);
        }
        dbAmountForm.CloseDB();
        return amountformsList;
    }

    private boolean checkNameList(ArrayList<String> placeList) {
        for (int i = 0; i < placeList.size(); i++) {
            if (amountformname.getText().toString().matches(placeList.get(i))) {
                return false;
            }
        }
        return true;
    }

    public void addAmountForm() {
        CheckAdded();
        ClearField();
        //setRecyclerView();
        retrieveAmountForms();
        inicialize();
    }

    private void CheckAdded() {
        dbAmountForm.OpenDB();
        long added = dbAmountForm.AddAmountForm(amountformname.getText().toString());
        dbAmountForm.CloseDB();
        if (added > 0) {
            Toast.makeText(EditAmountFormsList.this, "Forma dodana", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(EditAmountFormsList.this, "Porażka", Toast.LENGTH_LONG).show();
        }
    }

    private void ClearField() {
        amountformname.getText().clear();
    }

}
