package com.example.tick.sendmessage;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by tick on 2016/4/13.
 */
public class Replymsg extends Activity {

    private TextView phonnum;
    private TextView phontime;
    private TextView phonmsg;

    private EditText sendcontent;
    private Button rereplybt;
    private Intent intent;
    BroadcastReceiver send_sms=new SendSMS();
    BroadcastReceiver delivered_sms=new DelieveredSMS();
    String SENT_SMS_ACTION="SENT_SMS_ACTION";
    String DELIVERED_SMS_ACTION="DELIVERED_SMS_ACTION";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replysms);
        phonnum = (TextView) findViewById(R.id.renum);
        phontime = (TextView) findViewById(R.id.retime);
        phonmsg = (TextView) findViewById(R.id.recontent);
        sendcontent = (EditText) findViewById(R.id.replybody);
        rereplybt = (Button) findViewById(R.id.reply);
        registerReceiver(send_sms, new IntentFilter(SENT_SMS_ACTION));
        registerReceiver(delivered_sms, new IntentFilter(DELIVERED_SMS_ACTION));
        getdata();
        send();
    }

    private void send() {
        rereplybt.setOnClickListener(new View.OnClickListener() {

            String getnum;
            String getmsg;

            public void onClick(View v) {
                getnum = phonnum.getText().toString();
                getmsg = sendcontent.getText().toString();
                SendMessage sendmsg = new SendMessage(Replymsg.this,getmsg,getnum);
                sendmsg.sendmsg(send_sms, delivered_sms);
                ContentValues values = new ContentValues();
                values.put("date",System.currentTimeMillis());
                values.put("read",0);
                values.put("type",2);
                values.put("address",getnum);
                values.put("body",getmsg);
                getContentResolver().insert(Uri.parse("content://sms/sent"), values);
                getContentResolver().insert(Uri.parse("content://sms/"), values);
                sendcontent.setText("");
                back();
            }
        });
    }

    private void getdata() {
        intent = getIntent();
        String listnum = intent.getStringExtra("listnum");
        String listtime = intent.getStringExtra("listtime");
        String listmsg = intent.getStringExtra("listmsg");

        phonnum.setText(listnum);
        phontime.setText(listtime);
        phonmsg.setText(listmsg);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            back();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void back() {
        Intent intent=new Intent();
        intent.setClass(Replymsg.this,MsgListActivity.class);
        startActivity(intent);
        Replymsg.this.finish();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(send_sms);
        unregisterReceiver(delivered_sms);
        super.onDestroy();
    }
}
