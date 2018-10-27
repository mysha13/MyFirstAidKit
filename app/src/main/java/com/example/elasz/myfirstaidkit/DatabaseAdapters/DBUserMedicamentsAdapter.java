package com.example.elasz.myfirstaidkit.DatabaseAdapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.elasz.myfirstaidkit.DatabaseImplement.DatabaseConstantInformation;
import com.example.elasz.myfirstaidkit.DatabaseImplement.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    public long medCount() throws SQLException{
        long count = DatabaseUtils.queryNumEntries(database, DatabaseConstantInformation.USERMEDICAMENTSTABLE);
        return count;
    }

    public ArrayList<String> medNear(String date) throws  SQLException{
        String colName = DatabaseConstantInformation.ID_USERMED ;
        ArrayList<String> meds  = new ArrayList<String>();
        Cursor cursor;

        Calendar cal= Calendar.getInstance();
        Date currentTime = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //"dd/MM/yyyy_HHmmss");
        String currentDateandTime = sdf.format(currentTime);
        cal.add(Calendar.DAY_OF_MONTH, 7);
        String newdate = sdf.format(cal.getTime());

        Cursor c = database.rawQuery("select Id_UserMed, EXP from " + DatabaseConstantInformation.USERMEDICAMENTSTABLE + " where EXP BETWEEN '" + currentDateandTime + "' AND '" + newdate + "' ORDER BY EXP ASC", null);
 //https://stackoverflow.com/questions/14207494/android-sqlite-select-between-date1-and-date2
        if (c != null ) {
            if  (c.moveToFirst()) {
                do {
                    int tempId = c.getInt(c.getColumnIndex("Id_UserMed"));
                    long tempUnixTime = c.getLong(c.getColumnIndex("EXP"));

                    //convert tempUnixTime to Date
                    java.util.Date startDateDate = new java.util.Date(tempUnixTime);

                    //create SimpleDateFormat formatter
                    SimpleDateFormat formatter1;
                    formatter1 = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);

                    //convert Date to SimpleDateFormat and convert to String
                    String tempStringStartDate = formatter1.format(startDateDate);

                    //int tempHours = c.getInt(c.getColumnIndex("Hours"));
                   // meds.add(c.getString(0));
                    meds.add(+ tempId + "    Date: " + tempStringStartDate);// + "    Hours: " + tempHours);
                }while (c.moveToNext());
            }
        }
        /*cursor = database.rawQuery("SELECT " + colName + " FROM " + DatabaseConstantInformation.USERMEDICAMENTSTABLE +
                " WHERE " + DatabaseConstantInformation.EXPDATE + " < ?", new String[]{String.valueOf(date)});

        cursor = database.query(DatabaseConstantInformation.USERMEDICAMENTSTABLE,
                new String[]{colName},
                DatabaseConstantInformation.EXPDATE + "<?",
                new String[]{String.valueOf(date)},
                null, null, null);

        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            meds.add(String.valueOf(cursor.getColumnIndex(colName)));
            Log.v("Cursor DB", DatabaseUtils.dumpCursorToString(cursor));
        }*/
        return meds;
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

    public long UpdateRowUserMedInfo(int id, String name, int id_medicament, String exp_date, String open_date, int form, int purpose, double amount, int amount_form, String person, String note, boolean istake, byte[] image){
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

    /*public Cursor FindUserMedicamentByCode(String code, String columnName) {

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
    }*/

    public void renameForm(String formId) {
        ContentValues rowUpdate = new ContentValues();
        rowUpdate.put(DatabaseConstantInformation.FORM, String.valueOf(1));
        database.update(DatabaseConstantInformation.USERMEDICAMENTSTABLE, rowUpdate, DatabaseConstantInformation.FORM + "=" + String.valueOf(formId), null);
    }

    public void updateAmount(String id, double newvalue){
        ContentValues rowUpdate = new ContentValues();
        rowUpdate.put(DatabaseConstantInformation.AMOUNT, Double.valueOf(newvalue));
        database.update(DatabaseConstantInformation.USERMEDICAMENTSTABLE, rowUpdate, DatabaseConstantInformation.ID_MEDICAMENT + "=" + String.valueOf(id), null);

    }

    public void deleteMed(String id) {
        database.delete(DatabaseConstantInformation.USERMEDICAMENTSTABLE, DatabaseConstantInformation.ID_MEDICAMENT + "=?", new String[]{id});
    }

    public byte[] getImageByteArray(long id){
        byte[] image = null;
        Cursor cursor;
        cursor = database.query(DatabaseConstantInformation.USERMEDICAMENTSTABLE,
                new String[]{DatabaseConstantInformation.IMAGE},
                DatabaseConstantInformation.ID_MED + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            image = cursor.getBlob(cursor.getColumnIndex(DatabaseConstantInformation.IMAGE));
        }
        return image;
    }


}
