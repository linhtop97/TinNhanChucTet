package com.tinnhantet.nhantin.hengio.ui.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.adapters.MessageScheduleAdapter;
import com.tinnhantet.nhantin.hengio.database.sqlite.MessageDatabaseHelper;
import com.tinnhantet.nhantin.hengio.databinding.FragmentPendingBinding;
import com.tinnhantet.nhantin.hengio.listeners.OnDataClickListener;
import com.tinnhantet.nhantin.hengio.models.Message;
import com.tinnhantet.nhantin.hengio.ui.activities.AddMsgActivity;
import com.tinnhantet.nhantin.hengio.ui.activities.MainActivity;
import com.tinnhantet.nhantin.hengio.ui.activities.ViewMsgActivity;
import com.tinnhantet.nhantin.hengio.utils.Constant;
import com.tinnhantet.nhantin.hengio.utils.Navigator;

import java.util.List;

public class PendingFragment extends Fragment implements View.OnClickListener, OnDataClickListener<Message> {
    private static final String TAG = "PendingFragment";
    private FragmentPendingBinding mBinding;
    private MainActivity mMainActivity;
    private Navigator mNavigator;
    private MessageScheduleAdapter mAdapter;
    private List<Message> mMessages;
    private MessageDatabaseHelper helper;

    public static PendingFragment newInstance() {
        PendingFragment fragment = new PendingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pending, container, false);
        initUI();
        initAction();
        return mBinding.getRoot();
    }

    private void initAction() {
        mBinding.btnAdd.setOnClickListener(this);
    }

    private void initUI() {
        mNavigator = new Navigator(mMainActivity);
        helper = MessageDatabaseHelper.getInstance(mMainActivity);
        mMessages = helper.getAllMsgPending();
        mAdapter = new MessageScheduleAdapter(mMainActivity, mMessages);
        mAdapter.setOnDataListener(this);
        mBinding.recycleView.setLayoutManager(new LinearLayoutManager(mMainActivity));
        mBinding.recycleView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                mNavigator.startActivity(AddMsgActivity.class, Navigator.NavigateAnim.BOTTOM_UP);
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
        mAdapter.setMessages(helper.getAllMsgPending());
        //get All msg Schedule

    }

    @Override
    public void onItemClick(Message message, int pos) {
        //start Activity View Msg
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.EXTRA_MSG, message);
        mNavigator.startActivity(ViewMsgActivity.class, bundle);
    }
}
