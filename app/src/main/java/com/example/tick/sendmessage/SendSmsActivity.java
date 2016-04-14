package com.example.tick.sendmessage;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by tick on 2016/4/13.
 */
public class SendSmsActivity extends Activity{
EditText ednum;
    EditText edbody;
    Button send;
    BroadcastReceiver send_sms;
    BroadcastReceiver delivered_sms;
    String SENT_SMS_ACTION="SENT_SMS_ACTION";
    String DELIVERED_SMS_ACTION="DELIVERED_SMS_ACTION";
    String getnum;
    String getmsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendmsg);
        ednum=(EditText) findViewById(R.id.ednum);
        edbody=(EditText) findViewById(R.id.edbody);
        send=(Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getnum=ednum.getText().toString();
                getmsg=edbody.getText().toString();
                SendMessage sendmsg=new SendMessage(SendSmsActivity.this,getmsg,getnum);
                sendmsg.sendmsg(send_sms, delivered_sms);
                ContentValues values=new ContentValues();
                values.put("date",System.currentTimeMillis());
                values.put("read",0);
                values.put("type",2);
                values.put("address",getnum);
                values.put("body", getmsg);
                getContentResolver().insert(Uri.parse("content://sms/sent"), values);
                edbody.setText("");
                back();
            }
        });

        send_sms=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(context,"短信发送成功",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context,"短信发送失败",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context,"无线电被关闭",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context,"未提供PUD协议",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        delivered_sms=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context,"短信已被接收",Toast.LENGTH_SHORT).show();
            }
        };
        registerReceiver(send_sms, new IntentFilter(SENT_SMS_ACTION));
        registerReceiver(delivered_sms, new IntentFilter(DELIVERED_SMS_ACTION));
    }

    private void back() {
        Intent intent=new Intent(SendSmsActivity.this,MsgListActivity.class);
        SendSmsActivity.this.startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(send_sms);
        unregisterReceiver(delivered_sms);
        super.onDestroy();
    }
}
