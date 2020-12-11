package com.example.allears;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "AllEars.db";
    private static final String TABLE = "Users";
    private static final String GOAL_TABLE = "Goal";
    private static final String C1 = "ID";
    private static final String C2 = "USERNAME";
    private static final String GOAL_COLUMN = "QUESTION_COUNT";

    DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL("create table if not exists " + TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,USERNAME TEXT)");
        db.execSQL("create table if not exists " + GOAL_TABLE + "(QUESTION_COUNT INTEGER)");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    boolean insertToUserDB(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(C2, name);

        long insertStatus = db.insert(TABLE, null, contentValues);
        return insertStatus != -1;
    }

    boolean insertToGoalDB(Integer number){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues  = new ContentValues();
        contentValues.put(GOAL_COLUMN, number);

        long insertStatus = db.insert(GOAL_TABLE, null, contentValues);
        return insertStatus != -1;
    }

    Cursor getAllUserEntries() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + TABLE, null);
    }

    Cursor getGoalEntries(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + GOAL_TABLE, null);
    }

    boolean truncateTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " +TABLE);
        return true;
    }

    boolean truncateGoalTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " +GOAL_TABLE);
        return true;
    }

    boolean updateGoalEntry(Integer oldValue, Integer target) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GOAL_COLUMN, target);

        db.update(TABLE, contentValues, "TARGET = ?", new String[]{oldValue.toString()});
        return true;
    }

    Integer deleteEntry(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(C1, id);

        return db.delete(TABLE, "ID = ?", new String[]{id});
    }

}
