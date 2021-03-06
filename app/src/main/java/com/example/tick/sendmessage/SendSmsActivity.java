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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by tick on 2016/4/13.
 */
public class SendSmsActivity extends Activity{
    EditText ednum;
    EditText edbody;
    Button send,cancle;
    ImageButton contact;

    BroadcastReceiver send_sms = new SendSMS();
    BroadcastReceiver delivered_sms = new DelieveredSMS();
    String SENT_SMS_ACTION="SENT_SMS_ACTION";
    String DELIVERED_SMS_ACTION="DELIVERED_SMS_ACTION";
    String getnum;
    String getmsg;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    String returnedData=data.getStringExtra("data_return");
                    ednum.setText(returnedData);
                }
                break;
            default:
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendmsg);
        ednum=(EditText) findViewById(R.id.ednum);
        edbody=(EditText) findViewById(R.id.edbody);
        send=(Button) findViewById(R.id.send);
        cancle = (Button) findViewById(R.id.cancle);
        contact = (ImageButton) findViewById(R.id.contact);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(SendSmsActivity.this,"显示通讯录",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SendSmsActivity.this,Contact.class);
                startActivityForResult(intent, 1);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getnum=ednum.getText().toString();
                getmsg=edbody.getText().toString();
                SendMessage sendmsg=new SendMessage(SendSmsActivity.this,getmsg,getnum);
                sendmsg.sendmsg(delivered_sms, delivered_sms);
                ContentValues values=new ContentValues();
                values.put("date",System.currentTimeMillis());
                values.put("read",0);
                values.put("type",2);
                values.put("address",getnum);
                values.put("body", getmsg);
                getContentResolver().insert(Uri.parse("content://sms/sent"), values);
                getContentResolver().insert(Uri.parse("content://sms/"), values);
                edbody.setText("");
                back();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        /*send_sms=new BroadcastReceiver() {
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
        };*/
        /*delivered_sms=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context,"短信已被接收",Toast.LENGTH_SHORT).show();
            }
        };*/
        registerReceiver(send_sms, new IntentFilter(SENT_SMS_ACTION));
        registerReceiver(delivered_sms, new IntentFilter(DELIVERED_SMS_ACTION));
    }

    private void back() {
        Intent intent=new Intent();
        intent.setClass(SendSmsActivity.this,MsgListActivity.class);
        startActivity(intent);
        SendSmsActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        //unregisterReceiver(send_sms);
        unregisterReceiver(delivered_sms);
        super.onDestroy();
    }
}
