package com.tinnhanchuctet.loichuchay.chuctet.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.tinnhanchuctet.loichuchay.chuctet.MyApplication;
import com.tinnhanchuctet.loichuchay.chuctet.R;
import com.tinnhanchuctet.loichuchay.chuctet.databinding.FragmentGuideRequestBinding;
import com.tinnhanchuctet.loichuchay.chuctet.ui.activities.MainActivity;

public class GuideRequestFragment extends DialogFragment implements View.OnClickListener {
    private FragmentGuideRequestBinding mGuideRequestBinding;
    private MainActivity mMainActivity;

    public static GuideRequestFragment getInstance() {
        GuideRequestFragment fragment = new GuideRequestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mGuideRequestBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_guide_request, container, false);
        initUI();
        return mGuideRequestBinding.getRoot();
    }

    private void initUI() {
        mGuideRequestBinding.btnOk.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        int width = getResources().getDimensionPixelSize(R.dimen._220sdp);
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
            case R.id.btn_ok:
                Intent intent = new Intent();
                intent.setAction("RequestPMS");
                LocalBroadcastManager.getInstance(MyApplication.getInstance()).sendBroadcast(intent);
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
