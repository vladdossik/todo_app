package com.example.todo_app;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

public class ForNewDb extends SQLiteOpenHelper {
    private static  String COL2 = "todo";
    private static String COL3="checked";
    public static  String DB_name="qwerty";
    public ForNewDb(Context context) {
        super(context, DB_name, null, 11);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + DB_name + " (ID INTEGER PRIMARY KEY , " +
                COL2 + " TEXT," +
                COL3 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_name);
        onCreate(db);
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DB_name;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void Add(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, name);
        contentValues.put(COL3,0);
        db.insert(DB_name, null, contentValues);
    }

    public void delete(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_name, COL2 +" = " +name, null);
    }
    public void replace(String name) {
    SQLiteDatabase db=this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put(COL3, 1);
    db.update(DB_name,contentValues, COL2+" = "+name,null);

    }


}

