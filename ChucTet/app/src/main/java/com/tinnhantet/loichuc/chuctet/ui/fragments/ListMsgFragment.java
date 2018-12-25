package com.tinnhantet.loichuc.chuctet.ui.fragments;

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

import com.bumptech.glide.Glide;
import com.tinnhantet.loichuc.chuctet.MyApplication;
import com.tinnhantet.loichuc.chuctet.R;
import com.tinnhantet.loichuc.chuctet.adapters.MessageAdapter;
import com.tinnhantet.loichuc.chuctet.database.sharedprf.SharedPrefsImpl;
import com.tinnhantet.loichuc.chuctet.database.sharedprf.SharedPrefsKey;
import com.tinnhantet.loichuc.chuctet.database.sqlite.DatabaseHelper;
import com.tinnhantet.loichuc.chuctet.databinding.FragmentMsgListBinding;
import com.tinnhantet.loichuc.chuctet.listeners.DeleteCallBack;
import com.tinnhantet.loichuc.chuctet.listeners.OnItemClickListener;
import com.tinnhantet.loichuc.chuctet.listeners.OnItemLongClickListener;
import com.tinnhantet.loichuc.chuctet.models.Message;
import com.tinnhantet.loichuc.chuctet.ui.activities.MainActivity;
import com.tinnhantet.loichuc.chuctet.ui.dialogs.GuideFragment;
import com.tinnhantet.loichuc.chuctet.ui.dialogs.OptionFragment;
import com.tinnhantet.loichuc.chuctet.utils.Constant;
import com.tinnhantet.loichuc.chuctet.utils.Navigator;

import java.util.List;

public class ListMsgFragment extends Fragment implements OnItemClickListener, View.OnClickListener,
        OnItemLongClickListener<Message>, DeleteCallBack {

    private FragmentMsgListBinding mBinding;
    private MainActivity mMainActivity;
    private Navigator mNavigator;
    private List<Message> mMessages;
    private MessageAdapter mAdapter;
    private SharedPrefsImpl mSharedPrefs;
    private static final String OPTION_DIALOG = "OPTION_DIALOG";
    private static final String GUIDE_DIALOG = "GUIDE_DIALOG";
    private String tblName;

    public static ListMsgFragment newInstance(String categoryName, String tblName) {
        ListMsgFragment fragment = new ListMsgFragment();
        Bundle args = new Bundle();
        args.putString(Constant.ARGUMENT_CATEGORY_NAME, categoryName);
        args.putString(Constant.ARGUMENT_TBL_NAME, tblName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_msg_list, container, false);
        initUI();
        return mBinding.getRoot();
    }

    private void initUI() {
        Glide.with(this)
                .load(R.drawable.bg_1)
                .into(mBinding.imgBackground);
        Bundle bundle = getArguments();
        mSharedPrefs = new SharedPrefsImpl(mMainActivity);
        mNavigator = new Navigator(mMainActivity);
        mBinding.txtTitle.setText(bundle.getString(Constant.ARGUMENT_CATEGORY_NAME));
        mBinding.btnHome.setOnClickListener(this);
        mBinding.btnBack.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mMainActivity);
        tblName = bundle.getString(Constant.ARGUMENT_TBL_NAME);
        mMessages = DatabaseHelper.getInstance(MyApplication.getInstance()).getListMsg(tblName);
        mAdapter = new MessageAdapter(mMainActivity, mMessages);
        mAdapter.setOnItemClick(this);
        mAdapter.setOnItemLongClick(this);
        mBinding.recyclerView.setLayoutManager(linearLayoutManager);
        mBinding.recyclerView.setAdapter(mAdapter);
        if (!mSharedPrefs.get(SharedPrefsKey.PREF_REMEMBER_GUIDE, Boolean.class)) {
            GuideFragment f = GuideFragment.getInstance();
            mMainActivity.getSupportFragmentManager().beginTransaction().add(f, GUIDE_DIALOG).commit();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public void onItemClick(int pos) {
        MessageFragment messageFragment = MessageFragment.newInstance(mMessages, pos,true, false);
        mNavigator.addFragment(R.id.main_container, messageFragment, true,
                Navigator.NavigateAnim.NONE, MessageFragment.class.getSimpleName());
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
        }
    }

    @Override
    public void onItemLongClick(Message msg) {
        OptionFragment f = OptionFragment.getInstance(msg, tblName);
        f.setDeleteCallBack(this);
        mMainActivity.getSupportFragmentManager().beginTransaction().add(f, OPTION_DIALOG).commit();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void deleteSuccess(Message message) {
        int size = mMessages.size();
        for (int i = 0; i < size; i++) {
            if (mMessages.get(i).getId() == message.getId()) {
                mMessages.remove(i);
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
}
