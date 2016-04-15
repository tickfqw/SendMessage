package com.example.tick.sendmessage;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.LogRecord;

/**
 * Created by tick on 2016/4/13.
 */
public class MsgListActivity extends Activity {
    private Button sendmsg;
    private ListView msgList;
    private List<Map<String, Object>> contents;
    private SimpleAdapter adapter;
    private DelieveredSMS delivered_sms = new DelieveredSMS();
    private Receiver receiver= new Receiver();
    /*private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            Log.d("aa","Msg");
            adapter.notifyDataSetChanged();
            msgList.setAdapter(adapter);
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        Uri uri=Uri.parse(AllFinalInfo.SMS_URI_ALL);
        SmsContent sc=new SmsContent(this,uri);
        contents=sc.getSmsInPhone();
        sendmsg=(Button)findViewById(R.id.newsms);
        msgList= (ListView) findViewById(R.id.msg_list);
        sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MsgListActivity.this, SendSmsActivity.class);
                MsgListActivity.this.startActivity(intent);
                MsgListActivity.this.finish();
            }
        });
        adapter=new SimpleAdapter(MsgListActivity.this, contents,R.layout.showlist,new String[] {
                "imag","listnum","listmsg","listtime","listtype"},new int[] {R.id.imag,R.id.listnum,R.id.listmsg,R.id.listtime,R.id.type});
        adapter.notifyDataSetChanged();
        msgList.setAdapter(adapter);
        msgList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MsgListActivity.this, Replymsg.class);
                Map<String, Object> getlist = contents.get(position);
                intent.putExtra("listnum", (String) getlist.get("listnum"));
                intent.putExtra("listtime", (String) getlist.get("listtime"));
                intent.putExtra("listmsg", (String) getlist.get("listmsg"));
                intent.putExtra("listtype", (String) getlist.get("listtype"));
                MsgListActivity.this.startActivity(intent);
                // MsgListActivity.this.finish();

            }
        });
        registerReceiver(delivered_sms, new IntentFilter("DELIVERED_SMS_ACTION"));
        registerReceiver(receiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));

        receiver.setOnReceivedMessageListener(new Receiver.MessageListenerr(){
            @Override
            public void OnReceived() {
                Uri uri=Uri.parse(AllFinalInfo.SMS_URI_ALL);
                SmsContent sc=new SmsContent(MsgListActivity.this,uri);
                contents=sc.getSmsInPhone();
                adapter=new SimpleAdapter(MsgListActivity.this, contents,R.layout.showlist,new String[] {
                        "imag","listnum","listmsg","listtime","listtype"},new int[] {R.id.imag,R.id.listnum,R.id.listmsg,R.id.listtime,R.id.type});
                adapter.notifyDataSetChanged();
                msgList.setAdapter(adapter);
            }
        });

        delivered_sms.setOnReceivedMessageListener(new DelieveredSMS.MessageListener() {
            @Override
            public void OnReceived() {
               /* adapter=new SimpleAdapter(MsgListActivity.this, contents,R.layout.showlist,new String[] {
                        "imag","listnum","listmsg","listtime","listtype"},new int[] {R.id.imag,R.id.listnum,R.id.listmsg,R.id.listtime,R.id.type});
                adapter.notifyDataSetChanged();
                msgList.setAdapter(adapter);*/
                Uri uri=Uri.parse(AllFinalInfo.SMS_URI_ALL);
                SmsContent sc=new SmsContent(MsgListActivity.this,uri);
                contents=sc.getSmsInPhone();
                adapter=new SimpleAdapter(MsgListActivity.this, contents,R.layout.showlist,new String[] {
                        "imag","listnum","listmsg","listtime","listtype"},new int[] {R.id.imag,R.id.listnum,R.id.listmsg,R.id.listtime,R.id.type});
                adapter.notifyDataSetChanged();
                msgList.setAdapter(adapter);
            }
        });
    }


    /*public static void receivedSms(){
        Log.d("aa","Msga");
        Message msg = new Message();
        handler.sendMessage(msg);
    }*/

    @Override
    protected void onResume() {

        super.onResume();
        adapter.notifyDataSetChanged();
        msgList.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(delivered_sms);
        super.onDestroy();
    }
}
