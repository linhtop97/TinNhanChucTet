package thiepchuctet.tinnhanchuctet.tetnguyendan.ui.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import thiepchuctet.tinnhanchuctet.tetnguyendan.R;
import thiepchuctet.tinnhanchuctet.tetnguyendan.databinding.FragmentMsgBinding;
import thiepchuctet.tinnhanchuctet.tetnguyendan.listeners.DetectSwipeGestureListener;
import thiepchuctet.tinnhanchuctet.tetnguyendan.models.Message;
import thiepchuctet.tinnhanchuctet.tetnguyendan.ui.activities.MainActivity;
import thiepchuctet.tinnhanchuctet.tetnguyendan.utils.Constant;
import thiepchuctet.tinnhanchuctet.tetnguyendan.utils.Navigator;

public class MessageFragment extends Fragment implements View.OnClickListener {

    private FragmentMsgBinding mBinding;
    private MainActivity mMainActivity;
    private List<Message> mMessages;
    private int mPosition;
    private int mNum;
    private Navigator mNavigator;
    private DetectSwipeGestureListener mGestureListener;
    private int mSize;
    private boolean mIsAddNew;

    public static MessageFragment newInstance(List<Message> messages, int position, int num, boolean isAddNew) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(Constant.ARGUMENT_LIST_MSG, (ArrayList<? extends Parcelable>) messages);
        args.putInt(Constant.ARGUMENT_MSG_POS, position);
        args.putInt(Constant.ARGUMENT_NUM, num);
        args.putBoolean(Constant.ARGUMENT_IS_EDIT, isAddNew);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_msg, container, false);
        initUI();
        initAction();
        return mBinding.getRoot();
    }

    private void initUI() {
        mNavigator = new Navigator(mMainActivity);
        Bundle bundle = getArguments();
        mMessages = bundle.getParcelableArrayList(Constant.ARGUMENT_LIST_MSG);
        mPosition = bundle.getInt(Constant.ARGUMENT_MSG_POS);
        mNum = bundle.getInt(Constant.ARGUMENT_NUM);
        mIsAddNew = bundle.getBoolean(Constant.ARGUMENT_IS_EDIT);
        if (mIsAddNew) {
            mBinding.btnEdit.setText(R.string.add_to_my_collect);
        } else {
            mBinding.btnEdit.setText(R.string.edit_msg);
        }
        mBinding.txtTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        mSize = mMessages.size();
        setCurrentMsg(mNum);
        mBinding.contentOfMsg.setMovementMethod(new ScrollingMovementMethod());
        mBinding.contentOfMsg.setText(mMessages.get(mPosition).getContent());
    }

    private void initAction() {
        mBinding.btnBack.setOnClickListener(this);
        mBinding.btnHome.setOnClickListener(this);
        mBinding.btnCopy.setOnClickListener(this);
        mBinding.btnShare.setOnClickListener(this);
        mBinding.btnEdit.setOnClickListener(this);
        mGestureListener = new DetectSwipeGestureListener();
        mGestureListener.setActivity(mMainActivity);
        final GestureDetectorCompat gestureDetectorCompat = new GestureDetectorCompat(mMainActivity, mGestureListener);
        mBinding.contentOfMsg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetectorCompat.onTouchEvent(event);
            }
        });
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
            case R.id.btn_edit:
                EditMessageFragment fragment = EditMessageFragment.newInstance(mMessages.get(mPosition), true);
                mNavigator.addFragment(R.id.main_container, fragment, true,
                        Navigator.NavigateAnim.NONE, EditMessageFragment.class.getSimpleName());
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

    public void onSwipeRight() {
        if (mPosition == mMessages.size() - 1) {
            mNum = 0;
            mPosition = 0;
            mBinding.contentOfMsg.setText(mMessages.get(mPosition).getContent());

        } else {
            mBinding.contentOfMsg.setText(mMessages.get(++mPosition).getContent());
        }
        setCurrentMsg(++mNum);
    }

    public void onSwipeLeft() {
        if (mPosition == 0) {
            mNum = mSize;
            setCurrentMsg(mNum);
            mPosition = mMessages.size() - 1;
            mBinding.contentOfMsg.setText(mMessages.get(mPosition).getContent());
        } else {
            mBinding.contentOfMsg.setText(mMessages.get(--mPosition).getContent());
            setCurrentMsg(--mNum);
        }

    }

    private void setCurrentMsg(int position) {
        mBinding.txtTitle.setText("".concat(position + "/").concat(mMessages.size() + ""));
    }
}
