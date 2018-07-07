package com.example.sonatest.ults100;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper{

    byte[] signalData = null;

    private static final int DATABASE_VERSION = 1;
    private static final String  DATABASE_NAME = "signals.db";
    private static final String TABLE_SIGNAL = "signal_table";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DATETIME = "signal_datetime";
    private static final String COLUMN_SIGNAL_DATA = "signal_data";

    //We need to pass database information along to superclass
    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_SIGNAL + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATETIME + " TEXT, " + COLUMN_SIGNAL_DATA + " BLOB" + ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIGNAL);
        onCreate(db);
    }

    public void addSignalToTable() {

        // Get the signalName and signalData delivered
        signalData = getSignalData();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATETIME, getDateTime());
        values.put(COLUMN_SIGNAL_DATA, signalData);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_SIGNAL, null, values);
        db.close();

    }

    public ArrayList getNameForListView() {

        ArrayList<String> signalNameList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();

        // A Cursor provides read and write access to database results
        Cursor c = db.rawQuery("SELECT * FROM "+ TABLE_SIGNAL +" WHERE 1", null);

        // Move to the first row of results
        c.moveToFirst();

        // Verify that we have results
        if(c != null && (c.getCount() > 0)) {

            do {
                // Get the results and store them in a String
                String nameOfSignal = c.getString(c.getColumnIndex(COLUMN_DATETIME));
                signalNameList.add(nameOfSignal);
                // Keep getting results as long as they exist
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return signalNameList;
    }


    public byte[] databaseToGraph(String dateTime) {

        byte[] savedBuf = null;
        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_SIGNAL + " WHERE 1", null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if(c.getString(c.getColumnIndex(COLUMN_DATETIME)).equals(dateTime)) {
                savedBuf = c.getBlob(c.getColumnIndex(COLUMN_SIGNAL_DATA));
                break;
            }
            c.moveToNext();
        }
        c.close();
        db.close();

        return savedBuf;
    }

    public void deleteSignal(String nameToDelete) {

        SQLiteDatabase db = getWritableDatabase();
        // Delete matching id in database
        db.execSQL("DELETE FROM " + TABLE_SIGNAL + " WHERE " + COLUMN_DATETIME + " = '" + nameToDelete + "';");
        db.close();
    }
/*
    public void deleteDatabase(View view) {

        // Delete database
        this.deleteDatabase("MyContacts");

    }*/

    private byte[] getSignalData() {
        return signalData;
    }

    public void setSignalData(byte[] data) {
        signalData = data;
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
