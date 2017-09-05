package com.jcj.royalni.zhihudailyjcj.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jcj on 2017/7/16.
 */

public class NewsDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_ISREAD_TABLE = "create table News_isRead" +
            "( id integer, " + "isRead boolean)";
    public static final String NEWS_ISREAD_TABLENAME = "News_isRead";

    public NewsDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ISREAD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
