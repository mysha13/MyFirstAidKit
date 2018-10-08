package com.example.elasz.myfirstaidkit.DatabaseAdapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import com.example.elasz.myfirstaidkit.DatabaseImplement.DatabaseConstantInformation;
import com.example.elasz.myfirstaidkit.DatabaseImplement.DatabaseHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by elasz on 12.09.2018.
 */

public class DBUserMedicamentsAdapter {

    Context context;
    SQLiteDatabase database;
    DatabaseHelper dbHelper;

    public DBUserMedicamentsAdapter(Context context){
        super();
        this.context=context;
        dbHelper = new DatabaseHelper(context);
    }

    public void OpenDB() throws SQLException {
        database=dbHelper.getWritableDatabase();
    }

    public void CloseDB() throws SQLException{
        dbHelper.close();
    }

    public int GetAllCount() throws SQLException{
        ArrayList<DBUserMedicamentsAdapter> list= new ArrayList<>();
        int count = getNames().getCount();
        return count;
    }
    public long AddUserMedicamentData(String name, int id_medicament, String exp_date, String open_date, int form, int purpose, double amount, int amount_form, String person, String note, boolean istake, byte[] image) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(DatabaseConstantInformation.NAME, name);
            cv.put(DatabaseConstantInformation.ID_MEDICAMENT, id_medicament);
            cv.put(DatabaseConstantInformation.EXPDATE, exp_date);              //check format
            cv.put(DatabaseConstantInformation.OPENDATE, open_date);            //check format
            cv.put(DatabaseConstantInformation.FORM, form);
            cv.put(DatabaseConstantInformation.PURPOSE, purpose);
            cv.put(DatabaseConstantInformation.AMOUNT, amount);
            cv.put(DatabaseConstantInformation.AMOUNT_FORM, amount_form);       //check format
            cv.put(DatabaseConstantInformation.PERSON, person);                 //check format
            cv.put(DatabaseConstantInformation.NOTE, note);
            cv.put(DatabaseConstantInformation.ISTAKEN,istake);
            cv.put(DatabaseConstantInformation.IMAGE, image);                   // check format
            return database.insert(DatabaseConstantInformation.USERMEDICAMENTSTABLE, null, cv);

        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }
    public Cursor GetAllUserMedicamentInfoData(){
        String[] columns= new String[]{DatabaseConstantInformation.ID_USERMED,
                DatabaseConstantInformation.NAME,
                DatabaseConstantInformation.ID_MEDICAMENT,
                DatabaseConstantInformation.EXPDATE,
                DatabaseConstantInformation.OPENDATE,
                DatabaseConstantInformation.FORM,
                DatabaseConstantInformation.PURPOSE,
                DatabaseConstantInformation.AMOUNT,
                DatabaseConstantInformation.AMOUNT_FORM,
                DatabaseConstantInformation.PERSON,
                DatabaseConstantInformation.NOTE,
                DatabaseConstantInformation.ISTAKEN,
                DatabaseConstantInformation.IMAGE};

        return database.query(DatabaseConstantInformation.USERMEDICAMENTSTABLE, columns, null, null, null, null, null);
    }

    public long UpdateRowUserMedInfo(int id, String name, int id_medicament, String exp_date, String open_date, String form, String purpose, double amount, String amount_form, String person, String note, int istake, byte[] image){
        ContentValues cvUpdateRow = new ContentValues();
        cvUpdateRow.put(DatabaseConstantInformation.NAME, name);
        cvUpdateRow.put(DatabaseConstantInformation.ID_MEDICAMENT, id_medicament);
        cvUpdateRow.put(DatabaseConstantInformation.EXPDATE, exp_date);              //check format
        cvUpdateRow.put(DatabaseConstantInformation.OPENDATE, open_date);            //check format
        cvUpdateRow.put(DatabaseConstantInformation.FORM, form);
        cvUpdateRow.put(DatabaseConstantInformation.PURPOSE, purpose);
        cvUpdateRow.put(DatabaseConstantInformation.AMOUNT, amount);
        cvUpdateRow.put(DatabaseConstantInformation.AMOUNT_FORM, amount_form);       //check format
        cvUpdateRow.put(DatabaseConstantInformation.PERSON, person);            //check format
        cvUpdateRow.put(DatabaseConstantInformation.NOTE, note);
        cvUpdateRow.put(DatabaseConstantInformation.ISTAKEN,istake);
        cvUpdateRow.put(DatabaseConstantInformation.IMAGE,image);

        return database.update(DatabaseConstantInformation.USERMEDICAMENTSTABLE, cvUpdateRow, DatabaseConstantInformation.ID_USERMED + "=" + String.valueOf(id), null);
    }

    public String GetColumnContent(String colName, long id){
        String colContent = "";
        Cursor cursor;

        cursor = database.rawQuery("SELECT " + colName + " FROM " + DatabaseConstantInformation.USERMEDICAMENTSTABLE + " WHERE " + DatabaseConstantInformation.ID_USERMED + " = ?", new String[]{String.valueOf(id)});
        cursor = database.query(DatabaseConstantInformation.USERMEDICAMENTSTABLE,
                new String[]{colName},
                DatabaseConstantInformation.ID_USERMED + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            colContent = cursor.getString(cursor.getColumnIndex(colName));
        }
        return colContent;
    }

    public Cursor getNames() {
        return database.query(true, DatabaseConstantInformation.USERMEDICAMENTSTABLE, new String[]{DatabaseConstantInformation.NAME}, null, null, DatabaseConstantInformation.NAME, null, null, null);

    }
    public Cursor FindUserMedicamentByName(String name, String columnName) {

        String[] columns = new String[]{DatabaseConstantInformation.ID_USERMED,
                DatabaseConstantInformation.NAME,
                DatabaseConstantInformation.ID_MEDICAMENT,
                DatabaseConstantInformation.EXPDATE,
                DatabaseConstantInformation.OPENDATE,
                DatabaseConstantInformation.FORM,
                DatabaseConstantInformation.PURPOSE,
                DatabaseConstantInformation.AMOUNT,
                DatabaseConstantInformation.AMOUNT_FORM,
                DatabaseConstantInformation.PERSON,
                DatabaseConstantInformation.NOTE};

        return database.query(DatabaseConstantInformation.USERMEDICAMENTSTABLE,
                columns,
                columnName + "=?" + " COLLATE NOCASE",
                new String[]{name},
                null, null, DatabaseConstantInformation.ID_USERMED);
    }


    public void DeleteUserMedicament(String id) {
        database.delete(DatabaseConstantInformation.USERMEDICAMENTSTABLE, DatabaseConstantInformation.ID_USERMED + "=?", new String[]{id});
    }

    public Cursor FindUserMedicamentByCode(String code, String columnName) {

        String[] columns = new String[]{DatabaseConstantInformation.ID_USERMED,
                DatabaseConstantInformation.MEDNAME,
                DatabaseConstantInformation.POWER,
                DatabaseConstantInformation.ACTIVESUBS,
                DatabaseConstantInformation.CODE,
                DatabaseConstantInformation.PRODUCER};

        return database.query(DatabaseConstantInformation.USERMEDICAMENTSTABLE,
                columns,
                columnName + "=?" + " COLLATE NOCASE",
                new String[]{code}, null, null, DatabaseConstantInformation.CODE);
    }

    public void renameForm(String formId) {
        ContentValues rowUpdate = new ContentValues();
        rowUpdate.put(DatabaseConstantInformation.FORM, String.valueOf(1));
        database.update(DatabaseConstantInformation.USERMEDICAMENTSTABLE, rowUpdate, DatabaseConstantInformation.FORM + "=" + String.valueOf(formId), null);
    }

}
