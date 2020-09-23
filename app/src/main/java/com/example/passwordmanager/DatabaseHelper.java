package com.example.passwordmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;

    public DatabaseHelper(Context context){
        super(context,"Accounts",null,1);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase database) {
        //Create tables with column: ID, TITLE, URL, USERNAME, PASSWORD, NOTES
        database.execSQL("CREATE TABLE Accounts (ID INTEGER PRIMARY KEY, TITLE VARCHAR(60) NOT NULL, URL VARCHAR(255) NOT NULL, USERNAME VARCHAR(60) NOT NULL, PASSWORD VARCHAR(60) NOT NULL, NOTES TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int arg1, int arg2) {
        //Empty
    }

    public boolean addData(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.insert("Accounts", null, values);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + "Accounts";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void closeDB() {
        close();
    }

}
