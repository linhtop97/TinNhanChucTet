package com.ledbanner.ledmobile.ui.dialogs;

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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.ledbanner.ledmobile.R;
import com.ledbanner.ledmobile.adapters.ColorAdapter;
import com.ledbanner.ledmobile.data.local.sharedprf.SharedPrefsImpl;
import com.ledbanner.ledmobile.data.local.sharedprf.SharedPrefsKey;
import com.ledbanner.ledmobile.databinding.FragmentColorBinding;
import com.ledbanner.ledmobile.listeners.OnItemClickListener;
import com.ledbanner.ledmobile.ui.actvitities.SettingActivity;
import com.ledbanner.ledmobile.utils.Constans;

import java.util.Arrays;
import java.util.List;

public class ColorFragment extends DialogFragment implements OnItemClickListener {
    private FragmentColorBinding mBinding;
    private List<Integer> mListColor;
    public static Integer[] mArraysColor = new Integer[]{R.color.color1, R.color.color2, R.color.color3,
            R.color.color4, R.color.color5, R.color.color6, R.color.color7, R.color.color8,
            R.color.color9, R.color.color10, R.color.color11, R.color.color12,
            R.color.color13, R.color.color14, R.color.color15, R.color.color16};
    private boolean isSetTextColor;
    private SettingActivity mSettingActivity;
    private SharedPrefsImpl mSharedPrefs;

    public static ColorFragment getInstance(Boolean isSetTextColor) {
        ColorFragment fragment = new ColorFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constans.ARGUMENT_IS_SET_TEXT_COLOR, isSetTextColor);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mListColor = Arrays.asList(mArraysColor);
        mSharedPrefs = new SharedPrefsImpl(mSettingActivity);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_color, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
    }

    private void initUI() {
        isSetTextColor = getArguments().getBoolean(Constans.ARGUMENT_IS_SET_TEXT_COLOR);
        ColorAdapter adapter = new ColorAdapter(getContext(), mListColor, isSetTextColor);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 4);
        mBinding.rvColor.setLayoutManager(linearLayoutManager);
        adapter.setListener(this);
        mBinding.rvColor.setAdapter(adapter);
        mBinding.getRoot().findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
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
    public void onItemClick(int position) {
        if (isSetTextColor) {
           // mSharedPrefs.put(SharedPrefsKey.PREF_TEXT_COLOR_POS, position);
            mSettingActivity.setTextColor(mListColor.get(position));
//            dismiss();
            return;
        }
        mSettingActivity.setBGColor(mListColor.get(position));
       // mSharedPrefs.put(SharedPrefsKey.PREF_BG_COLOR_POS, position);
//        dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSettingActivity = (SettingActivity) context;
    }
}
