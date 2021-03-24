package com.example.womensaftey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
LinearLayout alarm,fakecall,profile;
    //BottomNavigationWork..
private    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener(){


                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId())
                    {
                        case R.id.navigation_people:
                            Intent SaveConIntent=new Intent(MainActivity.this,SaveContact.class);
                            startActivity(SaveConIntent);
                        case R.id.navigation_msg:
                            Intent EmergencyMsgIntent=new Intent(MainActivity.this,SendEmergencyMessage.class);
                            startActivity(EmergencyMsgIntent);


                    }

                    return false;



                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarm = (LinearLayout) findViewById(R.id.alarm);
        fakecall=(LinearLayout)findViewById(R.id.fakecall);

BottomNavigationView navView=findViewById(R.id.nav_view);
navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        //FakeCall WORK
        fakecall.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, CallReciever.class);
        intent.setAction("call");
        sendBroadcast(intent);
    }
});


        //Alaram Work
        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyBroadcastReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 232, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (5000), pendingIntent);
                Toast.makeText(MainActivity.this, "Alarm will start with in five seconds", Toast.LENGTH_SHORT).show();
             //   Toast.makeText(MainActivity.this, "Alarm will start with in five seconds", Toast.LENGTH_SHORT).show();

            }
        });

        //Profile Work


    }
}