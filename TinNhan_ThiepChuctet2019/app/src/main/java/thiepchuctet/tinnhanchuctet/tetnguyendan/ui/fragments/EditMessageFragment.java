package thiepchuctet.tinnhanchuctet.tetnguyendan.ui.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import thiepchuctet.tinnhanchuctet.tetnguyendan.R;
import thiepchuctet.tinnhanchuctet.tetnguyendan.databinding.FragmentMsgEditBinding;
import thiepchuctet.tinnhanchuctet.tetnguyendan.models.Message;
import thiepchuctet.tinnhanchuctet.tetnguyendan.ui.activities.MainActivity;
import thiepchuctet.tinnhanchuctet.tetnguyendan.utils.Constant;
import thiepchuctet.tinnhanchuctet.tetnguyendan.utils.Navigator;

public class EditMessageFragment extends Fragment implements View.OnClickListener {

    private FragmentMsgEditBinding mBinding;
    private MainActivity mMainActivity;
    private Navigator mNavigator;

    public static EditMessageFragment newInstance(Message message) {
        EditMessageFragment fragment = new EditMessageFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constant.ARGUMENT_MSG, message);
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
        mNavigator = new Navigator(mMainActivity);
        Bundle bundle = getArguments();
        Message msg = bundle.getParcelable(Constant.ARGUMENT_MSG);
        mBinding.contentOfMsg.setText(msg.getContent());
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
                mMainActivity.getSupportFragmentManager().popBackStackImmediate();
                break;
            case R.id.btn_home:
                mNavigator.startActivity(MainActivity.class, Navigator.ActivityTransition.FINISH);
                break;
            case R.id.btn_copy:
                copyTextToClipbroad();
                break;
            case R.id.btn_share:
                shareMessage();
                break;
            case R.id.btn_add:
                Toast.makeText(mMainActivity, "chưa làm", Toast.LENGTH_SHORT).show();
                break;
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


}
