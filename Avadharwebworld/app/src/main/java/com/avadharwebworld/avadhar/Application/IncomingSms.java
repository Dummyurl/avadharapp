package com.avadharwebworld.avadhar.Application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.avadharwebworld.avadhar.Activity.ConfirmOtp;

/**
 * Created by Vishnu on 21-09-2016.
 */
public class IncomingSms extends BroadcastReceiver {
    String phoneNumber;
     String senderNum ;
    String message;
    final SmsManager sms = SmsManager.getDefault();
    public void onReceive(Context context, Intent intent) {
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);


                    // Show Alert
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context,
                            "senderNum: "+ senderNum + ", message: " + message, duration);
                    toast.show();
                    try {
                        if (senderNum.contains("WAYSMS")) {
                            String otp = message.substring(12, 18);
                            ConfirmOtp Sms = new ConfirmOtp();
                            Sms.recivedSms(otp);
                            Toast.makeText(context, otp, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){

                    }

                } // end for loop
            } // bundle is null


        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);


        }
}
}
