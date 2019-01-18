package com.tinnhanchuctet.loichuchay.chuctet.ui.dialogs;

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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.tinnhanchuctet.loichuchay.chuctet.R;
import com.tinnhanchuctet.loichuchay.chuctet.database.sharedprf.SharedPrefsImpl;
import com.tinnhanchuctet.loichuchay.chuctet.databinding.FragmentMineOpBinding;
import com.tinnhanchuctet.loichuchay.chuctet.listeners.DeleteCallBack;
import com.tinnhanchuctet.loichuchay.chuctet.models.Message;
import com.tinnhanchuctet.loichuchay.chuctet.ui.activities.MainActivity;
import com.tinnhanchuctet.loichuchay.chuctet.utils.Constant;
import com.tinnhanchuctet.loichuchay.chuctet.utils.Navigator;

public class OptionMineFragment extends DialogFragment implements View.OnClickListener {
    private FragmentMineOpBinding mMineOpBinding;
    private boolean mIsDelMsg = false;
    private Message mMessage;
    private MainActivity mMainActivity;
    private DeleteCallBack mCallBack;
    private Navigator mNav;
    private SharedPrefsImpl mShare;
    private String tblName;
    private static final String MY_DEL_DIALOG = "MY_DEL_DIALOG";

    public static OptionMineFragment getInstance(Message message, String tbl) {
        OptionMineFragment fragment = new OptionMineFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constant.ARG_MESSAGE, message);
        args.putString(Constant.ARG_TABLE_NAME, tbl);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMineOpBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_mine_op, container, false);
        initUI();
        return mMineOpBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        int width = getResources().getDimensionPixelSize(R.dimen._236sdp);
        int height = getResources().getDimensionPixelSize(R.dimen._130sdp);
        window.setLayout(width, height);
        window.setGravity(Gravity.CENTER);
    }

    private void initUI() {
        mNav = new Navigator(mMainActivity);
        mShare = new SharedPrefsImpl(mMainActivity);
        Bundle bundle = getArguments();
        mMessage = bundle.getParcelable(Constant.ARG_MESSAGE);
        tblName = bundle.getString(Constant.ARG_TABLE_NAME);
        mMineOpBinding.txtDeleteMsg.setOnClickListener(this);
        mMineOpBinding.txtShareMsg.setOnClickListener(this);
    }

    public void setCallBack(DeleteCallBack callBack) {
        mCallBack = callBack;
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
                ConfirmDelFragment f = ConfirmDelFragment.getInstance(mMessage, tblName);
                f.setCallBack(mCallBack);
                mMainActivity.getSupportFragmentManager().beginTransaction().add(f, MY_DEL_DIALOG).commit();
                dismiss();
                break;
            case R.id.txt_share_msg:
                shareMessage();
                dismiss();
                break;
        }
    }

    private void shareMessage() {
        copyTextToClipbroad();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, mMessage.getContent());
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, getResources().getText(R.string.share_with)));

    }

    private boolean copyTextToClipbroad() {
        ClipboardManager myClipbroad = (ClipboardManager) mMainActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(Constant.LBL_CLIPBROAD, mMessage.getContent());
        myClipbroad.setPrimaryClip(clipData);
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }
}
