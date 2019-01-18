package com.tinnhanchuctet.loichuchay.chuctet.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.tinnhanchuctet.loichuchay.chuctet.databinding.DialogCfEditBinding;
import com.tinnhanchuctet.loichuchay.chuctet.ui.activities.MainActivity;
import com.tinnhanchuctet.loichuchay.chuctet.utils.Constant;

public class ConfirmEditFragment extends DialogFragment implements View.OnClickListener {
    private DialogCfEditBinding mDialogCfEditBinding;
    private boolean mIsAddnewMsg = false;
    private boolean mIsHomeScreen = false;
    private MainActivity mMainActivity;

    public static ConfirmEditFragment getInstance(boolean isAddNew, boolean isHome) {
        ConfirmEditFragment f = new ConfirmEditFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constant.ARG_IS_HOME, isHome);
        args.putBoolean(Constant.ARGS_IS_ADD_NEW, isAddNew);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDialogCfEditBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_cf_edit, container, false);
        initUI();
        return mDialogCfEditBinding.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        int width = getResources().getDimensionPixelSize(R.dimen._220sdp);
        int height = getResources().getDimensionPixelSize(R.dimen._100sdp);
        window.setLayout(width, height);
        window.setGravity(Gravity.CENTER);
    }


    private void initUI() {
        Bundle bundle = getArguments();
        mIsAddnewMsg = bundle.getBoolean(Constant.ARGS_IS_ADD_NEW);
        mIsHomeScreen = bundle.getBoolean(Constant.ARG_IS_HOME);
        if (mIsAddnewMsg) {
            mDialogCfEditBinding.txtConfirm.setText(R.string.cancel_add);
        } else {
            mDialogCfEditBinding.txtConfirm.setText(R.string.cancel_edit);
        }
        mDialogCfEditBinding.btnOk.setOnClickListener(this);
        mDialogCfEditBinding.btnCancel.setOnClickListener(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog myDialog = super.onCreateDialog(savedInstanceState);
        myDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCancelable(true);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
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
        return myDialog;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                if (mIsHomeScreen) {
                    mMainActivity.gotoHomeFragment();
                } else {
                    mMainActivity.getSupportFragmentManager().popBackStackImmediate();
                }
                dismiss();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }
}
