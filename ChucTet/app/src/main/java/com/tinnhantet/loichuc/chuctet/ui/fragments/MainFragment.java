package com.tinnhantet.loichuc.chuctet.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.tinnhantet.loichuc.chuctet.MyApplication;
import com.tinnhantet.loichuc.chuctet.R;
import com.tinnhantet.loichuc.chuctet.databinding.FragmentMainBinding;
import com.tinnhantet.loichuc.chuctet.ui.activities.MainActivity;
import com.tinnhantet.loichuc.chuctet.utils.Navigator;
import java.util.Objects;

public class MainFragment extends Fragment implements View.OnClickListener {

    private FragmentMainBinding mBinding;
    private Navigator mNavigator;
    private MainActivity mMainActivity;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        initUI();
        return mBinding.getRoot();
    }

    private void initUI() {
        Glide.with(this)
                .load(R.drawable.bg_2)
                .into(mBinding.imgBackground);
        Glide.with(this)
                .load(R.drawable.ic_cover)
                .into(mBinding.daoFlower);
        Glide.with(this)
                .load(R.drawable.ic_pig)
                .into(mBinding.imgIconApp);
        Glide.with(this)
                .load(R.drawable.bg_cover)
                .into(mBinding.imgBanner);
//        List<Message> mMessages = DatabaseHelper.getInstance(MyApplication.getInstance()).getListMsg(TableEntity.TBL_MY_MESSAGE);
//        new SharedPrefsImpl(MyApplication.getInstance()).putListMsg(mMessages);
        mNavigator = new Navigator(Objects.requireNonNull(getActivity()));
        Typeface font = Typeface.createFromAsset(mMainActivity.getAssets(), "fonts/font_tieude.ttf");
        mBinding.txtTitle.setTypeface(font);
        //mMainActivity.ads();
        mBinding.layoutMsgLib.setOnClickListener(this);
        mBinding.layoutMsgMine.setOnClickListener(this);
        mBinding.layoutRate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_msg_lib:
                LibraryFragment libraryFragment = LibraryFragment.newInstance();
                mNavigator.addFragment(R.id.main_container, libraryFragment, true, Navigator.NavigateAnim.RIGHT_LEFT, LibraryFragment.class.getSimpleName());
                break;
            case R.id.layout_msg_mine:
                MineFragment mineFragment = MineFragment.newInstance();
                mNavigator.addFragment(R.id.main_container, mineFragment, true, Navigator.NavigateAnim.RIGHT_LEFT, MineFragment.class.getSimpleName());
                break;
            case R.id.layout_rate:
                Uri uri = Uri.parse("market://details?id=" + MyApplication.getInstance().getPackageName());
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(uri);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }
}
