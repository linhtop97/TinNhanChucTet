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

import com.tinnhantet.loichuc.chuctet.MyApplication;
import com.tinnhantet.loichuc.chuctet.R;
import com.tinnhantet.loichuc.chuctet.database.sharedprf.SharedPrefsImpl;
import com.tinnhantet.loichuc.chuctet.database.sqlite.DatabaseHelper;
import com.tinnhantet.loichuc.chuctet.database.sqlite.TableEntity;
import com.tinnhantet.loichuc.chuctet.databinding.DialogCfDelBinding;
import com.tinnhantet.loichuc.chuctet.listeners.DeleteCallBack;
import com.tinnhantet.loichuc.chuctet.models.Message;
import com.tinnhantet.loichuc.chuctet.ui.activities.MainActivity;
import com.tinnhantet.loichuc.chuctet.utils.Constant;
import com.tinnhantet.loichuc.chuctet.utils.Navigator;

public class ConfirmDelFragment extends DialogFragment implements View.OnClickListener {
    private DialogCfDelBinding mBinding;
    private boolean mIsDeleteMsg = false;
    private Message mMessage;
    private MainActivity mMainActivity;
    private DeleteCallBack mDeleteCallBack;
    private Navigator mNavigator;
    private SharedPrefsImpl mSharedPrefs;
    private String tblName;

    public static ConfirmDelFragment getInstance(Message message, String tbl) {
        ConfirmDelFragment fragment = new ConfirmDelFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constant.ARGUMENT_MSG, message);
        args.putString(Constant.ARGUMENT_TBL_NAME, tbl);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_cf_del, container, false);
        initUI();
        return mBinding.getRoot();
    }

    private void initUI() {
        mNavigator = new Navigator(mMainActivity);
        mSharedPrefs = new SharedPrefsImpl(mMainActivity);
        Bundle bundle = getArguments();
        mMessage = bundle.getParcelable(Constant.ARGUMENT_MSG);
        tblName = bundle.getString(Constant.ARGUMENT_TBL_NAME);
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
                if (deleteMsg(TableEntity.TBL_MY_MESSAGE, mMessage) > 0) {
                    mDeleteCallBack.deleteSuccess(mMessage);
                    mNavigator.showToast(R.string.delete_success);
                } else {
                    mNavigator.showToast(R.string.delete_failed);
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

    public void setDeleteCallBack(DeleteCallBack deleteCallBack) {
        mDeleteCallBack = deleteCallBack;
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
