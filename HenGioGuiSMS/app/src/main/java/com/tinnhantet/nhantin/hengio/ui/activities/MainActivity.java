package com.tinnhantet.nhantin.hengio.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.adapters.MainPagerAdapter;
import com.tinnhantet.nhantin.hengio.databinding.ActivityMainBinding;
import com.tinnhantet.nhantin.hengio.listeners.DataCallBack;
import com.tinnhantet.nhantin.hengio.models.Contact;
import com.tinnhantet.nhantin.hengio.ui.dialogs.DialogRequestFragment;
import com.tinnhantet.nhantin.hengio.ui.dialogs.LoadingDialog;
import com.tinnhantet.nhantin.hengio.utils.Constant;
import com.tinnhantet.nhantin.hengio.utils.Navigator;
import com.tinnhantet.nhantin.hengio.utils.TabType;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TabLayout.BaseOnTabSelectedListener{
    public static final String[] PERMISSION_STRING = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS};
    private static final String REQUEST_DIALOG = "REQUEST_DIALOG";
    private ActivityMainBinding mBinding;
    private Navigator mNavigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        checkPermission();
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(PERMISSION_STRING, this) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(MainActivity.PERMISSION_STRING, Constant.REQUEST_PER_CODE);
            } else {
                //SHOW DATA
            }
        } else {
            //SHOW DATA
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
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        mBinding.viewPager.setAdapter(adapter);
        mBinding.viewPager.setCurrentItem(TabType.PENDING);
        mBinding.tablayout.addOnTabSelectedListener(this);
        mBinding.tablayout.setupWithViewPager(mBinding.viewPager);
        mBinding.tablayout.getTabAt(TabType.PENDING).setText(R.string.pending);
        mBinding.tablayout.getTabAt(TabType.SENT).setText(R.string.sent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constant.REQUEST_PER_CODE) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        boolean showRationale = shouldShowRequestPermissionRationale(permission);
                        if (!showRationale) {
                            //K CHO PHEP
                        } else if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission)) {
                            //KO CHO PHEP
                        } else if (Manifest.permission.READ_CONTACTS.equals(permission)) {
                            //KO CHO PHEP
                        }
                        //SHOW DIALOG BAT VAO
                        showDialogRequest();
                    }

                } else if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_PERMISSION_CODE) {
            showRequest();
        }

    }


    public void showRequest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(MainActivity.PERMISSION_STRING, this) != PackageManager.PERMISSION_GRANTED) {
                showDialogRequest();
            }
        }
    }

    private void showDialogRequest() {
        DialogRequestFragment f = DialogRequestFragment.getInstance();
        getSupportFragmentManager().beginTransaction().add(f, REQUEST_DIALOG).commit();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int pos = tab.getPosition();
        switch (pos) {
            case TabType.PENDING:
                break;
            case TabType.SENT:
                break;
        }
        mBinding.viewPager.setCurrentItem(pos);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}