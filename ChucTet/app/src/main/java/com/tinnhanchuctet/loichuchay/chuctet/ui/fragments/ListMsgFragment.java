package com.tinnhanchuctet.loichuchay.chuctet.ui.fragments;

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
import com.tinnhanchuctet.loichuchay.chuctet.MyApplication;
import com.tinnhanchuctet.loichuchay.chuctet.R;
import com.tinnhanchuctet.loichuchay.chuctet.adapters.MessageAdapter;
import com.tinnhanchuctet.loichuchay.chuctet.database.sharedprf.SharedPrefsImpl;
import com.tinnhanchuctet.loichuchay.chuctet.database.sharedprf.SharedPrefsKey;
import com.tinnhanchuctet.loichuchay.chuctet.database.sqlite.DatabaseHelper;
import com.tinnhanchuctet.loichuchay.chuctet.databinding.FragmentMsgListBinding;
import com.tinnhanchuctet.loichuchay.chuctet.listeners.OnItemClickListener;
import com.tinnhanchuctet.loichuchay.chuctet.listeners.OnItemLongClickListener;
import com.tinnhanchuctet.loichuchay.chuctet.models.Message;
import com.tinnhanchuctet.loichuchay.chuctet.ui.activities.MainActivity;
import com.tinnhanchuctet.loichuchay.chuctet.ui.dialogs.GuideFragment;
import com.tinnhanchuctet.loichuchay.chuctet.ui.dialogs.OptionFragment;
import com.tinnhanchuctet.loichuchay.chuctet.utils.Constant;
import com.tinnhanchuctet.loichuchay.chuctet.utils.Navigator;

import java.util.List;

public class ListMsgFragment extends Fragment implements OnItemClickListener, View.OnClickListener,
        OnItemLongClickListener<Message> {

    private FragmentMsgListBinding mMsgListBinding;
    private MainActivity mMainActivity;
    private Navigator mNav;
    private List<Message> mMessageList;
    private MessageAdapter mMessageAdapter;
    private SharedPrefsImpl mSharedPref;
    private static final String MY_OPTION_DIALOG = "MY_OPTION_DIALOG";
    private static final String MY_GUIDE_DIALOG = "MY_GUIDE_DIALOG";
    private String mTblName;

    public static ListMsgFragment newInstance(String categoryName, String tblName) {
        ListMsgFragment fragment = new ListMsgFragment();
        Bundle args = new Bundle();
        args.putString(Constant.ARG_CATEGORY_NAME, categoryName);
        args.putString(Constant.ARG_TABLE_NAME, tblName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMsgListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_msg_list, container, false);
        initUI();
        return mMsgListBinding.getRoot();
    }

    private void initUI() {
        Glide.with(this)
                .load(R.drawable.bg_1)
                .into(mMsgListBinding.imgBackground);
        Bundle bundle = getArguments();
        mSharedPref = new SharedPrefsImpl(mMainActivity);
        mNav = new Navigator(mMainActivity);
        mMsgListBinding.txtTitle.setText(bundle.getString(Constant.ARG_CATEGORY_NAME));
        mMsgListBinding.btnHome.setOnClickListener(this);
        mMsgListBinding.btnBack.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mMainActivity);
        mTblName = bundle.getString(Constant.ARG_TABLE_NAME);
        mMessageList = DatabaseHelper.getInstance(MyApplication.getInstance()).getListMsg(mTblName);
        mMessageAdapter = new MessageAdapter(mMainActivity, mMessageList);
        mMessageAdapter.setOnItemClick(this);
        mMessageAdapter.setOnItemLongClick(this);
        mMsgListBinding.recyclerView.setLayoutManager(linearLayoutManager);
        mMsgListBinding.recyclerView.setAdapter(mMessageAdapter);
        if (!mSharedPref.get(SharedPrefsKey.PREF_REMEMBER_GUIDE, Boolean.class)) {
            GuideFragment f = GuideFragment.getInstance();
            mMainActivity.getSupportFragmentManager().beginTransaction().add(f, MY_GUIDE_DIALOG).commit();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public void onItemClick(int pos) {
        MessageFragment messageFragment = MessageFragment.newInstance(mMessageList, pos, true, false);
        mNav.addFragment(R.id.main_container, messageFragment, true,
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
        OptionFragment f = OptionFragment.getInstance(msg, mTblName);
        mMainActivity.getSupportFragmentManager().beginTransaction().add(f, MY_OPTION_DIALOG).commit();
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
