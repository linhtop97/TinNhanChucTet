package com.example.linhnb.project1zergitas.screen.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.linhnb.project1zergitas.R;
import com.example.linhnb.project1zergitas.databinding.ActivityMainBinding;
import com.example.linhnb.project1zergitas.utils.Constant;
import com.example.linhnb.project1zergitas.utils.navigator.Navigator;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private Navigator mNavigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mNavigator = new Navigator(this);
        MainFragment mainFragment = MainFragment.newInstance();
        mNavigator.addFragment(R.id.main_container, mainFragment, false, Navigator.NavigateAnim.NONE, mainFragment.getClass().getSimpleName());
        initPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constant.REQUEST_PERMISSTION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permision Write File is Granted", Toast.LENGTH_SHORT).show();
                }
            } else {
                mNavigator.showToast("Permission Declined");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    public void initPermission() {
        String[] listPermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //Permisson don't granted
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Register permission
                    ActivityCompat.requestPermissions(MainActivity.this, listPermission, Constant.REQUEST_PERMISSTION);
                }
                // Permisson don't granted and dont show dialog again.
                else {
                    //Register permission
                    ActivityCompat.requestPermissions(MainActivity.this, listPermission, Constant.REQUEST_PERMISSTION);
                }

            }
        }
    }
}
