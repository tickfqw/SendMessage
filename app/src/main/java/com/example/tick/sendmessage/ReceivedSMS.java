package com.example.tick.sendmessage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by tick on 2016/4/15.
 */
public class ReceivedSMS extends BroadcastReceiver {

    private MessageListener mMessageListener1;
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "a message received",Toast.LENGTH_SHORT).show();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0; i < pdus.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }
            for (SmsMessage message : messages) {
                String strFrom = message.getDisplayOriginatingAddress();
                String strMsg = message.getDisplayMessageBody();
                Log.d("SMSReceiver","From:"+strFrom);
                Log.d("SMSReceiver","Msg:"+strMsg);
            }
        }
        mMessageListener1.OnReceived();
    }
    public interface  MessageListener {
        public void OnReceived();
    }
    public void setOnReceivedMessageListener(MessageListener messageListener) {
        this.mMessageListener1 = messageListener;
    }
}

