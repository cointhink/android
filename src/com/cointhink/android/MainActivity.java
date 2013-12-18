package com.cointhink.android;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.ListView;

public class MainActivity extends Activity {

    static String google_account_id = "837868261850";
    static Handler UiHandler;
    public static ListView noticeList;
    private Db db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UiHandler = new Handler();
        googleCloudRegister();
    }

    private void googleCloudRegister() {
        // C2DM
        Intent registrationIntent = new Intent(
                "com.google.android.c2dm.intent.REGISTER");
        registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0,
                new Intent(), 0));
        registrationIntent.putExtra("sender", google_account_id);
        startService(registrationIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        db = new Db(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
