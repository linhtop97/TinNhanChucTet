package com.tinnhantet.nhantin.hengio.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.database.sqlite.MessageDatabaseHelper;
import com.tinnhantet.nhantin.hengio.models.Contact;
import com.tinnhantet.nhantin.hengio.models.Message;
import com.tinnhantet.nhantin.hengio.ui.activities.MainActivity;
import com.tinnhantet.nhantin.hengio.ui.fragments.PendingFragment;
import com.tinnhantet.nhantin.hengio.ui.fragments.SentFragment;
import com.tinnhantet.nhantin.hengio.utils.Constant;
import com.tinnhantet.nhantin.hengio.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MessageService extends Service {
    private static final String TAG = "MessageService";

    String SMS_SENT = "SMS_SENT";
    String SMS_DELIVERED = "SMS_DELIVERED";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent i, int flags, int startId) {
        Message message = i.getParcelableExtra(Constant.EXTRA_MSG);
        if (message != null) {
            List<Contact> contacts = StringUtils.getAllContact(message.getListContact());
            String SSms = message.getContent();
            //
            // Get the default instance of SmsManager
//            SmsManager smsManager = SmsManager.getDefault();
//            ArrayList<String> phoneNumber = new ArrayList<>();
//            for (int j = 0; j < contacts.size(); j++) {
//                phoneNumber.add(contacts.get(j).getPhone());
//            }
//            String smsBody = SSms;
//
//
//            PendingIntent sentPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_SENT), 0);
//            PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_DELIVERED), 0);
//
//            ArrayList<String> smsBodyParts = smsManager.divideMessage(smsBody);
//            ArrayList<PendingIntent> sentPendingIntents = new ArrayList<>();
//            ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<>();
//
//            for (int k = 0; k < smsBodyParts.size(); k++) {
//                sentPendingIntents.add(sentPendingIntent);
//                deliveredPendingIntents.add(deliveredPendingIntent);
//            }
//
//// For when the SMS has been sent
//            registerReceiver(new BroadcastReceiver() {
//                @Override
//                public void onReceive(Context context, Intent intent) {
//                    switch (getResultCode()) {
//                        case Activity.RESULT_OK:
//                            Log.d(TAG, "SMS sent successfully");
//                            break;
//                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                            Log.d(TAG, "Generic failure cause");
//                            break;
//                        case SmsManager.RESULT_ERROR_NO_SERVICE:
//                            Log.d(TAG, "Service is currently unavailable");
//                            break;
//                        case SmsManager.RESULT_ERROR_NULL_PDU:
//                            Log.d(TAG, "No pdu provided");
//                            break;
//                        case SmsManager.RESULT_ERROR_RADIO_OFF:
//                            Log.d(TAG, "Radio was explicitly turned off");
//                            break;
//                    }
//                }
//            }, new IntentFilter(SMS_SENT));
//
//// For when the SMS has been delivered
//            registerReceiver(new BroadcastReceiver() {
//                @Override
//                public void onReceive(Context context, Intent intent) {
//                    switch (getResultCode()) {
//                        case Activity.RESULT_OK:
//                            Log.d(TAG, "SMS delivered");
//                            break;
//                        case Activity.RESULT_CANCELED:
//                            Log.d(TAG, "SMS not delivered");
//                              break;
//                    }
//                }
//            }, new IntentFilter(SMS_DELIVERED));
//
//// Send a text based SMS
//            for (int j = 0; j < phoneNumber.size(); j++) {
//                smsManager.sendMultipartTextMessage(phoneNumber.get(j), null, smsBodyParts, sentPendingIntents, deliveredPendingIntents);
//                try {
//                    // add a 3 second time delay before sending the next one
//                    // the sleep() method throws an InterruptedException
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//
//                }
//            }


            //
            int size = contacts.size();
            for (int j = 0; j < size; j++) {
                sendSMS(contacts.get(j).getPhone(), SSms);
                try {
                    Thread.sleep(1000 * size);
                } catch (InterruptedException e) {

                }
            }
            MessageDatabaseHelper helper = MessageDatabaseHelper.getInstance(this);
            helper.removeMsgToSent(message.getPendingId());
            if (MainActivity.active) {
                FragmentManager fragmentManager = MainActivity.sInstance.getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + 0);
                if (fragment != null) {
                    PendingFragment pendingFragment = (PendingFragment) fragment;
                    pendingFragment.onStart();
                }
                Fragment fragment2 = fragmentManager.findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + 1);
                if (fragment2 != null) {
                    SentFragment sentFragment = (SentFragment) fragment2;
                    sentFragment.onStart();
                }
            }
            stopSelf();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void sendSMS(String phoneNumber, String message) {
        ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(this, SentMessage.class), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(this, DeliveredMessage.class), 0);
        try {
            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> mSMSMessage = sms.divideMessage(message);
            for (int i = 0; i < mSMSMessage.size(); i++) {
                sentPendingIntents.add(i, sentPI);
                deliveredPendingIntents.add(i, deliveredPI);
            }
            sms.sendMultipartTextMessage(phoneNumber, null, mSMSMessage,
                    sentPendingIntents, deliveredPendingIntents);

        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(getBaseContext(), "SMS sending failed...", Toast.LENGTH_SHORT).show();
        }

    }
}
