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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.tinnhanchuctet.loichuchay.chuctet.MyApplication;
import com.tinnhanchuctet.loichuchay.chuctet.R;
import com.tinnhanchuctet.loichuchay.chuctet.database.sharedprf.SharedPrefsImpl;
import com.tinnhanchuctet.loichuchay.chuctet.database.sqlite.DatabaseHelper;
import com.tinnhanchuctet.loichuchay.chuctet.database.sqlite.TableEntity;
import com.tinnhanchuctet.loichuchay.chuctet.databinding.DialogCfDelBinding;
import com.tinnhanchuctet.loichuchay.chuctet.listeners.DeleteCallBack;
import com.tinnhanchuctet.loichuchay.chuctet.models.Message;
import com.tinnhanchuctet.loichuchay.chuctet.ui.activities.MainActivity;
import com.tinnhanchuctet.loichuchay.chuctet.utils.Constant;
import com.tinnhanchuctet.loichuchay.chuctet.utils.Navigator;

public class ConfirmDelFragment extends DialogFragment implements View.OnClickListener {
    private DialogCfDelBinding mDelBinding;
    private boolean mIsDelMsg = false;
    private Message mMMessage;
    private MainActivity mMainActivity;
    private DeleteCallBack mCallBack;
    private Navigator mNav;
    private SharedPrefsImpl mSharedPref;
    private String mTblName;

    public static ConfirmDelFragment getInstance(Message message, String tbl) {
        ConfirmDelFragment fragment = new ConfirmDelFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constant.ARG_MESSAGE, message);
        args.putString(Constant.ARG_TABLE_NAME, tbl);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDelBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_cf_del, container, false);
        initUI();
        return mDelBinding.getRoot();
    }

    private void initUI() {
        mNav = new Navigator(mMainActivity);
        mSharedPref = new SharedPrefsImpl(mMainActivity);
        Bundle bundle = getArguments();
        mMMessage = bundle.getParcelable(Constant.ARG_MESSAGE);
        mTblName = bundle.getString(Constant.ARG_TABLE_NAME);
        mDelBinding.btnOk.setOnClickListener(this);
        mDelBinding.btnCancel.setOnClickListener(this);
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
                mDelBinding.btnOk.setBackgroundResource(R.drawable.background_button_option);
                mDelBinding.btnOk.setTextColor(mMainActivity.getResources().getColor(R.color.colorWhite));
                if (deleteMsg(TableEntity.TBL_MY_MESSAGE, mMMessage) > 0) {
                    mCallBack.deleteSuccess(mMMessage);
                    mNav.showToast(R.string.delete_success);
                } else {
                    mNav.showToast(R.string.delete_failed);
                }
                dismiss();
                break;
            case R.id.btn_cancel:
                mDelBinding.btnCancel.setBackgroundResource(R.drawable.background_button_option);
                mDelBinding.btnCancel.setTextColor(mMainActivity.getResources().getColor(R.color.colorWhite));
                dismiss();
                break;
        }
    }

    public void setCallBack(DeleteCallBack callBack) {
        mCallBack = callBack;
    }

    private int deleteMsg(String tblName, Message message) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(MyApplication.getInstance());
        return databaseHelper.deleteMessage(tblName, message.getId());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }
}
