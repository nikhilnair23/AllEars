package com.example.allears;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Stickers.db";
    private static final String TABLE = "Users";
    private static final String C1 = "ID";
    private static final String C2 = "USERNAME";

    DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL("create table if not exists " + TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,USERNAME TEXT)");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    boolean inserttoDB(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(C2, name);

        long insertStatus = db.insert(TABLE, null, contentValues);
        return insertStatus != -1;
    }

    Cursor getAllEntries() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + TABLE, null);
    }

    boolean truncateTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " +TABLE);
        return true;
    }

    boolean updateEntry(String id, String name, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(C1, id);
        contentValues.put(C2, name);

        db.update(TABLE, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    Integer deleteEntry(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(C1, id);

        return db.delete(TABLE, "ID = ?", new String[]{id});
    }

}
