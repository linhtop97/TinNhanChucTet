package com.tinnhanchuctet.loichuchay.chuctet.ui.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tinnhanchuctet.loichuchay.chuctet.Ads;
import com.tinnhanchuctet.loichuchay.chuctet.R;
import com.tinnhanchuctet.loichuchay.chuctet.adapters.MyTVAdapter;
import com.tinnhanchuctet.loichuchay.chuctet.adapters.TextViewAdapter;
import com.tinnhanchuctet.loichuchay.chuctet.databinding.FragmentMsgBinding;
import com.tinnhanchuctet.loichuchay.chuctet.listeners.EditMsgSuccessListener;
import com.tinnhanchuctet.loichuchay.chuctet.models.Message;
import com.tinnhanchuctet.loichuchay.chuctet.ui.activities.MainActivity;
import com.tinnhanchuctet.loichuchay.chuctet.utils.Constant;
import com.tinnhanchuctet.loichuchay.chuctet.utils.Navigator;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment implements View.OnClickListener, EditMsgSuccessListener, TextViewAdapter.IsetMsg {

    private FragmentMsgBinding mMsgBinding;
    private MainActivity mMainActivity;
    private List<Message> mMessageList;
    private int mPos;
    private Navigator mNav;
    private boolean mIsAddNewMsg;
    private boolean mIsFromMineScreen;

    public static MessageFragment newInstance(List<Message> messages, int position, boolean isAddNew, Boolean isFromMine) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(Constant.ARG_LIST_MSG, (ArrayList<? extends Parcelable>) messages);
        args.putInt(Constant.ARG_MSG_POS, position);
        args.putBoolean(Constant.ARGS_IS_ADD_NEW, isAddNew);
        args.putBoolean(Constant.ARG_IS_FROM_MINE, isFromMine);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMsgBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_msg, container, false);
        initUI();
        initAction();
        return mMsgBinding.getRoot();
    }

    private void initUI() {
        Glide.with(this)
                .load(R.drawable.bg_1)
                .into(mMsgBinding.imgBackground);
        mNav = new Navigator(mMainActivity);
        Bundle bundle = getArguments();
        mMessageList = bundle.getParcelableArrayList(Constant.ARG_LIST_MSG);
        mPos = bundle.getInt(Constant.ARG_MSG_POS);
        mIsAddNewMsg = bundle.getBoolean(Constant.ARGS_IS_ADD_NEW);
        mIsFromMineScreen = bundle.getBoolean(Constant.ARG_IS_FROM_MINE);
        mMsgBinding.txtTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        setCurrentMsg(mPos + 1);

        MyTVAdapter myTVAdapter = new MyTVAdapter(getChildFragmentManager(), mMessageList);
        mMsgBinding.viewPager.setAdapter(myTVAdapter);
        mMsgBinding.viewPager.setCurrentItem(mPos);
        mMsgBinding.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int mScrollState;

            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                setCurrentMsg(i + 1);
                mPos = i;
            }

            @Override
            public void onPageScrollStateChanged(final int state) {
                handleScrollState(state);
                mScrollState = state;
            }

            private void handleScrollState(final int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    setNextItemIfNeeded();
                }
            }

            private void setNextItemIfNeeded() {
                if (!isScrollStateSettling()) {
                    handleSetNextItem();
                }
            }

            private boolean isScrollStateSettling() {
                return mScrollState == ViewPager.SCROLL_STATE_SETTLING;
            }
        });
        Ads.f(mMainActivity);
    }

    private void handleSetNextItem() {
        final int lstPosition = mMsgBinding.viewPager.getAdapter().getCount() - 1;
        if (mPos == 0) {
            mMsgBinding.viewPager.setCurrentItem(lstPosition, false);
        } else if (mPos == lstPosition) {
            mMsgBinding.viewPager.setCurrentItem(0, false);
        }
    }

    private void initAction() {
        mMsgBinding.btnBack.setOnClickListener(this);
        mMsgBinding.btnHome.setOnClickListener(this);
        mMsgBinding.btnCopy.setOnClickListener(this);
        mMsgBinding.btnShare.setOnClickListener(this);
        mMsgBinding.btnEdit.setOnClickListener(this);
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
                mMainActivity.hideSoftKeyboard();
                break;
            case R.id.btn_home:
                mMainActivity.gotoHomeFragment();
                mMainActivity.hideSoftKeyboard();
                break;
            case R.id.btn_copy:
                if (copyTextToClipbroad()) {
                    Toast.makeText(mMainActivity, R.string.copy_successful, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_share:
                shareMessage();
                break;
            case R.id.btn_edit:
                EditMessageFragment fragment;
                if (mIsAddNewMsg) {
                    fragment = EditMessageFragment.newInstance(mMessageList.get(mPos), true, mIsFromMineScreen);
                } else {
                    fragment = EditMessageFragment.newInstance(mMessageList.get(mPos), false, mIsFromMineScreen);
                    fragment.setSuccessListener(this);
                }

                mNav.addFragment(R.id.main_container, fragment, true,
                        Navigator.NavigateAnim.NONE, EditMessageFragment.class.getSimpleName());
                break;
        }
    }

    private void shareMessage() {
        String s = mMessageList.get(mPos).getContent();
        if (TextUtils.isEmpty(s)) {
            mNav.showToast(R.string.cannot_empty);
            return;
        }
        copyTextToClipbroad();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, s);
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, getResources().getText(R.string.share_with)));

    }

    private boolean copyTextToClipbroad() {
        String sContent = mMessageList.get(mPos).getContent();
        if (TextUtils.isEmpty(sContent)) {
            mNav.showToast(R.string.cannot_empty);
            return false;
        }
        ClipboardManager myClipbroad = (ClipboardManager) mMainActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(Constant.LBL_CLIPBROAD, sContent);
        myClipbroad.setPrimaryClip(clipData);
        return true;
    }

    private void setCurrentMsg(int position) {
        mMsgBinding.txtTitle.setText("".concat(position + "/").concat(mMessageList.size() + ""));
    }

    @Override
    public void msgEdited(Message message) {
        mMessageList.get(mPos).setContent(message.getContent());
    }

    @Override
    public void setMsg(Message message) {
        mMsgBinding.contentOfMsg.setText(message.getContent());
    }
}
