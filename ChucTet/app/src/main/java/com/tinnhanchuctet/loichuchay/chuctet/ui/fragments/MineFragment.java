package com.tinnhanchuctet.loichuchay.chuctet.ui.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.tinnhanchuctet.loichuchay.chuctet.Ads;
import com.tinnhanchuctet.loichuchay.chuctet.MyApplication;
import com.tinnhanchuctet.loichuchay.chuctet.R;
import com.tinnhanchuctet.loichuchay.chuctet.adapters.MessageAdapter;
import com.tinnhanchuctet.loichuchay.chuctet.database.sharedprf.SharedPrefsImpl;
import com.tinnhanchuctet.loichuchay.chuctet.database.sqlite.DatabaseHelper;
import com.tinnhanchuctet.loichuchay.chuctet.database.sqlite.TableEntity;
import com.tinnhanchuctet.loichuchay.chuctet.databinding.FragmentMsgMineBinding;
import com.tinnhanchuctet.loichuchay.chuctet.listeners.DeleteCallBack;
import com.tinnhanchuctet.loichuchay.chuctet.listeners.OnItemClickListener;
import com.tinnhanchuctet.loichuchay.chuctet.listeners.OnItemLongClickListener;
import com.tinnhanchuctet.loichuchay.chuctet.models.Message;
import com.tinnhanchuctet.loichuchay.chuctet.ui.activities.MainActivity;
import com.tinnhanchuctet.loichuchay.chuctet.ui.dialogs.OptionMineFragment;
import com.tinnhanchuctet.loichuchay.chuctet.utils.Navigator;

import java.util.ArrayList;
import java.util.List;

public class MineFragment extends Fragment implements OnItemClickListener, View.OnClickListener, OnItemLongClickListener<Message>, DeleteCallBack {

    private FragmentMsgMineBinding mMineBinding;
    private List<Message> mMessageList;
    private MainActivity mMainActivity;
    private Navigator mNav;
    private MessageAdapter mMessageAdapter;
    private SharedPrefsImpl mSharedPref;
    private static final String MINE_OPTION_DIALOG = "MINE_OPTION_DIALOG";

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMineBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_msg_mine, container, false);
        initUI();
        return mMineBinding.getRoot();
    }

    private void initUI() {
        Typeface font = Typeface.createFromAsset(mMainActivity.getAssets(), "fonts/font_tieude.otf");
        mMineBinding.txtTitle.setTypeface(font);
        Glide.with(this)
                .load(R.drawable.bg_app_none)
                .into(mMineBinding.imgBackground);
        mMineBinding.btnBack.setOnClickListener(this);
        mMineBinding.btnHome.setOnClickListener(this);
        mSharedPref = new SharedPrefsImpl(mMainActivity);
        mNav = new Navigator(mMainActivity);
        mMineBinding.btnAddNew.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mMainActivity);
        mMessageList = new ArrayList<>();
        mMessageList = DatabaseHelper.getInstance(MyApplication.getInstance()).getListMsg(TableEntity.TBL_MY_MESSAGE);
        mSharedPref.putListMsg(mMessageList);
        if (mMessageList.size() == 0) {
            mMineBinding.txtNone.setVisibility(View.VISIBLE);
        } else {
            mMineBinding.txtNone.setVisibility(View.GONE);
        }
        mMessageAdapter = new MessageAdapter(mMainActivity, mMessageList);
        mMessageAdapter.setOnItemClick(this);
        mMessageAdapter.setOnItemLongClick(this);
        mMineBinding.recyclerView.setLayoutManager(layoutManager);
        mMineBinding.recyclerView.setAdapter(mMessageAdapter);
        Ads.f(mMainActivity);
    }

    @Override
    public void onItemClick(int pos) {
        MessageFragment messageFragment = MessageFragment.newInstance(mMessageList, pos, false, true);
        mNav.addFragment(R.id.main_container, messageFragment, true,
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
                mNav.addFragment(R.id.main_container, fragment, true,
                        Navigator.NavigateAnim.NONE, EditMessageFragment.class.getSimpleName());
                break;
        }
    }

    @Override
    public void onItemLongClick(Message message) {
        OptionMineFragment f = OptionMineFragment.getInstance(message, TableEntity.TBL_MY_MESSAGE);
        f.setCallBack(this);
        mMainActivity.getSupportFragmentManager().beginTransaction().add(f, MINE_OPTION_DIALOG).commit();
    }

    @Override
    public void deleteSuccess(Message message) {
        int size = mMessageList.size();
        for (int i = 0; i < size; i++) {
            if (mMessageList.get(i).getId() == message.getId()) {
                mMessageList.remove(i);
                mMessageAdapter.notifyDataSetChanged();
                break;
            }
        }
        if (mMessageList.size() == 0) {
            mMineBinding.txtNone.setVisibility(View.VISIBLE);
        } else {
            mMineBinding.txtNone.setVisibility(View.GONE);
        }
    }
}
