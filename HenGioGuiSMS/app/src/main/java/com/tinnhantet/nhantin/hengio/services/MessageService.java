package com.tinnhantet.nhantin.hengio.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.database.sqlite.MessageDatabaseHelper;
import com.tinnhantet.nhantin.hengio.models.Contact;
import com.tinnhantet.nhantin.hengio.models.Message;
import com.tinnhantet.nhantin.hengio.receivers.DeliveredMessage;
import com.tinnhantet.nhantin.hengio.receivers.SentMessage;
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
        Log.i(TAG, "vào được đây: ");
        if (i != null) {
            Bundle bundle = i.getExtras();
            if (bundle != null) {
                Long id = bundle.getLong(Constant.EXTRA_ID);
                if (id != 0) {
                    Message message = MessageDatabaseHelper.getInstance(this).getMessageById(id);
                    if (message != null) {
                        Log.i(TAG, "đã vào được đây: ");
                        List<Contact> contacts = StringUtils.getAllContact(message.getListContact());
                        String SSms = message.getContent();
                        int size = contacts.size();
                        for (int j = 0; j < size; j++) {
                            Log.i(TAG, "vào được: ");
                            sendSMS(contacts.get(j).getPhone(), SSms);
                            Log.i(TAG, "gửi được: ");
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
                    }

                }
            }
        }

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void sendSMS(String phoneNumber, String message) {
        ArrayList<PendingIntent> sentPendingIntents = new ArrayList<>();
        ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<>();
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
