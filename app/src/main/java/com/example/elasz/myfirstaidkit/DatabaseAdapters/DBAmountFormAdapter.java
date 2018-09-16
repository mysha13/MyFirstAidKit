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

public class DBAmountFormAdapter {

    ArrayList<String> amountForms_list = new ArrayList<String>();

    Context context;
    SQLiteDatabase database;
    DatabaseHelper dbHelper;

    public DBAmountFormAdapter (Context context){
        this.context =context;
        dbHelper= new DatabaseHelper(context);
    }

    public void OpenDB() throws SQLException {
        database=dbHelper.getWritableDatabase();
    }

    public void CloseDB() throws SQLException{
        dbHelper.close();
    }

    public long AddAmountForm(String amountFormName){
        try{
            dbHelper.onCreate(database);
            ContentValues cv = new ContentValues();
            cv.put(DatabaseConstantInformation.AMOUNT_FORM_NAME, amountFormName);
            return database.insert(DatabaseConstantInformation.AMOUNTFORMTABLE, null, cv);
        }catch (Exception ex){
            ex.printStackTrace();
            return 0;
        }
    }

    public Cursor GetAllAmountForms(){
        String[] columns ={DatabaseConstantInformation.ID_AMOUNTFORM, DatabaseConstantInformation.AMOUNT_FORM_NAME};
        return database.query(DatabaseConstantInformation.AMOUNTFORMTABLE, columns, null, null, null, null,null);
    }

    public String GetAmountFormName(long id){
        String formName = "";
        Cursor cursor;
        cursor = database.query(DatabaseConstantInformation.AMOUNTFORMTABLE,
                new String[]{DatabaseConstantInformation.AMOUNT_FORM_NAME},
                DatabaseConstantInformation.ID_AMOUNTFORM + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            formName = cursor.getString(cursor.getColumnIndex(DatabaseConstantInformation.AMOUNT_FORM_NAME));
        }
        return formName;
    }

    public int GetAmountFormId(String name){
        int formName = 1;
        Cursor cursor;
        cursor = database.query(DatabaseConstantInformation.AMOUNTFORMTABLE,
                new String[]{DatabaseConstantInformation.ID_AMOUNTFORM},
                DatabaseConstantInformation.AMOUNT_FORM_NAME + "=?",
                new String[]{name},
                null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            formName = cursor.getInt(cursor.getColumnIndex(DatabaseConstantInformation.ID_AMOUNTFORM));
        }
        return formName;
    }
}
