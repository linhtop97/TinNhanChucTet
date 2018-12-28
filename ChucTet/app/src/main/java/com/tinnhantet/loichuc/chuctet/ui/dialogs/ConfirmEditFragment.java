package com.tinnhantet.loichuc.chuctet.ui.dialogs;

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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.tinnhantet.loichuc.chuctet.R;
import com.tinnhantet.loichuc.chuctet.databinding.DialogCfEditBinding;
import com.tinnhantet.loichuc.chuctet.ui.activities.MainActivity;
import com.tinnhantet.loichuc.chuctet.utils.Constant;

public class ConfirmEditFragment extends DialogFragment implements View.OnClickListener {
    private DialogCfEditBinding mBinding;
    private boolean mIsAddnew = false;
    private boolean mIsHome = false;
    private MainActivity mMainActivity;

    public static ConfirmEditFragment getInstance(boolean isAddNew, boolean isHome) {
        ConfirmEditFragment fragment = new ConfirmEditFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constant.ARGUMENT_IS_HOME, isHome);
        args.putBoolean(Constant.ARGUMENT_IS_ADD_NEW, isAddNew);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_cf_edit, container, false);
        initUI();
        return mBinding.getRoot();
    }

    private void initUI() {
        Bundle bundle = getArguments();
        mIsAddnew = bundle.getBoolean(Constant.ARGUMENT_IS_ADD_NEW);
        mIsHome = bundle.getBoolean(Constant.ARGUMENT_IS_HOME);
        if (mIsAddnew) {
            mBinding.txtConfirm.setText(R.string.cancel_add);
        } else {
            mBinding.txtConfirm.setText(R.string.cancel_edit);
        }
        mBinding.btnOk.setOnClickListener(this);
        mBinding.btnCancel.setOnClickListener(this);
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
            case R.id.btn_ok:
                mBinding.btnOk.setBackgroundResource(R.drawable.background_button_option);
                mBinding.btnOk.setTextColor(mMainActivity.getResources().getColor(R.color.colorWhite));
                if (mIsHome) {
                    mMainActivity.gotoHomeFragment();
                } else {
                    mMainActivity.getSupportFragmentManager().popBackStackImmediate();
                }
                dismiss();
                break;
            case R.id.btn_cancel:
                mBinding.btnCancel.setBackgroundResource(R.drawable.background_button_option);
                mBinding.btnCancel.setTextColor(mMainActivity.getResources().getColor(R.color.colorWhite));
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
