package com.example.elasz.myfirstaidkit.DatabaseImplement;

/**
 * Created by elasz on 12.09.2018.
 */

public class DatabaseConstantInformation {

    public static final String DBName = "FirstAidKit.db";
    public static final int DBVersion = 1;

    public static final String USERMEDICAMENTSTABLE = "UserMedicaments";
    public static final String ID_USERMED = "Id_UserMed";
    public static final String NAME = "Name";
    public static final String ID_MEDICAMENT="Id_Medicament";
    public static final String EXPDATE = "EXP";
    public static final String OPENDATE = "Opened";
    public static final String FORM = "Id_Form";
    public static final String PURPOSE = "Id_Purpose";
    public static final String AMOUNT = "Amount";
    public static final String AMOUNT_FORM = "Id_AmountForm";
    public static final String PERSON = "Id_Person";
    public static final String NOTE = "Note";

    public static final String FORMTABLE = "Form";
    public static final String ID_FORM = "Id_Form";
    public static final String FORM_NAME = "Form_Name";

    public static final String AMOUNTFORMTABLE = "AmountForm";
    public static final String ID_AMOUNTFORM = "Id_AmountForm";
    public static final String AMOUNT_FORM_NAME = "AmountForm_Name";

    public static final String PURPOSETABLE = "Purpose";
    public static final String ID_PURPOSE = "Id_Purpose";
    public static final String PURPOSE_NAME = "Form_Name";

    public static final String PERSONTABLE = "Person";
    public static final String ID_PERSON = "Id_Person";
    public static final String PERSON_NAME = "Person_Name";

    public static final String MEDICAMENTINFOTABLE = "MedicamentInfo";
    public static final String ID_MED = "Id_Medicament";
    public static final String MEDNAME = "MedName";
    public static final String POWER = "Power";
    public static final String ACTIVESUBS = "ActiveSubs";
    public static final String CODE = "Code";
    public static final String PRODUCER = "Producer";


    public static final String CREATE_TABLE_USERMEDICAMENTS = "CREATE TABLE " + USERMEDICAMENTSTABLE + "("
            + ID_USERMED + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NAME + " TEXT NOT NULL, "
            + ID_MEDICAMENT + " INT, "
            + EXPDATE + " TEXT, "
            + OPENDATE + " TEXT, "
            + FORM + " INTEGER, "
            + PURPOSE + " TEXT, "
            + AMOUNT + " DOUBLE NOT NULL, "
            + AMOUNT_FORM + " INT, "
            + PERSON + " INT, "
            + NOTE + " TEXT "
            + ");";

    public static final String CREATE_TABLE_FORM = "CREATE TABLE " + FORMTABLE + "("
            + ID_FORM + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FORM_NAME + " TEXT NOT NULL );";


    public static final String CREATE_TABLE_AMOUNTFORM = "CREATE TABLE " + AMOUNTFORMTABLE + "("
            + ID_AMOUNTFORM + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + AMOUNT_FORM_NAME + " TEXT NOT NULL );";

    public static final String CREATE_TABLE_PURPOSE = "CREATE TABLE " + PURPOSETABLE + "("
            + ID_PURPOSE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PURPOSE_NAME + " TEXT NOT NULL );";

    public static final String CREATE_TABLE_PERSON = "CREATE TABLE " + PERSONTABLE + "("
            + ID_PERSON + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PERSON_NAME + " TEXT NOT NULL );";


    public static final String CREATE_TABLE_MEDICAMENTINFO = "CREATE TABLE " + MEDICAMENTINFOTABLE + "("
            + ID_MED + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MEDNAME + " TEXT NOT NULL, "
            + POWER + " TEXT NOT NULL, "
            + ACTIVESUBS + " TEXT , "
            + CODE + " TEXT NOT NULL, "  //było int, text żeby wyszukiwac
            + PRODUCER + " TEXT NOT NULL, "
            + ");";

}
