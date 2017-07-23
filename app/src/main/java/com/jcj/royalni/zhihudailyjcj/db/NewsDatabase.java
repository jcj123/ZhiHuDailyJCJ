package com.jcj.royalni.zhihudailyjcj.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jcj.royalni.zhihudailyjcj.bean.Story;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

/**
 * Created by jcj on 2017/7/16.
 */

public class NewsDatabase {

    private NewsDatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    public NewsDatabase(Context context) {
        databaseHelper = new NewsDatabaseHelper(context,"ZhiHuNews.db",null,1);
        db = databaseHelper.getWritableDatabase();
    }

    public void insert(Story story) {
        ContentValues values = new ContentValues();
        values.put("id",story.getId());
        values.put("isRead",story.isRead());

        final long insert = db.insert(NewsDatabaseHelper.NEWS_ISREAD_TABLENAME, null, values);
    }

    public List queryIsRead() {
        List list = new ArrayList();
        final Cursor query = db.query(NewsDatabaseHelper.NEWS_ISREAD_TABLENAME, new String[]{"id"}
                , null, null, null, null, null);
        while (query.moveToNext()) {
           int isRead = query.getInt(query.getColumnIndex("id"));
            list.add(isRead);
        }
        System.out.println("list"+list);
        return list;
    }
}
