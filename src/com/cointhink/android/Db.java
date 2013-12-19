package com.cointhink.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Db {
    private SQLiteDatabase db;
    static final String TABLE_NAME="notices";
    static final String SENDER_COLUMN="sender";
    static final String DATE_COLUMN="date";
    static final String MESSAGE_COLUMN="message";
    static final String TYPE_COLUMN="type";
    static final String URGENCY_COLUMN="urgency";
    
    public Db(Context context){
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    
    public void close(){
        db.close();
    }

    public Cursor notices(){
        Cursor results = db.rawQuery("SELECT * "+
                        "FROM "+TABLE_NAME+" "+
                        "ORDER BY "+DATE_COLUMN+" desc",
                        null);
        return results;
    }
    
    public void insertNotice(String date, String sender, String message, String type, String urgency){
        ContentValues cv=new ContentValues();
        cv.put(DATE_COLUMN, date);
        cv.put(SENDER_COLUMN, sender);
        cv.put(MESSAGE_COLUMN, message);
        cv.put(TYPE_COLUMN, type);
        cv.put(URGENCY_COLUMN, urgency);
        db.insert(TABLE_NAME, null, cv);
    }

    public class DbHelper extends SQLiteOpenHelper {
        
        private static final String DATABASE_NAME = "cointhink";

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }
    
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE "+TABLE_NAME+" ("+
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    SENDER_COLUMN+" TEXT, "+
                    DATE_COLUMN+" TEXT, "+
                    MESSAGE_COLUMN+" TEXT, "+
                    TYPE_COLUMN+" TEXT, "+
                    URGENCY_COLUMN+" TEXT "+
                    ")");
        }
    
        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        }
                
    }
}
