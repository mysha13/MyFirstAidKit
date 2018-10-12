package com.example.elasz.myfirstaidkit.DatabaseAdapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.elasz.myfirstaidkit.DatabaseImplement.DatabaseConstantInformation;
import com.example.elasz.myfirstaidkit.DatabaseImplement.DatabaseHelper;

import java.util.ArrayList;

/**
 * Created by elasz on 12.09.2018.
 */

public class DBFormAdapter {

    ArrayList<String> forms_list = new ArrayList<String>();

    Context context;
    SQLiteDatabase database;
    DatabaseHelper dbHelper;

    public DBFormAdapter (Context context){
        this.context =context;
        dbHelper= new DatabaseHelper(context);
    }

    public void OpenDB() throws SQLException{
     database=dbHelper.getWritableDatabase();
    }

    public void CloseDB() throws SQLException{
        dbHelper.close();
    }


    public long AddForm(String formName){
        try{
            dbHelper.onCreate(database);
            ContentValues cv = new ContentValues();
            cv.put(DatabaseConstantInformation.FORM_NAME, formName);
            return database.insert(DatabaseConstantInformation.FORMTABLE, null, cv);
        }catch (Exception ex){
            ex.printStackTrace();
            return 0;
        }
    }

    public Cursor GetAllForms(){
        String[] columns ={DatabaseConstantInformation.ID_FORM, DatabaseConstantInformation.FORM_NAME};
        return database.query(DatabaseConstantInformation.FORMTABLE, columns, null, null, null, null,null);
    }

    public String GetFormName(long id){
        String formName = "";
        Cursor cursor;
        cursor = database.query(DatabaseConstantInformation.FORMTABLE,
                new String[]{DatabaseConstantInformation.FORM_NAME},
                DatabaseConstantInformation.ID_FORM + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            formName = cursor.getString(cursor.getColumnIndex(DatabaseConstantInformation.FORM_NAME));
        }
        return formName;
    }

    public int GetFormId(String name){
        int formName = 1;
        Cursor cursor;
        cursor = database.query(DatabaseConstantInformation.FORMTABLE,
                new String[]{DatabaseConstantInformation.ID_FORM},
                DatabaseConstantInformation.FORM_NAME + "=?",
                new String[]{name},
                null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            formName = cursor.getInt(cursor.getColumnIndex(DatabaseConstantInformation.ID_FORM));
        }
        return formName;
    }

    public void deleteOnlyForm(String id){
        database.delete(DatabaseConstantInformation.FORMTABLE,DatabaseConstantInformation.ID_FORM +"=?", new String[]{id});
    }


}
