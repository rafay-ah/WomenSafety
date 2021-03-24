package com.example.womensaftey;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;


public class SaveContact extends AppCompatActivity {
private Toolbar mToolbar;
EditText contact1,contact2,contact3;
CircleImageView imageView1,imageView2,imageView3;
    Button save;
    int flow = 0;
    public static int getContact = 2;
   int globalContact = 0;
    boolean saveContactd = false;

    // Session Manager Class
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_contact);
        //Getting Ids
        contact1=findViewById(R.id.Contac1);
        contact2=findViewById(R.id.Contact2);
        contact3=findViewById(R.id.Contact3);
        imageView1=findViewById(R.id.circleImageView1);
        imageView2=findViewById(R.id.circleImageView2);
        imageView3=findViewById(R.id.circleImageView3);
        save=findViewById(R.id.save);
        mToolbar=findViewById(R.id.Save_Contact);

        session = new SessionManager(getApplicationContext());


        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                saveContactd = true;
                flow = 2;
                if (Build.VERSION.SDK_INT < 23) {

                    saveContactData();

                } else
                    // for alove L version we need to get permission at the door to get throught that location ...

                      checkpermission();
                     if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {  // checkSelfPermission is a method avail in 23 api ..without if condition of (SDK_INT < 23 ) you cant implement it..

                        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS}, getContact);

                    } else {

                        saveContactData();
                    }
            }
        });

        //Set ToolBar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("SaveContact");

        if (Build.VERSION.SDK_INT<23)
        {

        }
        else{
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED|| checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
            {
                 requestPermissions(new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS},getContact);
            }else{


            }
        }


        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalContact = 101;
                flow = 1;
                if (Build.VERSION.SDK_INT < 23) {openContact();}
                else{
                  checkpermission();
                }


            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  openContact1(102);
                globalContact = 102;

                flow = 1;
                if (Build.VERSION.SDK_INT < 23) {

                    openContact();

                } else

            checkpermission();

            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  openContact1(103);
                globalContact = 103;
                flow = 1;
             if (Build.VERSION.SDK_INT < 23) {openContact();} else {checkpermission();}}
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == (101) && resultCode == RESULT_OK && null != data) {
            Uri contactData = data.getData();
            //String[] projection = { Phone.NUMBER, Phone.DISPLAY_NAME };

            Cursor c = managedQuery(contactData, null, null, null, null);
            if (c.moveToFirst()) {
                String id =
                        c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                String hasPhone =
                        c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                if (hasPhone.equalsIgnoreCase("1")) {
                    Cursor phones = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                            null, null);
                    phones.moveToFirst();
                    String phn_no1 = phones.getString(phones.getColumnIndex("data1"));
                    contact1.setText(phn_no1);

                }
            }
        }
        else if (requestCode == (102) && resultCode == RESULT_OK && null != data) {
            Uri contactData = data.getData();
            //String[] projection = { Phone.NUMBER, Phone.DISPLAY_NAME };

            Cursor c = managedQuery(contactData, null, null, null, null);
            if (c.moveToFirst()) {
                String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
              String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
             if (hasPhone.equalsIgnoreCase("1")) {
                    Cursor phones = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                            null, null);
                    phones.moveToFirst();
                  String phn_no2 = phones.getString(phones.getColumnIndex("data1"));
                  contact2.setText(phn_no2);

                }
            }
        }else //(requestCode == (101) && resultCode == RESULT_OK && null != data)
        {
            try {

                Uri contactData = data.getData();
                //String[] projection = { Phone.NUMBER, Phone.DISPLAY_NAME };

                Cursor c = managedQuery(contactData, null, null, null, null);
                if (c.moveToFirst()) {
                    String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                    String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    if (hasPhone.equalsIgnoreCase("1")) {
                        Cursor phones = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                null, null);
                        phones.moveToFirst();
                       String phn_no3 = phones.getString(phones.getColumnIndex("data1"));
                      contact3.setText(phn_no3);
                    }
                }

            } catch (Exception e) {

            }

        }




    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkpermission() {
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS}, getContact);


        }
        else{
            openContact();

        }
    }
//Open Contact Method
    private void openContact() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, globalContact);
        }
    }

//Permission

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == getContact) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (flow == 0) {

                } else if (flow == 1) {
                    openContact();
                } else if (flow == 2) {
                  saveContactData();
                }


            }


        }
    }

    private void saveContactData() {
        String cn1 = contact1.getText().toString();
        String cn2 = contact2.getText().toString();
        String cn3 = contact3.getText().toString();

      //  session.createContactSession(contact1.getText().toString(), contact2.getText().toString(), contact3.getText().toString());
        // Check if username, password is filled
        if (cn1.trim().length() > 0 && cn2.trim().length() > 0 && cn3.trim().length() > 0) {
//             session.createLoginSession();
            session.createContactSession(contact1.getText().toString(), contact2.getText().toString(), contact3.getText().toString());
            Intent i = new Intent(getApplicationContext(), SendEmergencyMessage.class);
            startActivity(i);
            Toast.makeText(this, "Contacts Saved Successfully", Toast.LENGTH_LONG).show();
            finish();
        } else {
            // username / password doesn't match
            Toast.makeText(getApplicationContext(), "Please Add all 3 Contacts", Toast.LENGTH_LONG).show();

        }

    }
}