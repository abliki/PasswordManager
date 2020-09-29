package com.example.passwordmanager;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import android.icu.text.LocaleDisplayNames;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = DatabaseHelper.class.getSimpleName();

    // sql variables
    private static final int DATABASE_VER = 1;
    //    private static DatabaseHelper dbhelper;
    public static final String DATABASE_NAME = "ACCOUNTS_ENC.db";
    public static final String TABLE_NAME = "Accounts";
//    public static final String DATABASE_NAME_TEST = "TEST3.db";

    // sql commands
    private static final String SQL_CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY, TITLE VARCHAR(60) NOT NULL, URL VARCHAR(255) NOT NULL, USERNAME VARCHAR(60) NOT NULL, PASSWORD VARCHAR(60) NOT NULL, NOTES TEXT NOT NULL);";
    private static final String SQL_GET_DATA = "SELECT * FROM " + TABLE_NAME;

    private Context context;


    private String masterKeyConn;

    public DatabaseHelper(Context context) {
//        super(context, DATABASE_NAME,null, DATABASE_VER);
        super(context, DATABASE_NAME, null, DATABASE_VER);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        //Create tables with column: ID, TITLE, URL, USERNAME, PASSWORD, NOTES
        database.execSQL(SQL_CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int arg1, int arg2) {
        //Empty
    }

    // CRUD methods
    public boolean addData(ContentValues values) {
//        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db = this.getWritableDatabase(masterKeyConn);

        long result = db.insert(TABLE_NAME, null, values);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            Log.d(LOG_TAG, "addData database insertion failed");
            return false;
        } else {
            return true;
        }
    }

    public boolean updateData(ContentValues values, long id) {
//        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db = this.getWritableDatabase(masterKeyConn);

        long result = db.update(TABLE_NAME, values, "ID=" + id, null);

        if (result == -1) {
            Log.d(LOG_TAG, "updateData database update failed");
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteData(long id) {
//        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db = this.getWritableDatabase(masterKeyConn);

        long result = db.delete(TABLE_NAME, "ID=" + id, null);

        if (result == -1) {
            Log.d(LOG_TAG, "deleteData database deletion failed");
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData() {
//        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db = this.getWritableDatabase(masterKeyConn);

        String query = SQL_GET_DATA;

        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void closeDB() {
        close();
    }

    public void setMasterKey(String key) {
        if (this.masterKeyConn == null) {
            this.masterKeyConn = key;
        }
    }

}
