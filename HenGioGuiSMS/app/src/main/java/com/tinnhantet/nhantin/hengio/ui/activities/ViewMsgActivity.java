package com.tinnhantet.nhantin.hengio.ui.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.view.View;

import com.bumptech.glide.Glide;
import com.tinnhantet.nhantin.hengio.Ads;
import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.adapters.PhoneNumberAdapter;
import com.tinnhantet.nhantin.hengio.database.sharedprf.SharedPrefsImpl;
import com.tinnhantet.nhantin.hengio.database.sqlite.MessageDatabaseHelper;
import com.tinnhantet.nhantin.hengio.databinding.ActivityViewMsgBinding;
import com.tinnhantet.nhantin.hengio.listeners.OnDataClickListener;
import com.tinnhantet.nhantin.hengio.models.Contact;
import com.tinnhantet.nhantin.hengio.models.Message;
import com.tinnhantet.nhantin.hengio.services.MessageService;
import com.tinnhantet.nhantin.hengio.ui.dialogs.ConfirmDeleteDialog;
import com.tinnhantet.nhantin.hengio.utils.Constant;
import com.tinnhantet.nhantin.hengio.utils.DateTimeUtil;
import com.tinnhantet.nhantin.hengio.utils.Navigator;
import com.tinnhantet.nhantin.hengio.utils.StringUtils;

import java.util.List;

public class ViewMsgActivity extends AppCompatActivity implements View.OnClickListener, OnDataClickListener {
    private static final int REQUEST_EDIT = 108;
    public static final String FORWARD = "FORWARD";
    private static final String DELETE_DIALOG = "DELETE_DIALOG";
    private ActivityViewMsgBinding mBinding;
    private Navigator mNavigator;
    private SharedPrefsImpl mSharedPrefs;
    private PhoneNumberAdapter mAdapter;
    private MessageDatabaseHelper mHelper;
    private Message mMessage;
    public static ViewMsgActivity mInstance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = this;
        initUI();
        initAction();
    }

    private void initAction() {
        mBinding.txtEdit.setOnClickListener(this);
        mBinding.btnDelete.setOnClickListener(this);
        mBinding.btnForward.setOnClickListener(this);
        mBinding.btnBack.setOnClickListener(this);
    }

    private void initUI() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_msg);
        Glide.with(this)
                .load(R.drawable.bg_main)
                .into(mBinding.imgBackground);
        mBinding.txtEdit.setText(Html.fromHtml(getString(R.string.edit)));
        mNavigator = new Navigator(this);
        mSharedPrefs = new SharedPrefsImpl(this);
        mHelper = MessageDatabaseHelper.getInstance(this);
        Bundle bundle = getIntent().getExtras();
        mMessage = bundle.getParcelable(Constant.EXTRA_MSG);
        boolean isEdit = bundle.getBoolean(Constant.EXTRA_IS_EDIT);
        if (!isEdit) {
            mBinding.txtEdit.setVisibility(View.GONE);
        }
        if (mMessage != null) {
            List<Contact> contacts = StringUtils.getAllContact(mMessage.getListContact());
            mAdapter = new PhoneNumberAdapter(this, contacts, false);
            mAdapter.setOnContactListener(this);
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,
                    StaggeredGridLayoutManager.VERTICAL);
            mBinding.rvNumbers.setLayoutManager(layoutManager);
            mBinding.rvNumbers.setAdapter(mAdapter);
            String content = mMessage.getContent();
            mBinding.txtTime.setText(DateTimeUtil.convertTimeToString(Long.valueOf(mMessage.getTime())));
            mBinding.edtContent.setText(content);
        }
        Ads.f(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.txt_edit:
                //start Edit Msg Activity
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constant.EXTRA_MSG, mMessage);
                mNavigator.startActivityForResult(AddMsgActivity.class, bundle, REQUEST_EDIT);
                break;
            case R.id.btn_forward:
                //Start Add Msg Activity
                Bundle fwBundle = new Bundle();
                Message message = new Message(mMessage.getPendingId(), mMessage.getContent(), mMessage.getTime(), "", true);
                fwBundle.putParcelable(Constant.EXTRA_MSG, message);
                Intent intent = new Intent();
                intent.setClass(this, AddMsgActivity.class);
                intent.setAction(FORWARD);
                intent.putExtras(fwBundle);
                mNavigator.startActivity(intent);

                break;
            case R.id.btn_delete:
                //delete this msg
                ConfirmDeleteDialog f = ConfirmDeleteDialog.getInstance();
                getSupportFragmentManager().beginTransaction().add(f, DELETE_DIALOG).commit();
                break;
        }
    }

    public void deleteMsg() {
        int id = mMessage.getPendingId();
        mHelper.deleteMsg(id);
        Intent i = new Intent(this, MessageService.class);
        PendingIntent pIntent = PendingIntent.getService(getApplicationContext(), id, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        aManager.cancel(pIntent);
        finish();
    }

    @Override
    public void onItemClick(Object data, int pos) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT && resultCode == RESULT_OK) {
            Bundle bundle = data.getBundleExtra(Constant.EXTRA_MSG);
            Message message = bundle.getParcelable(Constant.EXTRA_MSG);
            List<Contact> contacts = StringUtils.getAllContact(message.getListContact());
            mAdapter = new PhoneNumberAdapter(this, contacts, false);
            mAdapter.setOnContactListener(this);
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,
                    StaggeredGridLayoutManager.VERTICAL);
            mBinding.rvNumbers.setLayoutManager(layoutManager);
            mBinding.rvNumbers.setAdapter(mAdapter);
            String content = message.getContent();
            String dateTime[] = DateTimeUtil.separateTime(Long.valueOf(message.getTime()));
            mBinding.txtTime.setText("Thời gian : " + dateTime[0] + " giờ " + dateTime[1] + " phút " + "Ngày " + dateTime[2] + "/" + dateTime[3] + "/" + dateTime[4]);
            mBinding.edtContent.setText(content);
            mMessage = message;
        }
    }
}
