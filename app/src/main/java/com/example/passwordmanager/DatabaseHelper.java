package com.example.passwordmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import net.sqlcipher.database.SQLiteDatabase;
//import net.sqlcipher.database.SQLiteDatabase;
import android.provider.ContactsContract;

public class DatabaseHelper extends SQLiteOpenHelper {

    // sql variables
    private static final int DATABASE_VER = 1;
    private static DatabaseHelper dbhelper;
    public static final String DATABASE_NAME = "ACCOUNTS.db";
    public static final String TABLE_NAME = "Accounts";

    // sql commands
    private static final String SQL_CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY, TITLE VARCHAR(60) NOT NULL, URL VARCHAR(255) NOT NULL, USERNAME VARCHAR(60) NOT NULL, PASSWORD VARCHAR(60) NOT NULL, NOTES TEXT NOT NULL);";
    private static final String SQL_GET_DATA = "SELECT * FROM " + TABLE_NAME;

    private Context context;

    public DatabaseHelper(Context context){
//        super(context,"Accounts",null, DATABASE_VER);  // original
        super(context, DATABASE_NAME,null, DATABASE_VER);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        //Create tables with column: ID, TITLE, URL, USERNAME, PASSWORD, NOTES
        database.execSQL(SQL_CREATE_TABLE_QUERY);
//        database.execSQL("CREATE TABLE Accounts (ID INTEGER PRIMARY KEY, TITLE VARCHAR(60) NOT NULL, URL VARCHAR(255) NOT NULL, USERNAME VARCHAR(60) NOT NULL, PASSWORD VARCHAR(60) NOT NULL, NOTES TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int arg1, int arg2) {
        //Empty
    }

    public boolean addData(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.insert(TABLE_NAME, null, values);
//        long result = db.insert("Accounts", null, values);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateData(ContentValues values, long id){
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.update("Accounts", values, "ID=" + id, null);
        return true;
    }

    public boolean deleteData(long id){
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete("Accounts", "ID=" + id, null);
        return true;
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = SQL_GET_DATA;
//        String query = "SELECT * FROM " + TABLE_NAME;
//        String query = "SELECT * FROM " + "Accounts";

        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void closeDB() {
        close();
    }

}
