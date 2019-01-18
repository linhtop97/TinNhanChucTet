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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;

import com.tinnhanchuctet.loichuchay.chuctet.R;
import com.tinnhanchuctet.loichuchay.chuctet.database.sharedprf.SharedPrefsImpl;
import com.tinnhanchuctet.loichuchay.chuctet.database.sharedprf.SharedPrefsKey;
import com.tinnhanchuctet.loichuchay.chuctet.databinding.FragmentGuideFixBinding;
import com.tinnhanchuctet.loichuchay.chuctet.ui.activities.MainActivity;

public class GuideFragment extends DialogFragment implements View.OnClickListener {
    private FragmentGuideFixBinding mGuideFixBinding;
    private MainActivity mMainActivity;
    private SharedPrefsImpl mPrefs;
    private Boolean mIsCheckedClick = false;

    public static GuideFragment getInstance() {
        GuideFragment f = new GuideFragment();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mGuideFixBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_guide_fix, container, false);
        initUI();
        return mGuideFixBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        int width = getResources().getDimensionPixelSize(R.dimen._240sdp);
        int height = getResources().getDimensionPixelSize(R.dimen._135sdp);
        window.setLayout(width, height);
        window.setGravity(Gravity.CENTER);
    }

    private void initUI() {
        mPrefs = new SharedPrefsImpl(mMainActivity);
        mGuideFixBinding.btnOK.setOnClickListener(this);
        mGuideFixBinding.ckRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIsCheckedClick = isChecked;
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
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
                mPrefs.put(SharedPrefsKey.PREF_REMEMBER_GUIDE, mIsCheckedClick);
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
