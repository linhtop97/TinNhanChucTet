package com.tinnhantet.nhantin.hengio.ui.fragments;

import android.content.Context;
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
import com.tinnhantet.nhantin.hengio.models.Message;
import com.tinnhantet.nhantin.hengio.ui.activities.MainActivity;
import com.tinnhantet.nhantin.hengio.utils.Navigator;

import java.util.List;

public class SentFragment extends Fragment implements OnDataClickListener {
    private FragmentSentBinding mBinding;
    private Navigator mNavigator;
    private MessageScheduleAdapter mAdapter;
    private List<Message> mMessages;
    private MessageDatabaseHelper helper;
    private MainActivity mMainActivity;

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
        return mBinding.getRoot();
    }

    private void initUI() {
        mNavigator = new Navigator(mMainActivity);
        helper = MessageDatabaseHelper.getInstance(mMainActivity);
        mMessages = helper.getAllMsgSent();
        mAdapter = new MessageScheduleAdapter(mMainActivity, mMessages);
        mAdapter.setOnDataListener(this);
        mBinding.recycleView.setLayoutManager(new LinearLayoutManager(mMainActivity));
        mBinding.recycleView.setAdapter(mAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public void onItemClick(Object data, int pos) {

    }

    @Override
    public void onStart() {
        super.onStart();  mAdapter.setMessages(helper.getAllMsgSent());
    }
}
