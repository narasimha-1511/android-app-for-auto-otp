package com.example.auto_empcy_activity;


import static android.content.Context.MODE_PRIVATE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SmsReceiver extends BroadcastReceiver {
    private static final String aTAG = "MyBroadcastReceiver";
    private String phonenum1, phonenum2, tokenid;

    SharedPreferences sharedPreferences;

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SmsBroadcastReceiver";
    String msg, phoneNo = "";
    int simSlot;
    String SIM_SLOT_NAMES[] = {"extra_asus_dial_use_dualsim", "com.android.phone.extra.slot", "slot", "simslot", "sim_slot", "subscription", "Subscription", "phone",
            "com.android.phone.DialingMode",
            "simSlot",
            "slot_id",
            "simId",
            "simnum",
            "phone_type",
            "slotId",
            "slotIdx"
    };

    @Override
    public void onReceive(Context context, Intent intent) {

        //retrieves the general action to be performed and display on log
        Log.i(TAG, "Intent Received" + intent.getAction());

        //getting the slot of the sim
        for (int i = 0; i < SIM_SLOT_NAMES.length; i++) {
            if (intent.getIntExtra(SIM_SLOT_NAMES[i], -1) != -1) {
                simSlot = intent.getIntExtra(SIM_SLOT_NAMES[i], -1);
            }
        }


        if (intent.getAction() == SMS_RECEIVED) {
            // GETS THE DATA FORM THE EXTENDED CONTENT
            Bundle dataBundle = intent.getExtras();
            if (dataBundle != null) {

                //Creating PDU(Protocol Data Unit) Object which is a protocol for transferring message
                Object[] mypdu = (Object[]) dataBundle.get("pdus");
                final SmsMessage[] message = new SmsMessage[mypdu.length];

                for (int i = 0; i < mypdu.length; i++) {

                    //for Build Versions >= API 23 LEVEL
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String format = dataBundle.getString("format");
                        //From PDU we get all object and SmsMessage Object using following line of code
                        message[i] = SmsMessage.createFromPdu((byte[]) mypdu[i], format);
                    } else {
                        //less Than API 23
                        message[i] = SmsMessage.createFromPdu((byte[]) mypdu[i]);
                    }
                    // This is the Message
                    msg = message[i].getMessageBody();
                    phoneNo = message[i].getOriginatingAddress();
                }

            }
//            Toast.makeText(context,"Message: "+msg+"\n Number : "+phoneNo+"\n Sim : "+simSlot,Toast.LENGTH_SHORT).show();
//            Toast.makeText(context,"Message: "+msg+"\n Sim : "+simSlot,Toast.LENGTH_SHORT).show();

        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("DEMO",MODE_PRIVATE);
        phonenum1 = sharedPreferences.getString("PHONE1","");
        phonenum2 = sharedPreferences.getString("PHONE2","");
        tokenid = sharedPreferences.getString("TOKEN","");

        String ph=phonenum1;
        if(simSlot==1){
         ph = phonenum2;
        }



        //This is Small Code To Grab The 6-Digit COde From The message ---------
        if(msg.contains("Scaler")&&msg.contains("OTP")){
          for(int i=0;i<msg.length();i++){
              //Iterating Over Loop Till We Get A Number
              if(msg.charAt(i)>='0'&&msg.charAt(i)<='9'){
                  //Now Checking If the NUmber is Otp and Storing it
                  boolean check=true;
                  for(int j=i;j<i+5;j++){
                      if(msg.charAt(j)>='0'&&msg.charAt(j)<='9'){
                          //Do Nothing
                      }else{
                          check=false;
                          break;
                      }
                  }
                  if(check==true){
                      msg = msg.substring(i,i+6);
                      break;
                  }
              }
          }
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(ph+'s');

        myRef.setValue(msg);

        Toast.makeText(context,simSlot+"Message: "+msg+"\n Sim : "+simSlot+phonenum1,Toast.LENGTH_SHORT).show();



    } // donot close this is th end of sms reciver


}


