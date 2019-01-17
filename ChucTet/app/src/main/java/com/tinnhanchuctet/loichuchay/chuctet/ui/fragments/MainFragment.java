package com.tinnhanchuctet.loichuchay.chuctet.ui.fragments;

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
import com.tinnhanchuctet.loichuchay.chuctet.MyApplication;
import com.tinnhanchuctet.loichuchay.chuctet.R;
import com.tinnhanchuctet.loichuchay.chuctet.database.sharedprf.SharedPrefsImpl;
import com.tinnhanchuctet.loichuchay.chuctet.database.sqlite.DatabaseHelper;
import com.tinnhanchuctet.loichuchay.chuctet.database.sqlite.TableEntity;
import com.tinnhanchuctet.loichuchay.chuctet.databinding.FragmentMainBinding;
import com.tinnhanchuctet.loichuchay.chuctet.models.Message;
import com.tinnhanchuctet.loichuchay.chuctet.ui.activities.MainActivity;
import com.tinnhanchuctet.loichuchay.chuctet.utils.Navigator;

import java.util.List;
import java.util.Objects;

public class MainFragment extends Fragment implements View.OnClickListener {

    private FragmentMainBinding mMainBinding;
    private Navigator mNav;
    private MainActivity mMainActivity;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        initUI();
        return mMainBinding.getRoot();
    }

    private void initUI() {
        Glide.with(this)
                .load(R.drawable.bg_2)
                .into(mMainBinding.imgBackground);
        Glide.with(this)
                .load(R.drawable.ic_cover)
                .into(mMainBinding.daoFlower);
        Glide.with(this)
                .load(R.drawable.ic_pig)
                .into(mMainBinding.imgIconApp);
        Glide.with(this)
                .load(R.drawable.bg_cover)
                .into(mMainBinding.imgBanner);
        List<Message> mMessages = DatabaseHelper.getInstance(MyApplication.getInstance()).getListMsg(TableEntity.TBL_MY_MESSAGE);
        new SharedPrefsImpl(MyApplication.getInstance()).putListMsg(mMessages);
        mNav = new Navigator(Objects.requireNonNull(getActivity()));
        Typeface font = Typeface.createFromAsset(mMainActivity.getAssets(), "fonts/font_tieude.otf");
        mMainBinding.txtTitle.setTypeface(font);
        mMainActivity.ads();
        //mMainActivity.ads();
        mMainBinding.layoutMsgLib.setOnClickListener(this);
        mMainBinding.layoutMsgMine.setOnClickListener(this);
        mMainBinding.layoutRate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_msg_lib:
                LibraryFragment fragment = LibraryFragment.newInstance();
                mNav.addFragment(R.id.main_container, fragment, true, Navigator.NavigateAnim.RIGHT_LEFT, LibraryFragment.class.getSimpleName());
                break;
            case R.id.layout_msg_mine:
                MineFragment f = MineFragment.newInstance();
                mNav.addFragment(R.id.main_container, f, true, Navigator.NavigateAnim.RIGHT_LEFT, MineFragment.class.getSimpleName());
                break;
            case R.id.layout_rate:
                Uri myUri = Uri.parse("market://details?id=" + MyApplication.getInstance().getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(myUri);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }
}
