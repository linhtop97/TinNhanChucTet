package thiepchuctet.tinnhanchuctet.tetnguyendan.ui.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import thiepchuctet.tinnhanchuctet.tetnguyendan.MyApplication;
import thiepchuctet.tinnhanchuctet.tetnguyendan.R;
import thiepchuctet.tinnhanchuctet.tetnguyendan.database.sharedprf.SharedPrefsImpl;
import thiepchuctet.tinnhanchuctet.tetnguyendan.database.sharedprf.SharedPrefsKey;
import thiepchuctet.tinnhanchuctet.tetnguyendan.database.sqlite.DatabaseHelper;
import thiepchuctet.tinnhanchuctet.tetnguyendan.databinding.FragmentMsgEditBinding;
import thiepchuctet.tinnhanchuctet.tetnguyendan.listeners.EditMsgSuccessListener;
import thiepchuctet.tinnhanchuctet.tetnguyendan.models.Message;
import thiepchuctet.tinnhanchuctet.tetnguyendan.ui.activities.MainActivity;
import thiepchuctet.tinnhanchuctet.tetnguyendan.utils.Constant;
import thiepchuctet.tinnhanchuctet.tetnguyendan.utils.Navigator;

public class EditMessageFragment extends Fragment implements View.OnClickListener {

    private FragmentMsgEditBinding mBinding;
    private MainActivity mMainActivity;
    private Navigator mNavigator;
    private boolean mIsAddNew;
    private boolean mIsFromMine;
    private Message mMessage;
    private List<Message> mMessageList;
    private SharedPrefsImpl mSharedPrefs;
    private EditMsgSuccessListener mEditMsgSuccessListener;

    public static EditMessageFragment newInstance(Message message, boolean isAddNew, boolean isFromMine) {
        EditMessageFragment fragment = new EditMessageFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constant.ARGUMENT_MSG, message);
        args.putBoolean(Constant.ARGUMENT_IS_ADD_NEW, isAddNew);
        args.putBoolean(Constant.ARGUMENT_IS_FROM_MINE, isFromMine);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_msg_edit, container, false);
        initUI();
        initAction();
        return mBinding.getRoot();
    }

    private void initUI() {
        mSharedPrefs = new SharedPrefsImpl(mMainActivity);
        mMessageList = mSharedPrefs.getListMsg();
        mNavigator = new Navigator(mMainActivity);
        Glide.with(this)
                .load(R.drawable.bg_1)
                .into(mBinding.imgBackground);
        Bundle bundle = getArguments();
        mMessage = bundle.getParcelable(Constant.ARGUMENT_MSG);
        mIsAddNew = bundle.getBoolean(Constant.ARGUMENT_IS_ADD_NEW);
        mIsFromMine = bundle.getBoolean(Constant.ARGUMENT_IS_FROM_MINE);
        mSharedPrefs.put(SharedPrefsKey.KEY__MSG_EDIT, mMessage.getContent());
        mSharedPrefs.put(SharedPrefsKey.KEY_IS_ADD_NEW, mIsAddNew);
        if (!mIsAddNew) {
            mBinding.btnAdd.setText(R.string.done);
        } else {
            mBinding.txtTitle.setText(R.string.add_msg);
        }
        mBinding.contentOfMsg.setText(mMessage.getContent());
    }

    private void initAction() {
        mBinding.btnBack.setOnClickListener(this);
        mBinding.btnHome.setOnClickListener(this);
        mBinding.btnCopy.setOnClickListener(this);
        mBinding.btnShare.setOnClickListener(this);
        mBinding.btnAdd.setOnClickListener(this);
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
                confirmLeave(mIsAddNew, false);
                break;
            case R.id.btn_home:
                confirmLeave(mIsAddNew, true);
                break;
            case R.id.btn_copy:
                copyTextToClipbroad();
                break;
            case R.id.btn_share:
                shareMessage();
                break;
            case R.id.btn_add:
                if (mIsAddNew) {
                    insertNewMessage();
                    return;
                } else {
                    int edit = editMsg();
                    if (edit > 0 && edit != 100) {
                        mNavigator.showToast(R.string.edit_sucess);
                        mEditMsgSuccessListener.msgEdited(mMessage);
                        mMainActivity.getSupportFragmentManager().popBackStackImmediate();
                    }
                }

                break;
        }
    }

    private int editMsg() {
        String msg = mBinding.contentOfMsg.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            mNavigator.showToast(R.string.cannot_empty);
            return 0;
        }
        if (msg.equals(mSharedPrefs.get(SharedPrefsKey.KEY__MSG_EDIT, String.class))) {
            mNavigator.showToast(R.string.msg_not_change);
            return 100;
        }
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(MyApplication.getInstance());
        mMessage.setContent(msg);
        return databaseHelper.updateMessage(mMessage);
    }

    private void insertNewMessage() {
        String msg = mBinding.contentOfMsg.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            mNavigator.showToast(R.string.cannot_empty);
            return;
        }
        int size = mMessageList.size();
        boolean ok = true;
        for (int i = 0; i < size; i++) {
            if (msg.equals(mMessageList.get(i).getContent())) {
                Toast.makeText(mMainActivity, R.string.msg_is_exists, Toast.LENGTH_SHORT).show();
                ok = false;
                break;
            }
        }
        if (ok) {
            DatabaseHelper databaseHelper = DatabaseHelper.getInstance(MyApplication.getInstance());
            databaseHelper.insertNewMessage(msg);
            mNavigator.showToast(R.string.add_success);
            mMainActivity.getSupportFragmentManager().popBackStackImmediate();
            if (!mIsFromMine) {
                MineFragment mineFragment = MineFragment.newInstance();
                mNavigator.addFragment(R.id.main_container, mineFragment, true, Navigator.NavigateAnim.RIGHT_LEFT, MineFragment.class.getSimpleName());
            }
        }
    }

    private void shareMessage() {
        String msg = mBinding.contentOfMsg.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            mNavigator.showToast(R.string.cannot_empty);
            return;
        }
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mBinding.contentOfMsg.getText().toString());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share_with)));

    }

    private void copyTextToClipbroad() {
        String msg = mBinding.contentOfMsg.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            mNavigator.showToast(R.string.cannot_empty);
            return;
        }
        ClipboardManager clipboard = (ClipboardManager) mMainActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(Constant.LABEL_CLIPBROAD, mBinding.contentOfMsg.getText().toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(mMainActivity, R.string.copy_successful, Toast.LENGTH_SHORT).show();
    }

    public void setEditMsgSuccessListener(EditMsgSuccessListener listener) {
        mEditMsgSuccessListener = listener;
    }

    public void confirmLeave(boolean isAddNew, final boolean isHome) {
        boolean check = true;
        String msg = mBinding.contentOfMsg.getText().toString();

        if (msg.equals(mSharedPrefs.get(SharedPrefsKey.KEY__MSG_EDIT, String.class)) || TextUtils.isEmpty(msg)) {
            check = false;
        }
        if (!check) {
            if (isHome) {
                mMainActivity.gotoHomeFragment();
            } else {
                mMainActivity.getSupportFragmentManager().popBackStackImmediate();
            }
            return;
        }
        final AlertDialog.Builder dialog = new AlertDialog.Builder(mMainActivity);
        dialog.setTitle(R.string.leave);
        if (isAddNew) {
            dialog.setMessage(R.string.cancel_add);
        } else {
            dialog.setMessage(R.string.cancel_edit);
        }

        dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (isHome) {
                    mMainActivity.gotoHomeFragment();
                } else {
                    mMainActivity.getSupportFragmentManager().popBackStackImmediate();
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
