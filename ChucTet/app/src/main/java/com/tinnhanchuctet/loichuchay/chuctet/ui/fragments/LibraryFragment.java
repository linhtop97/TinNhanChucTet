package com.tinnhanchuctet.loichuchay.chuctet.ui.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.tinnhanchuctet.loichuchay.chuctet.Ads;
import com.tinnhanchuctet.loichuchay.chuctet.R;
import com.tinnhanchuctet.loichuchay.chuctet.adapters.CategoryAdapter;
import com.tinnhanchuctet.loichuchay.chuctet.database.sqlite.TableEntity;
import com.tinnhanchuctet.loichuchay.chuctet.databinding.FragmentMsgLibBinding;
import com.tinnhanchuctet.loichuchay.chuctet.listeners.OnCategoryClickListener;
import com.tinnhanchuctet.loichuchay.chuctet.models.Category;
import com.tinnhanchuctet.loichuchay.chuctet.ui.activities.MainActivity;
import com.tinnhanchuctet.loichuchay.chuctet.utils.Navigator;

import java.util.List;

public class LibraryFragment extends Fragment implements OnCategoryClickListener, View.OnClickListener {

    private FragmentMsgLibBinding mMsgLibBinding;
    private MainActivity mMainActivity;
    private Navigator mNav;
    LinearLayoutManager mLayoutManager;

    public static LibraryFragment newInstance() {
        return new LibraryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMsgLibBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_msg_lib, container, false);
        initUI();
        return mMsgLibBinding.getRoot();
    }


    private void initUI() {
        Glide.with(this).load(R.drawable.bg_1).into(mMsgLibBinding.imgBackground);
        mNav = new Navigator(mMainActivity);
        List<Category> categoryList = Category.initCategory();
        mLayoutManager = new LinearLayoutManager(mMainActivity);
        CategoryAdapter categoryAdapter = new CategoryAdapter(mMainActivity, categoryList);
        mMsgLibBinding.btnBack.setOnClickListener(this);
        mMsgLibBinding.btnHome.setOnClickListener(this);
        categoryAdapter.setOnItemClick(this);
        mMsgLibBinding.recyclerView.setLayoutManager(mLayoutManager);
        mMsgLibBinding.recyclerView.setAdapter(categoryAdapter);
        Ads.f(mMainActivity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public void onItemClick(Category category) {
        String tblName = "";
        switch (category.getId()) {
            case 1:
                tblName = TableEntity.TBL_GENERAL;
                break;
            case 2:
                tblName = TableEntity.TBL_ONG_DO;
                break;
            case 3:
                tblName = TableEntity.TBL_GIA_DINH;
                break;
            case 4:
                tblName = TableEntity.TBL_THAY_CO;
                break;
            case 5:
                tblName = TableEntity.TBL_SEP;
                break;
            case 6:
                tblName = TableEntity.TBL_VO_CHONG;
                break;
            case 7:
                tblName = TableEntity.TBL_SMS_CUTE;
                break;
        }

        ListMsgFragment listMsgFragment = ListMsgFragment.newInstance(category.getName(), tblName);
        mNav.addFragment(R.id.main_container, listMsgFragment, true,
                Navigator.NavigateAnim.NONE, ListMsgFragment.class.getSimpleName());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                mMainActivity.getSupportFragmentManager().popBackStackImmediate();
                break;
            case R.id.btn_home:
                mMainActivity.getSupportFragmentManager().popBackStackImmediate();
                break;
        }
    }
}
