package com.tinnhantet.nhantin.hengio.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.telephony.SmsManager;

import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.database.sharedprf.SharedPrefsImpl;
import com.tinnhantet.nhantin.hengio.database.sqlite.MessageDatabaseHelper;
import com.tinnhantet.nhantin.hengio.models.Contact;
import com.tinnhantet.nhantin.hengio.models.Message;
import com.tinnhantet.nhantin.hengio.ui.activities.MainActivity;
import com.tinnhantet.nhantin.hengio.ui.fragments.PendingFragment;
import com.tinnhantet.nhantin.hengio.ui.fragments.SentFragment;
import com.tinnhantet.nhantin.hengio.utils.Constant;

import java.util.List;

public class MessageService extends Service {
    private static final String TAG = "MessageService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent i, int flags, int startId) {
        Message message = i.getParcelableExtra(Constant.EXTRA_MSG);
        if (message != null) {
            List<Contact> contacts = new SharedPrefsImpl(this).getAllContact(message.getListContact());
            String SSms = message.getContent();
            int size = contacts.size();
            for (int j = 0; j < size; j++) {
                String SPhone = contacts.get(j).getPhone();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(SPhone, null, SSms, null, null);
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
}
