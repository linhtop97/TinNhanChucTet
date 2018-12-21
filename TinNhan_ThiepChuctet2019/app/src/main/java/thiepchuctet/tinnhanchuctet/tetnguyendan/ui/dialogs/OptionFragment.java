package thiepchuctet.tinnhanchuctet.tetnguyendan.ui.dialogs;

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
import android.widget.Toast;

import java.util.List;

import thiepchuctet.tinnhanchuctet.tetnguyendan.MyApplication;
import thiepchuctet.tinnhanchuctet.tetnguyendan.R;
import thiepchuctet.tinnhanchuctet.tetnguyendan.database.sharedprf.SharedPrefsImpl;
import thiepchuctet.tinnhanchuctet.tetnguyendan.database.sqlite.DatabaseHelper;
import thiepchuctet.tinnhanchuctet.tetnguyendan.database.sqlite.TableEntity;
import thiepchuctet.tinnhanchuctet.tetnguyendan.databinding.FragmentOptionBinding;
import thiepchuctet.tinnhanchuctet.tetnguyendan.listeners.DeleteCallBack;
import thiepchuctet.tinnhanchuctet.tetnguyendan.models.Message;
import thiepchuctet.tinnhanchuctet.tetnguyendan.ui.activities.MainActivity;
import thiepchuctet.tinnhanchuctet.tetnguyendan.ui.fragments.MineFragment;
import thiepchuctet.tinnhanchuctet.tetnguyendan.utils.Constant;
import thiepchuctet.tinnhanchuctet.tetnguyendan.utils.Navigator;

public class OptionFragment extends DialogFragment implements View.OnClickListener {
    private FragmentOptionBinding mBinding;
    private boolean mIsDeleteMsg = false;
    private Message mMessage;
    private MainActivity mMainActivity;
    private DeleteCallBack mDeleteCallBack;
    private Navigator mNavigator;
    private SharedPrefsImpl mSharedPrefs;

    public static OptionFragment getInstance(Message message) {
        OptionFragment fragment = new OptionFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constant.ARGUMENT_MSG, message);
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
        mMessage = getArguments().getParcelable(Constant.ARGUMENT_MSG);
        mBinding.btnOK.setOnClickListener(this);
        mBinding.btnCancel.setOnClickListener(this);
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
            case R.id.btn_OK:
                if (mIsDeleteMsg) {
                    if (deleteMsg(TableEntity.TBL_GENERAL, mMessage) > 0) {
                        mDeleteCallBack.deleteSuccess(mMessage);
                        Toast.makeText(mMainActivity, R.string.delete_success, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    insertMsg(mMessage);
                }
                dismiss();
                break;
            case R.id.btn_Cancel:
                dismiss();
                break;
            case R.id.txt_add_collection:
                mBinding.txtAddCollection.setBackgroundResource(R.drawable.background_button_option);
                mBinding.txtDeleteMsg.setBackgroundResource(R.drawable.background_button_option_unselect);
                mBinding.txtDeleteMsg.setTextColor(mMainActivity.getResources().getColor(R.color.colorText));
                mBinding.txtAddCollection.setTextColor(mMainActivity.getResources().getColor(R.color.colorWhite));

                mIsDeleteMsg = false;
                break;
            case R.id.txt_delete_msg:
                mBinding.txtDeleteMsg.setBackgroundResource(R.drawable.background_button_option);
                mBinding.txtAddCollection.setBackgroundResource(R.drawable.background_button_option_unselect);
                mBinding.txtDeleteMsg.setTextColor(mMainActivity.getResources().getColor(R.color.colorWhite));
                mBinding.txtAddCollection.setTextColor(mMainActivity.getResources().getColor(R.color.colorText));
                mIsDeleteMsg = true;
                break;
        }
    }

    private void insertMsg(Message message) {
        List<Message> mMessageList = mSharedPrefs.getListMsg();
        int size = mMessageList.size();
        String msg = message.getContent();
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
            MineFragment mineFragment = MineFragment.newInstance();
            mNavigator.addFragment(R.id.main_container, mineFragment, false, Navigator.NavigateAnim.RIGHT_LEFT, MineFragment.class.getSimpleName());
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
}
