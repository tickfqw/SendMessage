package com.example.tick.sendmessage;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by tick on 2016/4/15.
 */
public class Contact extends Activity {
    ListView contactsView;
    ArrayAdapter<String> adapter;
    List<String> contactsList=new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_phone_num);
        contactsView =(ListView) findViewById(R.id.contacts_view);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,contactsList);
        contactsView.setAdapter(adapter);
        readContacts();
        contactsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent intent=new Intent();
                String str= contactsList.get(position);
                str=str.trim();
                String str2="";
                if(str != null && !"".equals(str)){
                    for(int i=0;i<str.length();i++){
                        if(str.charAt(i)>=48 && str.charAt(i)<=57){
                            str2+=str.charAt(i);
                        }
                    }

                }
                intent.putExtra("data_return",str2);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void readContacts() {
        Cursor cursor=null;
        try {
            cursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactsList.add(displayName + "\n" + number);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            if(cursor!=null){
                cursor.close();
            }
        }
    }
}