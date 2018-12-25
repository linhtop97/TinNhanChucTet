package com.tinnhantet.loichuc.chuctet.ui.fragments;

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
import com.tinnhantet.loichuc.chuctet.R;
import com.tinnhantet.loichuc.chuctet.adapters.MyTVAdapter;
import com.tinnhantet.loichuc.chuctet.adapters.TextViewAdapter;
import com.tinnhantet.loichuc.chuctet.databinding.FragmentMsgBinding;
import com.tinnhantet.loichuc.chuctet.listeners.EditMsgSuccessListener;
import com.tinnhantet.loichuc.chuctet.models.Message;
import com.tinnhantet.loichuc.chuctet.ui.activities.MainActivity;
import com.tinnhantet.loichuc.chuctet.utils.Constant;
import com.tinnhantet.loichuc.chuctet.utils.Navigator;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment implements View.OnClickListener, EditMsgSuccessListener, TextViewAdapter.IsetMsg {

    private FragmentMsgBinding mBinding;
    private MainActivity mMainActivity;
    private List<Message> mMessages;
    private int mPosition;
    //private int mNum;
    private Navigator mNavigator;
    //    private DetectSwipeGestureListener mGestureListener;
//    private int mSize;
    private boolean mIsAddNew;
    private boolean mIsFromMine;

    public static MessageFragment newInstance(List<Message> messages, int position, boolean isAddNew, Boolean isFromMine) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(Constant.ARGUMENT_LIST_MSG, (ArrayList<? extends Parcelable>) messages);
        args.putInt(Constant.ARGUMENT_MSG_POS, position);
        args.putBoolean(Constant.ARGUMENT_IS_ADD_NEW, isAddNew);
        args.putBoolean(Constant.ARGUMENT_IS_FROM_MINE, isFromMine);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_msg, container, false);
        initUI();
        initAction();
        return mBinding.getRoot();
    }

    private void initUI() {
        Glide.with(this)
                .load(R.drawable.bg_1)
                .into(mBinding.imgBackground);
        mNavigator = new Navigator(mMainActivity);
        Bundle bundle = getArguments();
        mMessages = bundle.getParcelableArrayList(Constant.ARGUMENT_LIST_MSG);
        mPosition = bundle.getInt(Constant.ARGUMENT_MSG_POS);
        mIsAddNew = bundle.getBoolean(Constant.ARGUMENT_IS_ADD_NEW);
        mIsFromMine = bundle.getBoolean(Constant.ARGUMENT_IS_FROM_MINE);
        mBinding.txtTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        //mSize = mMessages.size();
        setCurrentMsg(mPosition + 1);

        MyTVAdapter myTVAdapter = new MyTVAdapter(getChildFragmentManager(), mMessages);
        mBinding.viewPager.setAdapter(myTVAdapter);
        mBinding.viewPager.setCurrentItem(mPosition);
        mBinding.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int mScrollState;

            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                setCurrentMsg(i + 1);
                mPosition = i;
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


        //   final TextViewAdapter adapter = new TextViewAdapter(mMainActivity, mMessages, this);
//        mBinding.viewPager.setAdapter(adapter);
//        mBinding.viewPager.setCurrentItem(mPosition);
//        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//                setCurrentMsg(i + 1);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//
//            }
//        });
//        mBinding.contentOfMsg.setMovementMethod(new ScrollingMovementMethod());
        //     mBinding.contentOfMsg.setText(mMessages.get(mPosition).getContent());
    }

    private void handleSetNextItem() {
        final int lastPosition = mBinding.viewPager.getAdapter().getCount() - 1;
        if (mPosition == 0) {
            mBinding.viewPager.setCurrentItem(lastPosition, false);
        } else if (mPosition == lastPosition) {
            mBinding.viewPager.setCurrentItem(0, false);
        }
    }

    private void initAction() {
        mBinding.btnBack.setOnClickListener(this);
        mBinding.btnHome.setOnClickListener(this);
        mBinding.btnCopy.setOnClickListener(this);
        mBinding.btnShare.setOnClickListener(this);
        mBinding.btnEdit.setOnClickListener(this);
//        mGestureListener = new DetectSwipeGestureListener();
//        mGestureListener.setActivity(mMainActivity);
//        final GestureDetectorCompat gestureDetectorCompat = new GestureDetectorCompat(mMainActivity, mGestureListener);
//        mBinding.contentOfMsg.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return gestureDetectorCompat.onTouchEvent(event);
//            }
//        });
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
                if (mIsAddNew) {
                    fragment = EditMessageFragment.newInstance(mMessages.get(mPosition), true, mIsFromMine);
                } else {
                    fragment = EditMessageFragment.newInstance(mMessages.get(mPosition), false, mIsFromMine);
                    fragment.setEditMsgSuccessListener(this);
                }

                mNavigator.addFragment(R.id.main_container, fragment, true,
                        Navigator.NavigateAnim.NONE, EditMessageFragment.class.getSimpleName());
                break;
        }
    }

    private void shareMessage() {
        String msg = mMessages.get(mPosition).getContent();
        if (TextUtils.isEmpty(msg)) {
            mNavigator.showToast(R.string.cannot_empty);
            return;
        }
        copyTextToClipbroad();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share_with)));

    }

    private boolean copyTextToClipbroad() {
        String msg = mMessages.get(mPosition).getContent();
        if (TextUtils.isEmpty(msg)) {
            mNavigator.showToast(R.string.cannot_empty);
            return false;
        }
        ClipboardManager clipboard = (ClipboardManager) mMainActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(Constant.LABEL_CLIPBROAD, msg);
        clipboard.setPrimaryClip(clip);
        return true;
    }

//    public void onSwipeRight() {
//        if (mPosition == mMessages.size() - 1) {
//            mNum = 0;
//            mPosition = 0;
//            mBinding.contentOfMsg.setText(mMessages.get(mPosition).getContent());
//
//        } else {
//            mBinding.contentOfMsg.setText(mMessages.get(++mPosition).getContent());
//        }
//        setCurrentMsg(++mNum);
//    }
//
//    public void onSwipeLeft() {
//        if (mPosition == 0) {
//            mNum = mSize;
//            setCurrentMsg(mNum);
//            mPosition = mMessages.size() - 1;
//            mBinding.contentOfMsg.setText(mMessages.get(mPosition).getContent());
//        } else {
//            mBinding.contentOfMsg.setText(mMessages.get(--mPosition).getContent());
//            setCurrentMsg(--mNum);
//        }
//
//    }

    private void setCurrentMsg(int position) {
        mBinding.txtTitle.setText("".concat(position + "/").concat(mMessages.size() + ""));
    }


    @Override
    public void msgEdited(Message message) {
        //update list msg
        mMessages.get(mPosition).setContent(message.getContent());
        //mBinding.contentOfMsg.setText(message.getContent());

        //set text cho view pager

    }

    @Override
    public void setMsg(Message message) {
        mBinding.contentOfMsg.setText(message.getContent());
    }
}
