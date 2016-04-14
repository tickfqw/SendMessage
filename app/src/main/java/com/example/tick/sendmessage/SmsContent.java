package com.example.tick.sendmessage;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tick on 2016/4/14.
 */
public class SmsContent {
    private Activity activity;//这里有个activity对象，不知道为啥以前好像不要，现在就要了。自己试试吧。
    private Uri uri;
    List<Map<String,Object>> contents;

    public SmsContent(Activity activity,Uri uri) {
        contents=new ArrayList<Map<String,Object>>();
        this.activity = activity;
        this.uri=uri;
    }

    public List<Map<String, Object>> getSmsInPhone() {
        String[] projection = new String[] { "_id", "address", "person",
                "body", "date", "type" };
        Cursor cur = activity.managedQuery(uri, projection, null, null,
                "date desc");
        while (cur.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            int index_Address = cur.getColumnIndex("address");
            int index_Person = cur.getColumnIndex("person");
            int index_Body = cur.getColumnIndex("body");
            int index_Date = cur.getColumnIndex("date");
            int index_Type = cur.getColumnIndex("type");
            String strAddress = cur.getString(index_Address);
            cur.getInt(index_Person);
            String strbody = cur.getString(index_Body);
            long longDate = cur.getLong(index_Date);
            int type = cur.getInt(index_Type);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date d = new Date(longDate);
            String strDate = dateFormat.format(d);
            map.put("listnum", strAddress);
            map.put("listmsg", strbody);
            map.put("listtime", strDate);
            if (type == 1) {
                map.put("listtype", "收");
            } else {
                map.put("listtype", "发");
            }
            contents.add(map);
        }
        if (!cur.isClosed()) {
            cur.close();
            cur = null;
        }
    return contents;
    }
}
