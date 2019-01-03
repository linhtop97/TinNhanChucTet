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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.databinding.DialogContactOptionBinding;
import com.tinnhantet.nhantin.hengio.listeners.RemoveContactCallback;
import com.tinnhantet.nhantin.hengio.models.Contact;
import com.tinnhantet.nhantin.hengio.ui.activities.AddMsgActivity;
import com.tinnhantet.nhantin.hengio.utils.Constant;

public class ContactOptionDialog extends DialogFragment implements View.OnClickListener {
    private DialogContactOptionBinding mBinding;
    private AddMsgActivity mAddMsgActivity;
    private RemoveContactCallback mCallback;

    public static ContactOptionDialog getInstance(Contact contact) {
        ContactOptionDialog fragment = new ContactOptionDialog();
        Bundle args = new Bundle();
        args.putParcelable(Constant.ARG_CONTACT, contact);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_contact_option, container, false);
        initUI();
        initAction();
        return mBinding.getRoot();
    }

    private void initAction() {
        mBinding.btnDelete.setOnClickListener(this);
    }

    private void initUI() {
        Contact contact = getArguments().getParcelable(Constant.ARG_CONTACT);
        String name = contact.getName();
        String phone = contact.getPhone();
        if (name.equals("")) {
            name = phone;
        } else {
            name = name + " - " + phone;
        }
        mBinding.txtTitle.setText(name);
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        int width = getResources().getDimensionPixelSize(R.dimen._250sdp);
        int height = getResources().getDimensionPixelSize(R.dimen._100sdp);
        window.setLayout(width, height);
        window.setGravity(Gravity.CENTER);
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
            case R.id.btn_delete:
                //mCallback.contactRemoved();
                mAddMsgActivity.removeContact();
                dismiss();
                break;
        }
    }

    public void setRemoveCallback(RemoveContactCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAddMsgActivity = (AddMsgActivity) context;
    }
}
