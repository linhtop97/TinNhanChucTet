package com.tinnhantet.nhantin.hengio.ui.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.database.sharedprf.SharedPrefsImpl;
import com.tinnhantet.nhantin.hengio.databinding.ActivitySplashBinding;
import com.tinnhantet.nhantin.hengio.listeners.DataCallBack;
import com.tinnhantet.nhantin.hengio.models.Contact;
import com.tinnhantet.nhantin.hengio.models.ContactHelper;
import com.tinnhantet.nhantin.hengio.ui.dialogs.DialogRequestFragment;
import com.tinnhantet.nhantin.hengio.utils.Constant;
import com.tinnhantet.nhantin.hengio.utils.Navigator;

import java.util.List;

public class SplashActivity extends AppCompatActivity implements DataCallBack<List<Contact>> {
    public static final String[] PERMISSION_STRING = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS};
    private static final String REQUEST_DIALOG = "REQUEST_DIALOG";
    private static final int REQUEST_PERMISSION_CODE = 1012;
    private ActivitySplashBinding mBinding;
    private Navigator mNavigator;
    private SharedPrefsImpl mSharedPrefs;

    private BroadcastReceiver mReQuestReceiver;
    IntentFilter filter = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filter.addAction("RequestPMS");
        mReQuestReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("RequestPMS")) {
                    Intent intent1 = new Intent();
                    intent1.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri2 = Uri.fromParts("package", getPackageName(), null);
                    intent1.setData(uri2);
                    startActivityForResult(intent1, REQUEST_PERMISSION_CODE);
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mReQuestReceiver, filter);
        initUI();
        checkPermission();
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(PERMISSION_STRING, this) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(SplashActivity.PERMISSION_STRING, Constant.REQUEST_PER_CODE);
            } else {
                //SHOW DATA
                loadContacts();
            }
        } else {
            //SHOW DATA
            loadContacts();
        }
    }

    public static int checkPermission(String[] permissions, Context context) {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (String permission : permissions) {
            permissionCheck += ContextCompat.checkSelfPermission(context, permission);
        }
        return permissionCheck;
    }

    private void initUI() {
        mNavigator = new Navigator(this);
        mSharedPrefs = new SharedPrefsImpl(this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == Constant.REQUEST_PER_CODE) {
            int check = 0;
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        boolean showRationale = shouldShowRequestPermissionRationale(permission);
                        if (!showRationale) {
                            showDialogRequest();
                        } else if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission)) {
                            checkPermission();
                        } else if (Manifest.permission.READ_CONTACTS.equals(permission)) {
                            checkPermission();
                        } else if (Manifest.permission.SEND_SMS.equals(permission)) {
                            checkPermission();
                        }
                    }

                } else if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission) || Manifest.permission.READ_CONTACTS.equals(permission) || Manifest.permission.SEND_SMS.equals(permission)) {
                        check++;
                    }
                    if (check == PERMISSION_STRING.length) {
                        loadContacts();
                    }
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            showRequest();
        }

    }

    private void loadContacts() {
        int SPLASH_DISPLAY_LENGTH = 2000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mNavigator.startActivity(MainActivity.class, Navigator.NavigateAnim.NONE);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
        ContactHelper.getAllContact(this, this);

    }

    public void showRequest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentByTag(REQUEST_DIALOG);
            if (fragment != null) {
                DialogRequestFragment f = (DialogRequestFragment) fragment;
                f.dismiss();
            }
            if (checkPermission(SplashActivity.PERMISSION_STRING, this) == PackageManager.PERMISSION_GRANTED) {
                loadContacts();
            } else {
                showDialogRequest();
            }
        }
    }

    private void showDialogRequest() {
        DialogRequestFragment f = DialogRequestFragment.getInstance();
        getSupportFragmentManager().beginTransaction().add(f, REQUEST_DIALOG).commit();
    }

    @Override
    public void onDataSuccess(List<Contact> data) {
        mSharedPrefs.putListContact(data);
    }

    @Override
    public void onDataFailed(String msg) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReQuestReceiver);
    }
}