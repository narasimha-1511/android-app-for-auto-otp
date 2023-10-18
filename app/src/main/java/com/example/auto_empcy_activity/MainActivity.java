package com.example.auto_empcy_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static  final  int MY_PERMISSION_REQUEST_RECEIVE_SMS = 0 ;
    EditText phone1 ;
    EditText phone2 ;
    EditText token;
    Button update ;
    Switch Lock;
    IntentFilter iFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText phone1 = findViewById(R.id.Number1);
        EditText phone2 = findViewById(R.id.Number2);
        EditText token = findViewById(R.id.tokenId);
        Button update = findViewById(R.id.Update);
        Switch Lock = findViewById(R.id.Lock);


         //checking if the permission is granted
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.RECEIVE_SMS}, MY_PERMISSION_REQUEST_RECEIVE_SMS);
        }


        //Setting on click listener on clicking the Update Button
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences("DEMO", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("PHONE1", phone1.getText().toString());
                editor.putString("PHONE2", phone2.getText().toString());
                editor.putString("TOKEN", token.getText().toString());
                editor.apply();

                Toast makeText = Toast.makeText(MainActivity.this, "This is REaltime", Toast.LENGTH_SHORT);
                makeText.show();

            }
        });
               // THis updates  the values as per the activity

        SharedPreferences get = getSharedPreferences("DEMO", MODE_PRIVATE);

        String phonenum1 = get.getString("PHONE1", "");
        String phonenum2 = get.getString("PHONE2", "");
        String tokenid = get.getString("TOKEN", "");

        phone1.setText(phonenum1);
        phone2.setText(phonenum2);
        token.setText(tokenid);


                }//This is the end of On create Instanced





    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode,String permission[],int[] grantResults){
        //will check the request code
            switch(requestCode){
                case MY_PERMISSION_REQUEST_RECEIVE_SMS:{
         // checking whether they grants results greater than 0 and equal to permission granted
                    Toast makeText;
                    if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //Thanking if Permission is granted
                        makeText = Toast.makeText(MainActivity.this, "hey!! Thank you for Granting the permission ", Toast.LENGTH_SHORT);
                    }
                    else{
                        makeText = Toast.makeText(MainActivity.this, "Well I can't do anything without Granting the permission ", Toast.LENGTH_SHORT);
                    }
                    makeText.show();
                }
            }


    }
}