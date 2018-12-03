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

import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBMedicamentInfoAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBPersonAdapter;
import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBUserMedicamentsAdapter;
import com.example.elasz.myfirstaidkit.Interfaces.RecyclerViewClickListener;
import com.example.elasz.myfirstaidkit.Medicaments.AllMedInfoItem;
import com.example.elasz.myfirstaidkit.PersonList.PersonList;
import com.example.elasz.myfirstaidkit.PersonList.PersonListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditPersonList extends AppCompatActivity {


    @BindView(R.id.et_formsname_editpersonlist)
    EditText personname;

    @BindView(R.id.recyclerView_editpersonlist)
    RecyclerView recyclerView_personlist;

    RecyclerViewClickListener listener;
    private DBPersonAdapter dbPerson;
    private DBUserMedicamentsAdapter dbUserMed;
    private DBMedicamentInfoAdapter dbMedInfo;
    PersonListAdapter personListAdapter;
    ArrayList<PersonList> people = new ArrayList<>();
    private ArrayList<AllMedInfoItem> meds = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_person_list);
        ButterKnife.bind(this);
        setRecyclerView();
        retrievePeople();
        initialize();
    }

    private void setRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView_personlist.setLayoutManager(layoutManager);
        recyclerView_personlist.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_personlist.setItemAnimator(new DefaultItemAnimator());
    }

    private void retrievePeople() {
        people.clear();
        addToPersonList(people);
        if (!(people.size() < 1)) {
            recyclerView_personlist.setAdapter(personListAdapter);
        }
    }

    public void addToPersonList(ArrayList<PersonList> personLists) {
        DBPersonAdapter db = new DBPersonAdapter(this);
        db.OpenDB();
        Cursor c = db.GetAllPeople();
        while (c.moveToNext()) {
            int id = c.getInt(0);
            String name = c.getString(1);
            PersonList person = new PersonList(id, name);
            personLists.add(person);
        }
        db.CloseDB();
    }

    public void initialize() {
        listener = (view, position, id, bNumber) -> {
            whichButtonWasClicked(id, bNumber);
        };
        personListAdapter = new PersonListAdapter(people, listener);
        recyclerView_personlist.setAdapter(personListAdapter);
    }

    private void whichButtonWasClicked(String id, int bNumber) {
        if (bNumber == 2) {
            deletePersonFromList(id);

           /* if (Integer.valueOf(id) != 2) {
                deletePersonFromList(id);
            } else {
                Toast.makeText(EditPersonList.this, "Nie można usunąć, osoba przypisana", Toast.LENGTH_LONG).show();
            }*/
        }
    }

    public void deletePersonFromList(String id) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_delete, null);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        doNotDelete(mView, dialog);
        deletePerson(id, mView, dialog);
        dialog.show();
    }

    private void deletePerson(String id, View mView, AlertDialog dialog) {
        Button bYes = (Button) mView.findViewById(R.id.bDeleteContent);
        bYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dialogDeletePlaceContent(id);
                deletePersonWithId(id);
                //dialog.dismiss();
                retrievePeople();
                dialog.dismiss();
            }
        });
    }

  /*  private void retrievePlace() {
        people.clear();
        addToPersonList(people);
        if (!(people.size() < 1)) {
            recyclerView_personlist.setAdapter(personListAdapter);
        }
    }*/

    public void deletePersonWithId(String id) {
        dbUserMed = new DBUserMedicamentsAdapter(this);
        dbPerson = new DBPersonAdapter(this);
        dbUserMed.OpenDB();
        dbUserMed.renamePerson(id);
        dbUserMed.CloseDB();

        dbPerson.OpenDB();
        dbPerson.deleteOnlyPerson(id);
        dbPerson.CloseDB();
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


    @OnClick(R.id.btn_add_editpersonlist)
    void ButtonAddPerson() {
        if (personname.getText().toString().matches("")) {
            Toast.makeText(EditPersonList.this,"Nazwa formy jest pusta", Toast.LENGTH_LONG).show();
        } else if (checkIfExist()){  //ifexist
            addPerson();
        } else{
            Toast.makeText(EditPersonList.this,"Podana forma już istanieje", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkIfExist() {
        ArrayList<String> personList = createNamesList();
        return checkNameList(personList);
    }

    @NonNull
    private ArrayList<String> createNamesList() {
        dbPerson = new DBPersonAdapter(this);
        ArrayList<String> personList = new ArrayList<>();
        dbPerson.OpenDB();
        Cursor c = dbPerson.GetAllPeople();
        while (c.moveToNext()) {
            String name = c.getString(1);
            personList.add(name);
        }
        dbPerson.CloseDB();
        return personList;
    }

    private boolean checkNameList(ArrayList<String> peopleList) {
        for (int i = 0; i < peopleList.size(); i++) {
            if (personname.getText().toString().matches(peopleList.get(i))) {
                return false;
            }
        }
        return true;
    }

    public void addPerson() {
        checkAddResult();
        clearField();
        //setRecyclerView();
        retrievePeople();
        initialize();
    }

    private void checkAddResult() {
        dbPerson.OpenDB();
        long added = dbPerson.AddPerson(personname.getText().toString());
        dbPerson.CloseDB();
        if (added > 0) {
            Toast.makeText(EditPersonList.this, "Forma dodana", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(EditPersonList.this, "Porażka", Toast.LENGTH_LONG).show();
        }
    }

    private void clearField() {
        personname.getText().clear();
    }

}

