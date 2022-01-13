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

        StringBuilder lsb = new StringBuilder();
        lsb.append("CREATE TABLE login6 (");
        lsb.append("loginid TEXT, ");
        lsb.append("loginpass TEXT");
        lsb.append(");");
        String loginSql = lsb.toString();

        StringBuilder esb = new StringBuilder();
        esb.append("CREATE TABLE event6 (");
        esb.append("_id INTEGER PRIMARY KEY,");
        esb.append("eventname TEXT, ");
        esb.append("eventamount TEXT, ");
        esb.append("eventlimit DATE, ");
        esb.append("eventmember TEXT");
        esb.append(");");
        String eventSql = esb.toString();

        StringBuilder isb = new StringBuilder();
        isb.append("CREATE TABLE individual6 (");
        isb.append("_id INTEGER PRIMARY KEY,");
        isb.append("event_id TEXT, ");
        isb.append("date DATE, ");
        isb.append("member TEXT, ");
        isb.append("amount TEXT");
        isb.append(");");
        String individualSql = isb.toString();

        db.execSQL(sql);
        db.execSQL(targetSql);
        db.execSQL(loginSql);
        db.execSQL(eventSql);
        db.execSQL(individualSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }
}

