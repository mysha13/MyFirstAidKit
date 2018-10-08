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
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBUserMedicamentsAdapter;
import com.example.elasz.myfirstaidkit.FormsList.FormsList;
import com.example.elasz.myfirstaidkit.FormsList.FormsListAdapter;
import com.example.elasz.myfirstaidkit.Interfaces.RecyclerViewClickListener;
import com.example.elasz.myfirstaidkit.Medicaments.AllMedInfoItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditFormsList extends AppCompatActivity {

    //DBFormAdapter dbForm;

    @BindView(R.id.et_formsname_editformslist)
    EditText formname;

    @BindView(R.id.recyclerView_editformslist)
    RecyclerView recyclerView_formslist;

    RecyclerViewClickListener listener;
    private DBFormAdapter dbForm;
    private DBUserMedicamentsAdapter dbUserMed;
    private DBMedicamentInfoAdapter dbMedInfo;
    FormsListAdapter formsListAdapter;
    ArrayList<FormsList> forms = new ArrayList<>();
    private ArrayList<AllMedInfoItem> meds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_forms_list);
        ButterKnife.bind(this);
        setRecyclerView();
        retrieveForms();
        inicialize();
    }

    private void setRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView_formslist.setLayoutManager(layoutManager);
        recyclerView_formslist.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_formslist.setItemAnimator(new DefaultItemAnimator());
    }

    private void retrieveForms() {
        forms.clear();
        addToFormsList(forms);
        if (!(forms.size() < 1)) {
            recyclerView_formslist.setAdapter(formsListAdapter);
        }
    }

    public void addToFormsList(ArrayList<FormsList> formsLists) {
        DBFormAdapter db = new DBFormAdapter(this);
        db.OpenDB();
        Cursor c = db.GetAllForms();
        while (c.moveToNext()) {
            int id = c.getInt(0);
            String name = c.getString(1);
            FormsList form = new FormsList(id, name);
            formsLists.add(form);
        }
        db.CloseDB();
    }

    public void inicialize() {
        listener = (view, position, id, bNumber) -> {
            whichOneWasClicked(id, bNumber);
        };
        formsListAdapter = new FormsListAdapter(forms, listener);
        recyclerView_formslist.setAdapter(formsListAdapter);
    }

    private void whichOneWasClicked(String id, int bNumber) {
        if (bNumber == 2) {
            if (Integer.valueOf(id) != 2) {
                deleteFormFromList(id);
            } else {
                Toast.makeText(EditFormsList.this, "Nie można usunąć", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void deleteFormFromList(String id) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_delete, null);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        doNotDelete(mView, dialog);
        deleteForm(id, mView, dialog);
        dialog.show();
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
    /*public void dialogDeletePlaceContent(String id) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        View mView = getLayoutInflater().inflate(R.layout.place_dialog, null);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        changeFormToNone(id, mView, dialog);
       // deletePlaceWithItsContent(id, mView, dialog);
        dialog.show();
    }
    private void changeFormToNone(String id, View mView, AlertDialog dialog) {
        Button bNoReplece = (Button) mView.findViewById(R.id.bReplaceContentToNone);
        bNoReplece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteWithoutContent(id);
                dialog.dismiss();
                retrievePlace();

            }
        });
    }*/

    private void retrievePlace() {
        forms.clear();
        addToFormsList(forms);
        if (!(forms.size() < 1)) {
            recyclerView_formslist.setAdapter(formsListAdapter);
        }
    }
    public void deleteWithoutContent(String id) {
        dbUserMed = new DBUserMedicamentsAdapter(this);
        dbForm = new DBFormAdapter(this);
        dbUserMed.OpenDB();
        dbUserMed.renameForm(id);
        dbUserMed.CloseDB();

        dbForm.OpenDB();
        dbForm.deleteOnlyForm(id);
        dbForm.CloseDB();
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

    @OnClick(R.id.btn_add_editformslist)
    void ButtonAddPlace() {
        if (formname.getText().toString().matches("")) {
            Toast.makeText(EditFormsList.this,"Nazwa formy jest pusta", Toast.LENGTH_LONG).show();
        } else if (checkIfExist()){  //ifexist
            addForm();
        } else{
            Toast.makeText(EditFormsList.this,"Podana forma już istanieje", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkIfExist() {
        ArrayList<String> formsList = createNamesList();
        return checkNameList(formsList);
    }

    @NonNull
    private ArrayList<String> createNamesList() {
        dbForm = new DBFormAdapter(this);
        ArrayList<String> formsList = new ArrayList<>();
        dbForm.OpenDB();
        Cursor c = dbForm.GetAllForms();
        while (c.moveToNext()) {
            String name = c.getString(1);
            formsList.add(name);
        }
        dbForm.CloseDB();
        return formsList;
    }

    private boolean checkNameList(ArrayList<String> placeList) {
        for (int i = 0; i < placeList.size(); i++) {
            if (formname.getText().toString().matches(placeList.get(i))) {
                return false;
            }
        }
        return true;
    }

    public void addForm() {
        CheckAdded();
        ClearField();
        //setRecyclerView();
        retrieveForms();
        inicialize();
    }

   private void CheckAdded() {
       dbForm.OpenDB();
       long added = dbForm.AddForm(formname.getText().toString());
       dbForm.CloseDB();
       if (added > 0) {
           Toast.makeText(EditFormsList.this, "Forma dodana", Toast.LENGTH_LONG).show();
       } else {
           Toast.makeText(EditFormsList.this, "Porażka", Toast.LENGTH_LONG).show();
       }
   }

    private void ClearField() {
        formname.getText().clear();
    }
}
