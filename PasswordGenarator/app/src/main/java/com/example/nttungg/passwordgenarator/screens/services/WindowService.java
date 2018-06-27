package com.example.nttungg.passwordgenarator.screens.services;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nttungg.passwordgenarator.R;
import com.example.nttungg.passwordgenarator.models.UserData;
import com.example.nttungg.passwordgenarator.screens.listscreen.ListPassActivity;

/**
 * Created by nttungg on 6/20/18.
 */

public class WindowService extends Service implements View.OnClickListener {
    private WindowManager windowManager;
    private WindowManager.LayoutParams mParams;
    private View mFloatingView;

    private TextView mTextviewAccount;
    private TextView mTextviewPass;
    private TextView mTextviewCancel;
    private ImageView mImageViewCopPass;
    private ImageView mImageViewCopAcc;
    private static UserData mUserData;
    private  View collapsedView;
    private  View expandedView;

    public static Intent getServiceIntent(Context context, UserData userData) {
        Intent intent = new Intent(context, WindowService.class);
        intent.putExtra("key_user_data", (Parcelable) userData);
        return intent;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initView();
    }

    private void initView() {
        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);

        mFloatingView = LayoutInflater.from(this).inflate(R.layout.windowmanager, null);
        collapsedView = mFloatingView.findViewById(R.id.collapse_view);
        expandedView = mFloatingView.findViewById(R.id.expanded_container);

        mTextviewCancel = mFloatingView.findViewById(R.id.text_cancel);
        mTextviewAccount = mFloatingView.findViewById(R.id.text_account);
        mTextviewPass = mFloatingView.findViewById(R.id.text_password);
        mImageViewCopPass = mFloatingView.findViewById(R.id.imageView_coppass);
        mImageViewCopAcc = mFloatingView.findViewById(R.id.imageView_copacc);

        mParams = new WindowManager.LayoutParams();
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.gravity = Gravity.CENTER;

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mParams.gravity = Gravity.CENTER;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(mFloatingView,mParams);

        mTextviewCancel.setOnClickListener(this);
        mImageViewCopAcc.setOnClickListener(this);
        mImageViewCopPass.setOnClickListener(this);

        ImageView closeButtonCollapsed =mFloatingView.findViewById(R.id.close_btn);
        closeButtonCollapsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                windowManager.removeView(mFloatingView);
                stopSelf();
            }
        });

        mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //remember the initial position.
                        initialX = mParams.x;
                        initialY = mParams.y;
                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);
                        if (Xdiff < 10 && Ydiff < 10) {
                            if (isViewCollapsed()) {
                                collapsedView.setVisibility(View.GONE);
                                expandedView.setVisibility(View.VISIBLE);
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        mParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                        mParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                        //Update the layout with new X & Y coordinate
                        windowManager.updateViewLayout(mFloatingView, mParams);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mUserData = (UserData) intent.getExtras().get("key_user_data");
        if (mUserData.equals("")){
            mImageViewCopAcc.setVisibility(View.GONE);
        }
        mTextviewAccount.setText(mUserData.getAccount());
        mTextviewPass.setText(mUserData.getPassword());
        return START_NOT_STICKY;
    }


    private boolean isViewCollapsed() {
        return mFloatingView == null || mFloatingView.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }

    private void copyText(String copyText){
        ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("copy_text",copyText);
        clipboard.setPrimaryClip(clip);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_cancel:
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
                break;
            case R.id.imageView_copacc:
                copyText(mUserData.getAccount());
                Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_LONG).show();
                break;
            case R.id.imageView_coppass:
                copyText(mUserData.getPassword());
                Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_LONG).show();
                break;
        }
    }


}
