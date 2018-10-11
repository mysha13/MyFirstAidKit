package com.example.elasz.myfirstaidkit.DatabaseImplement;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.elasz.myfirstaidkit.DatabaseAdapters.DBFormAdapter;

import java.sql.DatabaseMetaData;

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

            formTableStartContent(db);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void formTableStartContent(SQLiteDatabase dbs) {
        String[] forms = {"czopki","inne","kapsułki","maść","pastylki","saszetki","syrop","tabletki", "tabletki musujące", "zawiesina"};

        ContentValues cv = new ContentValues();
        String name=DatabaseConstantInformation.FORM_NAME;

        for(int i=0; i<forms.length;i++) {
            cv.put(name, forms[i]);
            dbs.insertOrThrow(DatabaseConstantInformation.FORMTABLE, null, cv);
            cv.clear();
        }
        ContentValues cv1 = new ContentValues();
        String[] amount_forms ={"ml" ,"g", "tabletek","opakowań","sztuk"};

        for(int i=0; i<amount_forms.length;i++) {
            cv1.put(DatabaseConstantInformation.AMOUNT_FORM_NAME, amount_forms[i]);
            dbs.insert(DatabaseConstantInformation.AMOUNTFORMTABLE, null, cv1);
            cv1.clear();
        }
        ContentValues cv2 = new ContentValues();
        String[] purposes = {"alergia","katar","kaszel/gardło", "gorączka","przeciwbólwo", "witaminy", "od specjalisty", "inne"};
        for(int i=0; i<purposes.length;i++) {
            cv2.put(DatabaseConstantInformation.PURPOSE_NAME, purposes[i]);
            dbs.insert(DatabaseConstantInformation.PURPOSETABLE, null, cv2);
            cv2.clear();
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
