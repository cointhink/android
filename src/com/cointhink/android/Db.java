package com.cointhink.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Db {
    private SQLiteDatabase db;

    public Db(Context context){
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    
    public void close(){
        db.close();
    }

    public class DbHelper extends SQLiteOpenHelper {
        
        private static final String DATABASE_NAME = "notices";

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }
    
        @Override
        public void onCreate(SQLiteDatabase db) {
        }
    
        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        }
                
    }
}
