package com.example.yzh.baidumaptest.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

//接收并解析短信
public class SmsReceiver extends BroadcastReceiver {
    private final String TAG = "SmsReceiver";
    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    public SmsReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG,"action: "+action);
        if (SMS_RECEIVED_ACTION.equals(action)) {
            Bundle bundle = intent.getExtras();
            StringBuffer messageContent = new StringBuffer();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                String sender = null;
                for (Object pdu : pdus) {
                    SmsMessage message = SmsMessage.createFromPdu((byte[]) pdu);
                    //发送人
                    sender = message.getOriginatingAddress();
                    Log.d(TAG,"sender: "+sender);
                    //短信内容
                    messageContent.append(message.getMessageBody());
                }
                //发送解析好的内容
                if(!messageContent.toString().isEmpty()) {
                    Log.d(TAG,"send message broadcast.");
                    Intent intentBroadcast = new Intent();
                    intentBroadcast.putExtra("sender",sender);
                    intentBroadcast.putExtra("message", messageContent.toString());
                    intentBroadcast.setAction("sms_received");
                    context.sendBroadcast(intentBroadcast);
                    Log.d(TAG, "send broadcast and abort");
                }
            }
        }
    }
}
