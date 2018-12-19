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

import thiepchuctet.tinnhanchuctet.tetnguyendan.MyApplication;
import thiepchuctet.tinnhanchuctet.tetnguyendan.R;
import thiepchuctet.tinnhanchuctet.tetnguyendan.database.sqlite.DatabaseHelper;
import thiepchuctet.tinnhanchuctet.tetnguyendan.database.sqlite.TableEntity;
import thiepchuctet.tinnhanchuctet.tetnguyendan.databinding.FragmentOptionBinding;
import thiepchuctet.tinnhanchuctet.tetnguyendan.listeners.DeleteCallBack;
import thiepchuctet.tinnhanchuctet.tetnguyendan.models.Message;
import thiepchuctet.tinnhanchuctet.tetnguyendan.ui.activities.MainActivity;
import thiepchuctet.tinnhanchuctet.tetnguyendan.utils.Constant;

public class OptionFragment extends DialogFragment implements View.OnClickListener {
    private FragmentOptionBinding mBinding;
    private boolean mIsDeleteMsg = false;
    private Message mMessage;
    private MainActivity mMainActivity;
    private DeleteCallBack mDeleteCallBack;

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
                mBinding.txtAddCollection.setBackgroundColor(mMainActivity.getResources().getColor(R.color.colorOptionBG));
                mBinding.txtDeleteMsg.setBackgroundColor(0);
                mIsDeleteMsg = false;
                break;
            case R.id.txt_delete_msg:
                mBinding.txtDeleteMsg.setBackgroundColor(mMainActivity.getResources().getColor(R.color.colorOptionBG));
                mBinding.txtAddCollection.setBackgroundColor(0);
                mIsDeleteMsg = true;
                break;
        }
    }

    private void insertMsg(Message message) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(MyApplication.getInstance());
        databaseHelper.insertNewMessage(message.getContent());
        Toast.makeText(mMainActivity, R.string.add_success, Toast.LENGTH_SHORT).show();
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
