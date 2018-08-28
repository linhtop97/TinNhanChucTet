package com.example.nttungg.passwordgenarator.screens.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nttungg.passwordgenarator.R;
import com.example.nttungg.passwordgenarator.models.UserData;
import com.example.nttungg.passwordgenarator.screens.homescreen.HomeActivity;
import com.example.nttungg.passwordgenarator.screens.listscreen.ListPassActivity;
import com.example.nttungg.passwordgenarator.utils.Constant;

/**
 * Created by nttungg on 6/20/18.
 */

public class WindowService extends Service implements View.OnClickListener {
    public static final String ACTION_OPEN_WINDOW = "ACTION_OPEN_WINDOW";
    private static final int NOTIFY_ID = 1;

    private PendingIntent pendingIntentOpenApp;
    private NotificationCompat.Builder mBuilder;

    private WindowManager windowManager;
    private WindowManager.LayoutParams mParams;
    private View mFloatingView;
    private WindowService mWindowService;

    private TextView mTextviewAccount;
    private TextView mTextviewPass;
    private TextView mTextViewTitle;
    private ImageView mTextviewCancel;
    private ImageView mImageViewCopPass;
    private ImageView mImageViewCopAcc;
    private TextView mImageViewTitleAcc;
    private TextView mImageViewTitlePass;
    private static UserData mUserData;
    private  View collapsedView;
    private  View expandedView;
    private BroadcastReceiver mReceiver;

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
        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction("remove_window");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                windowManager.removeView(mFloatingView);
                Constant.is_show = false;
                stopSelf();
            }
        };
        this.registerReceiver(this.mReceiver, theFilter);
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(this.mReceiver);
    }

    private void initView() {
        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);

        mFloatingView = LayoutInflater.from(this).inflate(R.layout.windowmanager, null);
        collapsedView = mFloatingView.findViewById(R.id.collapse_view);
        expandedView = mFloatingView.findViewById(R.id.expanded_container);

        mTextviewCancel = mFloatingView.findViewById(R.id.text_cancel);
        mTextviewAccount = mFloatingView.findViewById(R.id.text_account);
        mTextviewPass = mFloatingView.findViewById(R.id.text_password);
        mTextViewTitle = mFloatingView.findViewById(R.id.window_title);
        mImageViewTitleAcc = mFloatingView.findViewById(R.id.window_title_acc);
        mImageViewTitlePass = mFloatingView.findViewById(R.id.window_title_pass);

        mImageViewCopPass = mFloatingView.findViewById(R.id.imageView_coppass);
        mImageViewCopAcc = mFloatingView.findViewById(R.id.imageView_copacc);

        Typeface mtypeFace = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/utm_avo.ttf");
        // set TypeFace to the TextView or Edittext
        mTextViewTitle.setTypeface(mtypeFace);
        mTextviewAccount.setTypeface(mtypeFace);
        mTextviewPass.setTypeface(mtypeFace);
        mImageViewTitleAcc.setTypeface(mtypeFace);
        mImageViewTitlePass.setTypeface(mtypeFace);

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
                Constant.is_show = false;
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
        handleIntent(intent);
        showForegroundNotification("Viewing mode is running");
        return START_NOT_STICKY;
    }

    private void handleIntent(Intent intent) {
        Constant.is_show = true;
        mUserData = (UserData) intent.getExtras().get("key_user_data");
        if (mUserData.equals("")){
            mImageViewCopAcc.setVisibility(View.GONE);
        }
        mTextviewAccount.setText(mUserData.getAccount());
        mTextviewPass.setText(mUserData.getPassword());
        mTextViewTitle.setText(mUserData.getTitle());
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

    private static final int NOTIFICATION_ID = 1;
    private static final String NOTIFICATION_CHANNEL_ID = "window_channel";

    private void showForegroundNotification(String contentText) {
        // Create intent that will bring our app to the front, as if it was tapped in the app
        // launcher
        Intent showTaskIntent = new Intent(getApplicationContext(), ListPassActivity.class);

        showTaskIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                showTaskIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentText(contentText)
                .setSmallIcon(R.drawable.icon_launcher_small)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(contentIntent)
                .build();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(NOTIFICATION_ID, notification);
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        Intent showTaskIntent = new Intent(getApplicationContext(), ListPassActivity.class);

        showTaskIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                showTaskIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentText("Viewing mode is running")
                .setSmallIcon(R.drawable.icon_launcher_small)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(contentIntent)
                .build();
        startForeground(NOTIFICATION_ID, notification);
    }

}
