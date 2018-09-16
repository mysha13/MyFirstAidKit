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

public class DBPurposeAdapter {

    ArrayList<String> forms_list = new ArrayList<String>();

    Context context;
    SQLiteDatabase database;
    DatabaseHelper dbHelper;

    public DBPurposeAdapter (Context context){
        this.context =context;
        dbHelper= new DatabaseHelper(context);
    }

    public void OpenDB() throws SQLException {
        database=dbHelper.getWritableDatabase();
    }

    public void CloseDB() throws SQLException{
        dbHelper.close();
    }


    public long AddPurpose(String formName){
        try{
            dbHelper.onCreate(database);
            ContentValues cv = new ContentValues();
            cv.put(DatabaseConstantInformation.PURPOSE_NAME, formName);
            return database.insert(DatabaseConstantInformation.PURPOSETABLE, null, cv);
        }catch (Exception ex){
            ex.printStackTrace();
            return 0;
        }
    }

    public Cursor GetAllPurposes(){
        String[] columns ={DatabaseConstantInformation.ID_PURPOSE, DatabaseConstantInformation.PURPOSE_NAME};
        return database.query(DatabaseConstantInformation.PURPOSETABLE, columns, null, null, null, null,null);
    }

    public String GetPurposeName(long id){
        String formName = "";
        Cursor cursor;
        cursor = database.query(DatabaseConstantInformation.PURPOSETABLE,
                new String[]{DatabaseConstantInformation.PURPOSE_NAME},
                DatabaseConstantInformation.ID_PURPOSE + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            formName = cursor.getString(cursor.getColumnIndex(DatabaseConstantInformation.PURPOSE_NAME));
        }
        return formName;
    }

    public int GetPurposeId(String name){
        int formName = 1;
        Cursor cursor;
        cursor = database.query(DatabaseConstantInformation.PURPOSETABLE,
                new String[]{DatabaseConstantInformation.ID_PURPOSE},
                DatabaseConstantInformation.PURPOSE_NAME + "=?",
                new String[]{name},
                null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            formName = cursor.getInt(cursor.getColumnIndex(DatabaseConstantInformation.ID_PURPOSE));
        }
        return formName;
    }

}
