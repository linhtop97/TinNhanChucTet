package com.tinnhantet.nhantin.hengio.ui.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.adapters.MessageScheduleAdapter;
import com.tinnhantet.nhantin.hengio.database.sqlite.MessageDatabaseHelper;
import com.tinnhantet.nhantin.hengio.databinding.FragmentSentBinding;
import com.tinnhantet.nhantin.hengio.listeners.OnDataClickListener;
import com.tinnhantet.nhantin.hengio.listeners.OnItemLongClickListener;
import com.tinnhantet.nhantin.hengio.models.Message;
import com.tinnhantet.nhantin.hengio.services.MessageService;
import com.tinnhantet.nhantin.hengio.ui.activities.MainActivity;
import com.tinnhantet.nhantin.hengio.ui.activities.ViewMsgActivity;
import com.tinnhantet.nhantin.hengio.ui.dialogs.ConfirmDeleteAllDialog;
import com.tinnhantet.nhantin.hengio.utils.Constant;
import com.tinnhantet.nhantin.hengio.utils.Navigator;

import java.util.List;

import static android.content.Context.ALARM_SERVICE;

public class SentFragment extends Fragment implements View.OnClickListener, OnDataClickListener<Message>, OnItemLongClickListener<String> {
    private static final String DELETE_ALL_DIALOG = "DELETE_ALL_DIALOG";
    private FragmentSentBinding mBinding;
    private MainActivity mMainActivity;
    private Navigator mNavigator;
    private MessageScheduleAdapter mAdapter;
    private List<Message> mMessages;
    private MessageDatabaseHelper helper;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean mIsSelectAll = false;

    public static SentFragment newInstance() {
        SentFragment fragment = new SentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sent, container, false);
        initUI();
        initAction();
        return mBinding.getRoot();
    }

    private void initAction() {
        mBinding.btnDelete.setOnClickListener(this);
        mBinding.btnCancel.setOnClickListener(this);
        mBinding.btnSelectAll.setOnClickListener(this);
    }

    private void initUI() {
        mNavigator = new Navigator(mMainActivity);
        mLinearLayoutManager = new LinearLayoutManager(mMainActivity);
        helper = MessageDatabaseHelper.getInstance(mMainActivity);
        mMessages = helper.getAllMsgSent();
        showMessageNormal(mMessages);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                mIsSelectAll = false;
                mBinding.btnSelectAll.setText(R.string.select_all);
                showMessageNormal(helper.getAllMsgSent());
                break;
            case R.id.btn_delete:
                List<Message> messages = ((MessageScheduleAdapter) mBinding.recycleView.getAdapter()).getMessages();
                int size = messages.size();
                boolean check = false;
                for (int i = 0; i < size; i++) {
                    if (messages.get(i).getSelected()) {
                        check = true;
                        break;
                    }
                }
                if (check) {
                    ConfirmDeleteAllDialog f = ConfirmDeleteAllDialog.getInstance();
                    getChildFragmentManager().beginTransaction().add(f, DELETE_ALL_DIALOG).commit();
                } else {
                    mNavigator.showToast(R.string.havent_chosse);
                }
                break;

            case R.id.btn_select_all:
                if (!mIsSelectAll) {
                    mAdapter.setSelectedAll();
                    mIsSelectAll = true;
                    mBinding.btnSelectAll.setText(R.string.un_select_all);
                } else {
                    mAdapter.removeSelectedAll();
                    mIsSelectAll = false;
                    mBinding.btnSelectAll.setText(R.string.select_all);
                }


                break;
        }
    }

    public void deleteAllSelected() {
        List<Message> messages = ((MessageScheduleAdapter) mBinding.recycleView.getAdapter()).getMessages();
        MessageDatabaseHelper databaseHelper = MessageDatabaseHelper.getInstance(mMainActivity);
        int size = messages.size();
        for (int i = 0; i < size; i++) {
            Message message = messages.get(i);
            if (message.getSelected()) {
                int id = message.getPendingId();
                databaseHelper.deleteMsg(id);
                Intent z = new Intent(mMainActivity, MessageService.class);
                PendingIntent pIntent = PendingIntent.getService(mMainActivity, id, z, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager aManager = (AlarmManager) mMainActivity.getSystemService(ALARM_SERVICE);
                aManager.cancel(pIntent);
            }
        }
        showMessageNormal(MessageDatabaseHelper.getInstance(mMainActivity).getAllMsgSent());
        mIsSelectAll = false;
        mBinding.btnSelectAll.setText(R.string.select_all);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.setMessages(helper.getAllMsgSent());
    }

    @Override
    public void onItemClick(Message message, int pos) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.EXTRA_MSG, message);
        bundle.putBoolean(Constant.EXTRA_IS_EDIT, false);
        mNavigator.startActivity(ViewMsgActivity.class, bundle);
    }

    @Override
    public void onItemLongClick(String data) {
        mBinding.layoutOption.setVisibility(View.VISIBLE);
        mIsSelectAll = false;
        mBinding.btnSelectAll.setText(R.string.select_all);
        mAdapter = new MessageScheduleAdapter(mMainActivity, helper.getAllMsgSent(), true, false);
        mAdapter.setOnDataListener(this);
        mAdapter.setOnLongItemClickListner(this);
        mBinding.recycleView.setLayoutManager(mLinearLayoutManager);
        mBinding.recycleView.setAdapter(mAdapter);
    }

    private void showMessageNormal(List<Message> messages) {
        mBinding.layoutOption.setVisibility(View.GONE);
        mAdapter = new MessageScheduleAdapter(mMainActivity, messages, false, false);
        mAdapter.setOnDataListener(this);
        mAdapter.setOnLongItemClickListner(this);
        mBinding.recycleView.setLayoutManager(mLinearLayoutManager);
        mBinding.recycleView.setAdapter(mAdapter);
    }

    public void setTextUnSelectAll() {
        mIsSelectAll = true;
        mAdapter.setSelectedAll();
        mBinding.btnSelectAll.setText(R.string.un_select_all);
    }

    public void setTextSelectAll() {
        mIsSelectAll = false;
        mAdapter.removeSelectedAll();
        mBinding.btnSelectAll.setText(R.string.select_all);
    }
}
