package com.tinnhantet.loichuc.chuctet.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.tinnhantet.loichuc.chuctet.MyApplication;
import com.tinnhantet.loichuc.chuctet.R;
import com.tinnhantet.loichuc.chuctet.adapters.MessageAdapter;
import com.tinnhantet.loichuc.chuctet.database.sharedprf.SharedPrefsImpl;
import com.tinnhantet.loichuc.chuctet.database.sqlite.DatabaseHelper;
import com.tinnhantet.loichuc.chuctet.database.sqlite.TableEntity;
import com.tinnhantet.loichuc.chuctet.databinding.FragmentMsgMineBinding;
import com.tinnhantet.loichuc.chuctet.listeners.OnItemClickListener;
import com.tinnhantet.loichuc.chuctet.listeners.OnItemLongClickListener;
import com.tinnhantet.loichuc.chuctet.models.Message;
import com.tinnhantet.loichuc.chuctet.ui.activities.MainActivity;
import com.tinnhantet.loichuc.chuctet.utils.Navigator;
import java.util.ArrayList;
import java.util.List;

public class MineFragment extends Fragment implements OnItemClickListener, View.OnClickListener, OnItemLongClickListener<Message> {

    private FragmentMsgMineBinding mBinding;
    private List<Message> mMessages;
    private MainActivity mMainActivity;
    private Navigator mNavigator;
    private MessageAdapter mAdapter;
    private SharedPrefsImpl mSharedPrefs;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_msg_mine, container, false);
        initUI();
        return mBinding.getRoot();
    }

    private void initUI() {
        Glide.with(this)
                .load(R.drawable.bg_1)
                .into(mBinding.imgBackground);
        mBinding.btnBack.setOnClickListener(this);
        mBinding.btnHome.setOnClickListener(this);
        mSharedPrefs = new SharedPrefsImpl(mMainActivity);
        mNavigator = new Navigator(mMainActivity);
        mBinding.btnAddNew.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mMainActivity);
        mMessages = new ArrayList<>();
        List<Message> messages = DatabaseHelper.getInstance(MyApplication.getInstance()).getListMsg(TableEntity.TBL_MY_MESSAGE);
        int size = messages.size();
        for (int i = size - 1; i >= 0; i--) {
            mMessages.add(messages.get(i));
        }
        mSharedPrefs.putListMsg(mMessages);
        if (mMessages.size() == 0) {
            mBinding.txtNone.setVisibility(View.VISIBLE);
        } else {
            mBinding.txtNone.setVisibility(View.GONE);
        }
        mAdapter = new MessageAdapter(mMainActivity, mMessages);
        mAdapter.setOnItemClick(this);
        mAdapter.setOnItemLongClick(this);
        mBinding.recyclerView.setLayoutManager(linearLayoutManager);
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(int pos) {
        MessageFragment messageFragment = MessageFragment.newInstance(mMessages, pos, pos + 1, false, true);
        mNavigator.addFragment(R.id.main_container, messageFragment, true,
                Navigator.NavigateAnim.NONE, MessageFragment.class.getSimpleName());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                mMainActivity.getSupportFragmentManager().popBackStackImmediate();
                break;
            case R.id.btn_home:
                mMainActivity.gotoHomeFragment();
                break;
            case R.id.btn_add_new:
                EditMessageFragment fragment = EditMessageFragment.newInstance(new Message(0, ""), true, true);
                mNavigator.addFragment(R.id.main_container, fragment, true,
                        Navigator.NavigateAnim.NONE, EditMessageFragment.class.getSimpleName());
                break;
        }
    }

    @Override
    public void onItemLongClick(Message message) {
        confirmDelete(message);
    }

    private void confirmDelete(final Message message) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(mMainActivity);
        dialog.setTitle(R.string.notifi);
        dialog.setMessage(R.string.delete_confirm_msg);
        dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (deleteMsg(TableEntity.TBL_MY_MESSAGE, message) > 0) {
                    int size = mMessages.size();
                    for (int j = 0; j < size; j++) {
                        if (mMessages.get(j).getId() == message.getId()) {
                            mMessages.remove(j);
                            mAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                    mSharedPrefs.putListMsg(mMessages);
                    mNavigator.showToast(R.string.delete_success);
                } else {
                    mNavigator.showToast(R.string.delete_failed);
                }

            }
        });

        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog.show();
    }

    private int deleteMsg(String tblName, Message message) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(MyApplication.getInstance());
        return databaseHelper.deleteMessage(tblName, message.getId());
    }
}
