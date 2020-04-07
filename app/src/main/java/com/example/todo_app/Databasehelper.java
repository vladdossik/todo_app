package com.example.todo_app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.todo_app.MainActivity.dbname;

public class Databasehelper extends SQLiteOpenHelper {
    private  static final String COL2 = "todo";
    public static final String DB_name="goals";
    public Databasehelper(Context context) {

        super(context, dbname, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + DB_name + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 + " TEXT)";
        db.execSQL(createTable);
    }
        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_name);
            onCreate(db);
        }
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DB_name;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    }
