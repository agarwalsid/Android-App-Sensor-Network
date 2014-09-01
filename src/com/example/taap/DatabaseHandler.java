package com.example.taap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
 
import android.content.ContentValues;
import android.database.Cursor;

public class DatabaseHandler extends SQLiteOpenHelper {
	 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "TAAPManager";
 
    // Contacts table name
    private static final String TABLE_TAAPDATA = "taapdata";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DT = "dt";
    private static final String KEY_TEMPER = "temper";
    private static final String KEY_CONDI = "condi";
    
    private static final String[] COLUMNS = {KEY_ID,KEY_DT,KEY_TEMPER, KEY_CONDI};
 
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TAAPDATA_TABLE = "CREATE TABLE " + TABLE_TAAPDATA + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DT + " TEXT,"
                + KEY_TEMPER + " TEXT," + KEY_CONDI + " TEXT" + ")";
        db.execSQL(CREATE_TAAPDATA_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAAPDATA);
 
        // Create tables again
        onCreate(db);
    }
    
 // Adding new contact
    void addData(Data data) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_DT, data.getDt()); // Contact Name
        values.put(KEY_TEMPER, data.getTemper()); // Contact Phone
        values.put(KEY_CONDI, data.getCondi()); // Contact Phone
 
        // Inserting Row
        db.insert(TABLE_TAAPDATA, null, values);
        db.close(); // Closing database connection
    }
 
    // Getting single contact
    Data getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_TAAPDATA, COLUMNS, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        Data data = new Data(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        // return contact
        return data;
    }
     
    // Getting All Contacts
    public List<Data> getAllData() {
        List<Data> dataList = new ArrayList<Data>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TAAPDATA;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        Data data = null;
        if (cursor.moveToFirst()) {
            do {
                data = new Data();
                data.setID(Integer.parseInt(cursor.getString(0)));
                data.setDt(cursor.getString(1));
                data.setTemper(cursor.getString(2));
                data.setCondi(cursor.getString(3));
                // Adding contact to list
                dataList.add(data);
            } while (cursor.moveToNext());
        }
 
        // return contact list
        return dataList;
    }
 
    // Updating single contact
    public int updateData(Data data) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_DT, data.getDt());
        values.put(KEY_TEMPER, data.getTemper());
        values.put(KEY_CONDI, data.getCondi());
 
        // updating row
        return db.update(TABLE_TAAPDATA, values, KEY_ID + " = ?",
                new String[] { String.valueOf(data.getID()) });
    }
 
 
 
 
}