package com.cointhink.android;

import java.util.Date;

import org.apache.http.Header;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class GCMReceiver extends BroadcastReceiver implements Constants {
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
            handleRegistration(context, intent);
        } else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
            handleMessage(context, intent);
        }
        setResultCode(Activity.RESULT_OK);
     }
    
    private void handleRegistration(Context context, Intent intent) {
        String registration = intent.getStringExtra("registration_id");
        String err = intent.getStringExtra("error");
        if (err != null) {
            // Registration failed, should try again later.
            Log.d(APP_TAG, "C2DM reg failed: "+err);
            if("ACCOUNT_MISSING".equals(err)){
                Toast.makeText(context, "Sign into google first", 
                           Toast.LENGTH_LONG).show();               
            }
        } else if (intent.getStringExtra("unregistered") != null) {
            // unregistration done, new messages from the authorized sender will be rejected
            Log.d(APP_TAG, "C2DM unregistered");
        } else if (registration != null) {
           // Send the registration ID to the 3rd party site that is sending the messages.
           // This should be done in a separate thread.
           // When done, remember that all registration is done. 
            Log.d(APP_TAG, "C2DM reg OK for "+registration);
            RequestParams postData = new RequestParams();
            postData.put("registration_id", registration);
            Net.register(postData, new RegisterHandler());
        }
    }
    
    protected void handleMessage(Context context, Intent intent) {
        String message = intent.getExtras().getString("message");
        String sender = intent.getExtras().getString("sender");
        String date = intent.getExtras().getString("date");
        String type = intent.getExtras().getString("type");
        String urgency = intent.getExtras().getString("urgency");
        Log.d(APP_TAG, "C2DM Message: date:"+date+" sender:"+sender+": "+message);
        Db db = new Db(context);
        db.insertNotice(date, sender, message, type, urgency);
        db.close();
        addNotice(context, date, sender, message);
        if(MainActivity.UiHandler != null) {
            UiRunnable uiRun = new UiRunnable();
            uiRun.setMessage(message);
            uiRun.setSender(sender);
            Date when = Util.iso8601ToDate(date);
            uiRun.setWhen(when.getTime());
            MainActivity.UiHandler.post(uiRun);
        }
    }
    
    private void addNotice(Context context, String date, String sender, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.notification_icon, null, System
                .currentTimeMillis());
        notification.flags = notification.flags ^ Notification.FLAG_AUTO_CANCEL;
        Log.i(APP_TAG,"notification sender:"+sender+" message:"+message);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, 
                                        new Intent(context, MainActivity.class), 0);
        notification.setLatestEventInfo(context, sender, message, contentIntent);
        notificationManager.notify(1, notification);        
        
    }

    public class UiRunnable implements Runnable {
        private CharSequence message;
        private CharSequence sender;
        private long when;

        @Override
        public void run(){
            resetList();
        }

        public void resetList() {
            if(MainActivity.noticeList != null){
                ((SimpleCursorAdapter)MainActivity.noticeList.getAdapter()).getCursor().requery();
            }
        }


        public void setMessage(CharSequence message) {
            this.message = message;
        }

        public void setSender(CharSequence sender) {
            this.sender = sender;
        }

        public void setWhen(long when) {
            this.when = when;
        }
    }
    
    public class RegisterHandler extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Log.d(APP_TAG, "Success: "+ new String(responseBody));
        }        
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Log.d(APP_TAG, "Error: "+error.toString());
        }        
    }
}
