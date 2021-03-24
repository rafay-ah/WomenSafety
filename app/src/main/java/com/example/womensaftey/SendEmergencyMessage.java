package com.example.womensaftey;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.TaskInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.CursorJoiner;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SendEmergencyMessage extends AppCompatActivity {
    Toolbar mToolbar;
    SmsManager sm;
    TextView cont1, cont2, cont3, tv4;
    // Session Manager Class
    SessionManager session;
    FloatingActionButton fab;
    public static final String DEFAULT = "N/A";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private LocationRequest locationRequest;
    public static final int REQUEST_CHECK_SETTING=1001;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_emergency_message);
        mToolbar = findViewById(R.id.Send_Msg);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Send Emergency Message");
        fab=findViewById(R.id.floatbtn);





        //Asking location permission



        locationRequest=LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);


        LocationSettingsRequest.Builder builder=new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        Task<LocationSettingsResponse> result= LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
    @Override
    public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
        try {
            task.getResult(ApiException.class);
        } catch (ApiException e) {
            switch (e.getStatusCode()){
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                    try {
                        ResolvableApiException resolvableApiException= (ResolvableApiException) e;
                        resolvableApiException.startResolutionForResult(SendEmergencyMessage.this,REQUEST_CHECK_SETTING);
                    } catch (IntentSender.SendIntentException ex) {
                     //   ex.printStackTrace();
                    }break;
                case  LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    break;



            }
        }
    }
});



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           /*     Intent SaveConIntent=new Intent(SendEmergencyMessage.this,SaveContact.class);
                startActivity(SaveConIntent);*/
                session.logoutUser();
            }
        });





        cont1 = findViewById(R.id.cont1);
        cont2 = findViewById(R.id.cont2);
        cont3 = findViewById(R.id.cont3);
        // Session Manager
        session = new SessionManager(getApplicationContext());
        session.checkStatusOfContactsAdd();

        HashMap<String, String> user = session.getUserDetails();
        String number1 = user.get(SessionManager.CONTACT1);
        // name
        String number2 = user.get(SessionManager.CONTACT2);

        // email
        String number3 = user.get(SessionManager.CONTACT3);
        // displaying user data
        cont1.setText(number1);
        cont2.setText(number2);
        cont3.setText(number3);

        GPSTracker mGPS = new GPSTracker(this);
        tv4 = (TextView) findViewById(R.id.texts);
        if (mGPS.canGetLocation) {
            mGPS.getLocation();
            tv4.setText(mGPS.getLatitude() + "," + mGPS.getLongitude());

        } else {
            tv4.setText("Unabletofind");
//            System.out.println("Unable");
        }

        sm = SmsManager.getDefault();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQUEST_CHECK_SETTING){

            switch (resultCode){
                case Activity.RESULT_OK:
                    Toast.makeText(this, "Gps is Turned On", Toast.LENGTH_SHORT).show();
                    break;
                case  Activity.RESULT_CANCELED:
                    Toast.makeText(this, "Gps is required to be turned on", Toast.LENGTH_SHORT).show();
                    break;

            }

        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        int permissionLocation = ContextCompat.checkSelfPermission(SendEmergencyMessage.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
           // getMyLocation();
            save=findViewById(R.id.save);
            save.setVisibility(View.VISIBLE);
        }
    }



    public void send(View view) {
         sm.sendTextMessage(cont1.getText().toString(), null, "I'm in danger..My current location is http://maps.google.com/?q=" + tv4.getText().toString(), null, null);
        sm.sendTextMessage(cont2.getText().toString(), null, "I'm in danger..My current location is http://maps.google.com/?q=" + tv4.getText().toString(), null, null);
        sm.sendTextMessage(cont3.getText().toString(), null, "I'm in danger..My current location is http://maps.google.com/?q=" + tv4.getText().toString(), null, null);
        Toast.makeText(this, "message sent", Toast.LENGTH_LONG).show();

    }

}