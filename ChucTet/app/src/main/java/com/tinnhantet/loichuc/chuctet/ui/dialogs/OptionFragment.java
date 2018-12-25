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
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.tinnhantet.loichuc.chuctet.MyApplication;
import com.tinnhantet.loichuc.chuctet.R;
import com.tinnhantet.loichuc.chuctet.database.sharedprf.SharedPrefsImpl;
import com.tinnhantet.loichuc.chuctet.database.sqlite.DatabaseHelper;
import com.tinnhantet.loichuc.chuctet.databinding.FragmentOptionBinding;
import com.tinnhantet.loichuc.chuctet.listeners.DeleteCallBack;
import com.tinnhantet.loichuc.chuctet.models.Message;
import com.tinnhantet.loichuc.chuctet.ui.activities.MainActivity;
import com.tinnhantet.loichuc.chuctet.ui.fragments.MineFragment;
import com.tinnhantet.loichuc.chuctet.utils.Constant;
import com.tinnhantet.loichuc.chuctet.utils.Navigator;

import java.util.List;

public class OptionFragment extends DialogFragment implements View.OnClickListener {
    private FragmentOptionBinding mBinding;
    private boolean mIsDeleteMsg = false;
    private Message mMessage;
    private MainActivity mMainActivity;
    private DeleteCallBack mDeleteCallBack;
    private Navigator mNavigator;
    private SharedPrefsImpl mSharedPrefs;
    private String tblName;

    public static OptionFragment getInstance(Message message, String tbl) {
        OptionFragment fragment = new OptionFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constant.ARGUMENT_MSG, message);
        args.putString(Constant.ARGUMENT_TBL_NAME, tbl);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_option, container, false);
        initUI();
        return mBinding.getRoot();
    }

    private void initUI() {
        mNavigator = new Navigator(mMainActivity);
        mSharedPrefs = new SharedPrefsImpl(mMainActivity);
        Bundle bundle = getArguments();
        mMessage = bundle.getParcelable(Constant.ARGUMENT_MSG);
        tblName = bundle.getString(Constant.ARGUMENT_TBL_NAME);
        mBinding.txtAddCollection.setOnClickListener(this);
        mBinding.txtDeleteMsg.setOnClickListener(this);
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
            case R.id.txt_add_collection:
                mBinding.txtAddCollection.setBackgroundResource(R.drawable.background_button_option);
                mBinding.txtAddCollection.setTextColor(mMainActivity.getResources().getColor(R.color.colorWhite));
                insertMsg(mMessage);
                dismiss();
                break;
            case R.id.txt_delete_msg:
                mBinding.txtDeleteMsg.setBackgroundResource(R.drawable.background_button_option);
                mBinding.txtDeleteMsg.setTextColor(mMainActivity.getResources().getColor(R.color.colorWhite));
                confirmDelete();
                dismiss();
                break;
        }
    }

    private void insertMsg(Message message) {
        List<Message> mMessageList = mSharedPrefs.getListMsg();
        int size = mMessageList.size();
        String msg = message.getContent();
        boolean ok = true;
        for (int i = 0; i < size; i++) {
            if ((msg.toLowerCase()).equals(mMessageList.get(i).getContent().toLowerCase())) {
                Toast.makeText(mMainActivity, R.string.msg_is_exists, Toast.LENGTH_SHORT).show();
                ok = false;
                break;
            }
        }
        if (ok) {
            DatabaseHelper databaseHelper = DatabaseHelper.getInstance(MyApplication.getInstance());
            databaseHelper.insertNewMessage(msg);
            mNavigator.showToast(R.string.add_success);
            MineFragment mineFragment = MineFragment.newInstance();
            mNavigator.addFragment(R.id.main_container, mineFragment, true, Navigator.NavigateAnim.RIGHT_LEFT, MineFragment.class.getSimpleName());
        }
    }

    private int deleteMsg(String tblName, Message message) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(MyApplication.getInstance());
        return databaseHelper.deleteMessage(tblName, message.getId());
    }

    public void setDeleteCallBack(DeleteCallBack callBack) {
        mDeleteCallBack = callBack;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    private void confirmDelete() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(mMainActivity);
        dialog.setTitle(R.string.notifi);
        dialog.setMessage(R.string.delete_confirm_msg);
        dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (deleteMsg(tblName, mMessage) > 0) {
                    mDeleteCallBack.deleteSuccess(mMessage);
                    Toast.makeText(mMainActivity, R.string.delete_success, Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog.show();
    }
}
