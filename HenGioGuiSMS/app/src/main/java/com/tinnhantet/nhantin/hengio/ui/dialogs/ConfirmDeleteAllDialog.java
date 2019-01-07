package com.tinnhantet.nhantin.hengio.ui.dialogs;

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
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.databinding.DialogCfDeleteSelectedBinding;
import com.tinnhantet.nhantin.hengio.ui.activities.MainActivity;
import com.tinnhantet.nhantin.hengio.ui.fragments.PendingFragment;
import com.tinnhantet.nhantin.hengio.ui.fragments.SentFragment;

public class ConfirmDeleteAllDialog extends DialogFragment implements View.OnClickListener {
    private DialogCfDeleteSelectedBinding mBinding;
    private MainActivity mMainActivity;

    public static ConfirmDeleteAllDialog getInstance() {
        ConfirmDeleteAllDialog fragment = new ConfirmDeleteAllDialog();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_cf_delete_selected, container, false);
        initUI();
        return mBinding.getRoot();
    }

    private void initUI() {
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
                //delete Msg
                Fragment fragment = mMainActivity.getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + 0);
                if (fragment != null) {
                    PendingFragment messageFragment = (PendingFragment) fragment;
                    messageFragment.deleteAllSelected();
                }
                Fragment fragment2 = mMainActivity.getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + 1);
                if (fragment2 != null) {
                    SentFragment messageFragment2 = (SentFragment) fragment2;
                    messageFragment2.deleteAllSelected();
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
