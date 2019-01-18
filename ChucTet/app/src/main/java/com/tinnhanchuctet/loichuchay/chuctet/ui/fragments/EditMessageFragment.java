package com.tinnhanchuctet.loichuchay.chuctet.ui.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tinnhanchuctet.loichuchay.chuctet.MyApplication;
import com.tinnhanchuctet.loichuchay.chuctet.R;
import com.tinnhanchuctet.loichuchay.chuctet.database.sharedprf.SharedPrefsImpl;
import com.tinnhanchuctet.loichuchay.chuctet.database.sharedprf.SharedPrefsKey;
import com.tinnhanchuctet.loichuchay.chuctet.database.sqlite.DatabaseHelper;
import com.tinnhanchuctet.loichuchay.chuctet.databinding.FragmentMsgEditBinding;
import com.tinnhanchuctet.loichuchay.chuctet.listeners.EditMsgSuccessListener;
import com.tinnhanchuctet.loichuchay.chuctet.models.Message;
import com.tinnhanchuctet.loichuchay.chuctet.ui.activities.MainActivity;
import com.tinnhanchuctet.loichuchay.chuctet.ui.dialogs.ConfirmEditFragment;
import com.tinnhanchuctet.loichuchay.chuctet.utils.Constant;
import com.tinnhanchuctet.loichuchay.chuctet.utils.Navigator;

import java.util.List;

public class EditMessageFragment extends Fragment implements View.OnClickListener {

    private FragmentMsgEditBinding mMsgEditBinding;
    private MainActivity mMainActivity;
    private Navigator mNav;
    private boolean mIsAddNewMsg;
    private boolean mIsFromMineScreen;
    private Message mMessageGet;
    private List<Message> mMessagelist;
    private SharedPrefsImpl mSharedPref;
    private EditMsgSuccessListener mSuccessListener;

    public static EditMessageFragment newInstance(Message message, boolean isAddNew, boolean isFromMine) {
        EditMessageFragment fragment = new EditMessageFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constant.ARG_MESSAGE, message);
        args.putBoolean(Constant.ARGS_IS_ADD_NEW, isAddNew);
        args.putBoolean(Constant.ARG_IS_FROM_MINE, isFromMine);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMsgEditBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_msg_edit, container, false);
        initUI();
        initAction();
        return mMsgEditBinding.getRoot();
    }

    private void initUI() {
        Typeface font = Typeface.createFromAsset(mMainActivity.getAssets(), "fonts/font_tieude.otf");
        mMsgEditBinding.txtTitle.setTypeface(font);
        Typeface font2 = Typeface.createFromAsset(mMainActivity.getAssets(), "fonts/font_app_b.ttf");
        mMsgEditBinding.txtCopyMsg.setTypeface(font2);
        mMsgEditBinding.txtEditMsg.setTypeface(font2);
        mMsgEditBinding.txtShareMsg.setTypeface(font2);
        Glide.with(this)
                .load(R.drawable.bg_app_none)
                .into(mMsgEditBinding.imgBackground);
        mSharedPref = new SharedPrefsImpl(mMainActivity);
        mMessagelist = mSharedPref.getListMsg();
        mNav = new Navigator(mMainActivity);
        Bundle bundle = getArguments();
        mMessageGet = bundle.getParcelable(Constant.ARG_MESSAGE);
        mIsAddNewMsg = bundle.getBoolean(Constant.ARGS_IS_ADD_NEW);
        mIsFromMineScreen = bundle.getBoolean(Constant.ARG_IS_FROM_MINE);
        mSharedPref.put(SharedPrefsKey.KEY__MSG_EDIT, mMessageGet.getContent());
        mSharedPref.put(SharedPrefsKey.KEY_IS_ADD_NEW, mIsAddNewMsg);
        if (!mIsAddNewMsg) {
            mMsgEditBinding.txtEditMsg.setText(R.string.done);
            mMsgEditBinding.imgAdd.setImageResource(R.drawable.ic_done);
            mMsgEditBinding.txtTitle.setText(R.string.edit_msg);
        } else {
            mMsgEditBinding.txtTitle.setText(R.string.add_msg);
        }
        mMsgEditBinding.contentOfMsg.setText(mMessageGet.getContent());
        //mMsgEditBinding.contentOfMsg.setMovementMethod(new ScrollingMovementMethod());
    }

    private void initAction() {
        mMsgEditBinding.btnBack.setOnClickListener(this);
        mMsgEditBinding.btnHome.setOnClickListener(this);
        mMsgEditBinding.btnCopy.setOnClickListener(this);
        mMsgEditBinding.btnShare.setOnClickListener(this);
        mMsgEditBinding.btnAdd.setOnClickListener(this);
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
                try {
                    mMainActivity.hideSoftKeyboard();
                } catch (Exception ex) {
                    //none
                }
                confirmLeave(mIsAddNewMsg, false);
                break;
            case R.id.btn_home:
                confirmLeave(mIsAddNewMsg, true);
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
            case R.id.btn_add:
                if (mIsAddNewMsg) {
                    insertNewMessage();
                    return;
                } else {
                    int edit = editMsg();
                    if (edit > 0) {
                        mNav.showToast(R.string.edit_sucess);
                        mSuccessListener.msgEdited(mMessageGet);
                        mMainActivity.getSupportFragmentManager().popBackStackImmediate();
                    }
                }

                break;
        }
    }

    private int editMsg() {
        String msg = mMsgEditBinding.contentOfMsg.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            mNav.showToast(R.string.cannot_empty);
            return 0;
        }
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(MyApplication.getInstance());
        mMessageGet.setContent(msg);
        return databaseHelper.updateMessage(mMessageGet);
    }

    private void insertNewMessage() {
        String msg = mMsgEditBinding.contentOfMsg.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            mNav.showToast(R.string.cannot_empty);
            return;
        }
        int size = mMessagelist.size();
        boolean ok = true;
        for (int i = 0; i < size; i++) {
            if ((msg.toLowerCase()).equals(mMessagelist.get(i).getContent().toLowerCase())) {
                Toast.makeText(mMainActivity, R.string.msg_is_exists, Toast.LENGTH_SHORT).show();
                ok = false;
                break;
            }
        }
        if (ok) {
            DatabaseHelper databaseHelper = DatabaseHelper.getInstance(MyApplication.getInstance());
            databaseHelper.insertNewMessage(msg);
            mNav.showToast(R.string.add_success);
            mMainActivity.getSupportFragmentManager().popBackStackImmediate();
            if (!mIsFromMineScreen) {
                MineFragment mineFragment = MineFragment.newInstance();
                mNav.addFragment(R.id.main_container, mineFragment, true, Navigator.NavigateAnim.RIGHT_LEFT, MineFragment.class.getSimpleName());
            }
            mMainActivity.hideSoftKeyboard();
        }
    }

    private void shareMessage() {
        String msg = mMsgEditBinding.contentOfMsg.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            mNav.showToast(R.string.cannot_empty);
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
        String msg = mMsgEditBinding.contentOfMsg.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            mNav.showToast(R.string.cannot_empty);
            return false;
        }
        ClipboardManager clipboard = (ClipboardManager) mMainActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(Constant.LBL_CLIPBROAD, mMsgEditBinding.contentOfMsg.getText().toString());
        clipboard.setPrimaryClip(clip);
        return true;
    }

    public void setSuccessListener(EditMsgSuccessListener listener) {
        mSuccessListener = listener;
    }

    public void confirmLeave(boolean isAddNew, final boolean isHome) {
        boolean checkLeave = true;
        String msg = mMsgEditBinding.contentOfMsg.getText().toString();

        if (msg.equals(mSharedPref.get(SharedPrefsKey.KEY__MSG_EDIT, String.class)) || TextUtils.isEmpty(msg)) {
            checkLeave = false;
        }
        if (!checkLeave) {
            if (isHome) {
                mMainActivity.gotoHomeFragment();
            } else {
                mMainActivity.getSupportFragmentManager().popBackStackImmediate();
            }
            return;
        }
        ConfirmEditFragment f = ConfirmEditFragment.getInstance(isAddNew, isHome);
        mMainActivity.getSupportFragmentManager().beginTransaction().add(f, "Editdialog").commit();
    }
}
