package com.example.elasz.myfirstaidkit.DatabaseAdapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.elasz.myfirstaidkit.DatabaseImplement.DatabaseConstantInformation;
import com.example.elasz.myfirstaidkit.DatabaseImplement.DatabaseHelper;

/**
 * Created by elasz on 12.09.2018.
 */

public class DBMedicamentInfoAdapter {

    Context context;
    SQLiteDatabase database;
    DatabaseHelper dbHelper;

    public DBMedicamentInfoAdapter(Context context){
       // super();
        this.context=context;
        dbHelper = new DatabaseHelper(context);
    }

    public void OpenDB() throws SQLException {
        database=dbHelper.getWritableDatabase();
    }

    public void CloseDB() throws SQLException{
        dbHelper.close();
    }

    public long AddMedicamentInfoData(String name, String power, String activeSubs, String code, String producer){
        try{
            ContentValues cv = new ContentValues();
            cv.put(DatabaseConstantInformation.MEDNAME, name);
            cv.put(DatabaseConstantInformation.POWER, power);
            cv.put(DatabaseConstantInformation.ACTIVESUBS, activeSubs);
            cv.put(DatabaseConstantInformation.CODE, code);
            cv.put(DatabaseConstantInformation.PRODUCER, producer);
            return database.insert(DatabaseConstantInformation.MEDICAMENTINFOTABLE, null, cv);

        }catch (SQLException  ex){
            ex.printStackTrace();
            return 0;
        }
    }

    public Cursor GetAllMedicamentInfoData(){
        String[] columns= new String[]{DatabaseConstantInformation.ID_MEDICAMENT,
        DatabaseConstantInformation.MEDNAME,
        DatabaseConstantInformation.POWER,
        DatabaseConstantInformation.ACTIVESUBS,
        DatabaseConstantInformation.CODE,
        DatabaseConstantInformation.PRODUCER};

        return database.query(DatabaseConstantInformation.MEDICAMENTINFOTABLE, columns, null, null, null, null, null);
    }

    public long UpdateRowMedInfo(int id, String name, String power, String activeSubs, int code, String producer){
        ContentValues cvUpdateRow = new ContentValues();
        cvUpdateRow.put(DatabaseConstantInformation.MEDNAME, name);
        cvUpdateRow.put(DatabaseConstantInformation.POWER, power);
        cvUpdateRow.put(DatabaseConstantInformation.ACTIVESUBS, activeSubs);
        cvUpdateRow.put(DatabaseConstantInformation.CODE, code);
        cvUpdateRow.put(DatabaseConstantInformation.PRODUCER, producer);
        return database.update(DatabaseConstantInformation.MEDICAMENTINFOTABLE, cvUpdateRow, DatabaseConstantInformation.ID_MED + "=" + String.valueOf(id), null);
    }

    public String GetColumnContent(String colName, long id){
        String colContent = "";
        Cursor cursor;

        cursor = database.rawQuery("SELECT " + colName + " FROM " + DatabaseConstantInformation.MEDICAMENTINFOTABLE + " WHERE " + DatabaseConstantInformation.ID_MED + " = ?", new String[]{String.valueOf(id)});
        cursor = database.query(DatabaseConstantInformation.MEDICAMENTINFOTABLE,
                new String[]{colName},
                DatabaseConstantInformation.ID_MED + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            colContent = cursor.getString(cursor.getColumnIndex(colName));
        }
        return colContent;
    }

    public Cursor GetNames(){
        return database.query(true, DatabaseConstantInformation.MEDICAMENTINFOTABLE, new String[]{DatabaseConstantInformation.MEDNAME}, null, null, DatabaseConstantInformation.MEDNAME, null, null, null);

    }

    public Cursor GetCodes(){
        return database.query(true, DatabaseConstantInformation.MEDICAMENTINFOTABLE, new String[]{DatabaseConstantInformation.CODE}, null, null, DatabaseConstantInformation.CODE, null, null, null);

    }

    public Cursor FindMedicamentByName(String name, String columnName) {

        String[] columns = new String[]{DatabaseConstantInformation.ID_MED,
                DatabaseConstantInformation.MEDNAME,
                DatabaseConstantInformation.POWER,
                DatabaseConstantInformation.ACTIVESUBS,
                DatabaseConstantInformation.CODE,
                DatabaseConstantInformation.PRODUCER};

        return database.query(DatabaseConstantInformation.MEDICAMENTINFOTABLE,
                columns,
                columnName + "=?" + " COLLATE NOCASE",
                new String[]{name},
                null, null, DatabaseConstantInformation.ID_MED);
    }

    public Cursor FindMedicamentByCode(String code, String columnName) {

        String[] columns = new String[]{DatabaseConstantInformation.ID_MED,
                DatabaseConstantInformation.MEDNAME,
                DatabaseConstantInformation.POWER,
                DatabaseConstantInformation.ACTIVESUBS,
                DatabaseConstantInformation.CODE,
                DatabaseConstantInformation.PRODUCER};

        return database.query(DatabaseConstantInformation.MEDICAMENTINFOTABLE,
                columns,
                columnName + "=?" + " COLLATE NOCASE",
                new String[]{code}, null, null, DatabaseConstantInformation.CODE);
    }


    public void DeleteMedicamentInfo(String id) {
        database.delete(DatabaseConstantInformation.MEDICAMENTINFOTABLE, DatabaseConstantInformation.ID_MED + "=?", new String[]{id});
    }



}
