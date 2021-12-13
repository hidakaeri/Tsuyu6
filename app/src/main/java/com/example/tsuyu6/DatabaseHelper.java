package com.example.tsuyu6;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tsuyu6.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE tsuyu6 (");
        sb.append("_id INTEGER PRIMARY KEY,");
        sb.append("date DATE,");
        sb.append("item TEXT,");
        sb.append("amount INTEGER,");
        sb.append("memo TEXT, ");
        sb.append("flag TEXT");
        sb.append(");");
        String sql = sb.toString();

        StringBuilder tsb = new StringBuilder();
        tsb.append("CREATE TABLE target6 (");
        tsb.append("targetamount INTEGER, ");
        tsb.append("targetlimit DATE");
        tsb.append(");");
        String targetSql = tsb.toString();

        db.execSQL(sql);
        db.execSQL(targetSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }
}

