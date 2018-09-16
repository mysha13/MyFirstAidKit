package com.example.elasz.myfirstaidkit.DatabaseAdapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.elasz.myfirstaidkit.DatabaseImplement.DatabaseConstantInformation;
import com.example.elasz.myfirstaidkit.DatabaseImplement.DatabaseHelper;

import java.util.ArrayList;

/**
 * Created by elasz on 12.09.2018.
 */

public class DBPersonAdapter {


    ArrayList<String> forms_list = new ArrayList<String>();

    Context context;
    SQLiteDatabase database;
    DatabaseHelper dbHelper;

    public DBPersonAdapter (Context context){
        this.context =context;
        dbHelper= new DatabaseHelper(context);
    }

    public void OpenDB() throws SQLException {
        database=dbHelper.getWritableDatabase();
    }

    public void CloseDB() throws SQLException{
        dbHelper.close();
    }

    public long AddPerson(String formName){
        try{
            dbHelper.onCreate(database);
            ContentValues cv = new ContentValues();
            cv.put(DatabaseConstantInformation.PERSON_NAME, formName);
            return database.insert(DatabaseConstantInformation.PERSONTABLE, null, cv);
        }catch (Exception ex){
            ex.printStackTrace();
            return 0;
        }
    }

    public Cursor GetAllPeople(){
        String[] columns ={DatabaseConstantInformation.ID_PERSON, DatabaseConstantInformation.PERSON_NAME};
        return database.query(DatabaseConstantInformation.PERSONTABLE, columns, null, null, null, null,null);
    }

    public String GetPersonName(long id){
        String formName = "";
        Cursor cursor;
        cursor = database.query(DatabaseConstantInformation.PERSONTABLE,
                new String[]{DatabaseConstantInformation.PERSON_NAME},
                DatabaseConstantInformation.ID_PERSON + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            formName = cursor.getString(cursor.getColumnIndex(DatabaseConstantInformation.PERSON_NAME));
        }
        return formName;
    }

    public int GetPersonId(String name){
        int formName = 1;
        Cursor cursor;
        cursor = database.query(DatabaseConstantInformation.PERSONTABLE,
                new String[]{DatabaseConstantInformation.ID_PERSON},
                DatabaseConstantInformation.PERSON_NAME + "=?",
                new String[]{name},
                null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            formName = cursor.getInt(cursor.getColumnIndex(DatabaseConstantInformation.ID_PERSON));
        }
        return formName;
    }

}
