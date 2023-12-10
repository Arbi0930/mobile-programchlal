package com.example.sqlitelab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DbHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION =1;
    private static final String DB_NAME  = "bookdb";
    private static final String TABLE_Users = "bookdetails";
    private static final String KEY_ID = "id";
    private static final String B_NAME = "bname";
    private static final String B_AUTHOR = "bauthor";
    private static final String B_DATE = "bdate";

    public DbHandler(Context context){
        super(context,DB_NAME, null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE "+ TABLE_Users + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + B_NAME + " TEXT, "
                +B_AUTHOR + " TEXT, "
                +B_DATE + " TEXT" + " )";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_Users);
    onCreate(db);
    }

    void insertBookDetails(String bname, String bauthor, String bdate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues  cValues = new ContentValues();
        cValues.put(B_NAME, bname);
        cValues.put(B_AUTHOR, bauthor);
        cValues.put(B_DATE,  bdate);
        long newRowId = db.insert(TABLE_Users, null, cValues);
        db.close();
    }
    public ArrayList<HashMap<String, String>> GetUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String,  String>> userList = new ArrayList<>();
        String query = "SELECT bname, bauthor, bdate FROM " + TABLE_Users;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()){
            HashMap<String, String> user = new HashMap<>();
            user.put("bname", cursor.getString(cursor.getColumnIndexOrThrow(B_NAME)));
            user.put("bnauthor", cursor.getString(cursor.getColumnIndexOrThrow(B_AUTHOR)));
            user.put("bdate", cursor.getString(cursor.getColumnIndexOrThrow(B_DATE)));
            userList.add(user);
        }
        return userList;
    }
    public int DeleteBook(String bookName){
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_Users, B_NAME + " = ?", new String[]{bookName});
        db.close();
        return rowsDeleted;
    }


    public int UpdateBookDetails(String bname, String bauthor, String bdate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put(B_AUTHOR, bauthor);
        cVals.put(B_DATE, bdate);

        int rowsUpdated = db.update(TABLE_Users, cVals, B_NAME + " = ?", new String[]{bname});
        db.close();
        return rowsUpdated;
    }
}
