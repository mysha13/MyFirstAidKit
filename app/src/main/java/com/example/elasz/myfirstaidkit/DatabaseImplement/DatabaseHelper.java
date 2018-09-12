package com.example.elasz.myfirstaidkit.DatabaseImplement;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by elasz on 12.09.2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, DatabaseConstantInformation.DBName, null, DatabaseConstantInformation.DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(DatabaseConstantInformation.CREATE_TABLE_USERMEDICAMENTS);
            db.execSQL(DatabaseConstantInformation.CREATE_TABLE_FORM);
            db.execSQL(DatabaseConstantInformation.CREATE_TABLE_AMOUNTFORM);
            db.execSQL(DatabaseConstantInformation.CREATE_TABLE_PURPOSE);
            db.execSQL(DatabaseConstantInformation.CREATE_TABLE_PERSON);
            db.execSQL(DatabaseConstantInformation.CREATE_TABLE_MEDICAMENTINFO);

            //placeTableStartContent(db);
            formTableStartContent(db);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void formTableStartContent(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        /*cv.put(DatabaseConstants.PLACE_NAME, "brak");
        cv.put(DatabaseConstants.PLACE_DESCRIPTION,"Leki nieprzypisane do żadnego miejsca");
        db.insert(DatabaseConstants.PLACESTABLE,null,cv);
        cv.clear();*/

        String[] forms = {"czopki","inne","kapsułki","maść","pastylki","saszetki","syrop","tabletki", "tabletki musujące", "zawiesina"};

        for(int i=0; i<forms.length;i++) {
            cv.put(DatabaseConstantInformation.FORM_NAME, forms[i]);
            db.insert(DatabaseConstantInformation.FORMTABLE, null, cv);
            cv.clear();
        }

        String[] amount_forms ={"ml" ,"g", "tabletek","opakowań","sztuk"};

        for(int i=0; i<amount_forms.length;i++) {
            cv.put(DatabaseConstantInformation.AMOUNT_FORM_NAME, forms[i]);
            db.insert(DatabaseConstantInformation.AMOUNTFORMTABLE, null, cv);
            cv.clear();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstantInformation.USERMEDICAMENTSTABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstantInformation.FORMTABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstantInformation.AMOUNTFORMTABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstantInformation.PURPOSETABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstantInformation.PERSONTABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstantInformation.MEDICAMENTINFOTABLE);

        onCreate(db);

    }

}
