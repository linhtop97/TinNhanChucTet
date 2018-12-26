package com.tinnhantet.loichuc.chuctet.ui.dialogs;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.tinnhantet.loichuc.chuctet.R;
import com.tinnhantet.loichuc.chuctet.database.sharedprf.SharedPrefsImpl;
import com.tinnhantet.loichuc.chuctet.databinding.FragmentMineOpBinding;
import com.tinnhantet.loichuc.chuctet.listeners.DeleteCallBack;
import com.tinnhantet.loichuc.chuctet.models.Message;
import com.tinnhantet.loichuc.chuctet.ui.activities.MainActivity;
import com.tinnhantet.loichuc.chuctet.utils.Constant;
import com.tinnhantet.loichuc.chuctet.utils.Navigator;

public class OptionMineFragment extends DialogFragment implements View.OnClickListener {
    private FragmentMineOpBinding mBinding;
    private boolean mIsDeleteMsg = false;
    private Message mMessage;
    private MainActivity mMainActivity;
    private DeleteCallBack mDeleteCallBack;
    private Navigator mNavigator;
    private SharedPrefsImpl mSharedPrefs;
    private String tblName;
    private static final String DEL_DIALOG = "DEL_DIALOG";

    public static OptionMineFragment getInstance(Message message, String tbl) {
        OptionMineFragment fragment = new OptionMineFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constant.ARGUMENT_MSG, message);
        args.putString(Constant.ARGUMENT_TBL_NAME, tbl);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_mine_op, container, false);
        initUI();
        return mBinding.getRoot();
    }

    private void initUI() {
        mNavigator = new Navigator(mMainActivity);
        mSharedPrefs = new SharedPrefsImpl(mMainActivity);
        Bundle bundle = getArguments();
        mMessage = bundle.getParcelable(Constant.ARGUMENT_MSG);
        tblName = bundle.getString(Constant.ARGUMENT_TBL_NAME);
        mBinding.txtDeleteMsg.setOnClickListener(this);
        mBinding.txtShareMsg.setOnClickListener(this);
    }

    public void setDeleteCallBack(DeleteCallBack deleteCallBack) {
        mDeleteCallBack = deleteCallBack;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // Disable Back key and Search key
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                    case KeyEvent.KEYCODE_SEARCH:
                        return true;
                    default:
                        return false;
                }
            }
        });
        return dialog;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_delete_msg:
                mBinding.txtDeleteMsg.setBackgroundResource(R.drawable.background_button_option);
                mBinding.txtDeleteMsg.setTextColor(mMainActivity.getResources().getColor(R.color.colorWhite));
                ConfirmDelFragment f = ConfirmDelFragment.getInstance(mMessage, tblName);
                f.setDeleteCallBack(mDeleteCallBack);
                mMainActivity.getSupportFragmentManager().beginTransaction().add(f, DEL_DIALOG).commit();
                dismiss();
                break;
            case R.id.txt_share_msg:
                mBinding.txtShareMsg.setBackgroundResource(R.drawable.background_button_option);
                mBinding.txtShareMsg.setTextColor(mMainActivity.getResources().getColor(R.color.colorWhite));
                shareMessage();
                dismiss();
                break;
        }
    }

    private void shareMessage() {
        copyTextToClipbroad();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mMessage.getContent());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share_with)));

    }

    private boolean copyTextToClipbroad() {
        ClipboardManager clipboard = (ClipboardManager) mMainActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(Constant.LABEL_CLIPBROAD, mMessage.getContent());
        clipboard.setPrimaryClip(clip);
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }
}
