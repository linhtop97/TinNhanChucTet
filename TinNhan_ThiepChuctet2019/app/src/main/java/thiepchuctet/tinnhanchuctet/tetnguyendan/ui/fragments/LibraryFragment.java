package thiepchuctet.tinnhanchuctet.tetnguyendan.ui.fragments;

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

import java.util.List;

import thiepchuctet.tinnhanchuctet.tetnguyendan.R;
import thiepchuctet.tinnhanchuctet.tetnguyendan.adapters.CategoryAdapter;
import thiepchuctet.tinnhanchuctet.tetnguyendan.database.sqlite.TableEntity;
import thiepchuctet.tinnhanchuctet.tetnguyendan.databinding.FragmentMsgLibBinding;
import thiepchuctet.tinnhanchuctet.tetnguyendan.listeners.OnCategoryClickListener;
import thiepchuctet.tinnhanchuctet.tetnguyendan.models.Category;
import thiepchuctet.tinnhanchuctet.tetnguyendan.ui.activities.MainActivity;
import thiepchuctet.tinnhanchuctet.tetnguyendan.utils.Navigator;

public class LibraryFragment extends Fragment implements OnCategoryClickListener, View.OnClickListener {

    private FragmentMsgLibBinding mBinding;
    private MainActivity mMainActivity;
    private Navigator mNavigator;
    LinearLayoutManager mLinearLayoutManager;

    public static LibraryFragment newInstance() {
        return new LibraryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_msg_lib, container, false);
        initUI();
        return mBinding.getRoot();
    }

    private void initUI() {
        mNavigator = new Navigator(mMainActivity);
        List<Category> categories = Category.initCategory();
        mLinearLayoutManager = new LinearLayoutManager(mMainActivity);
        CategoryAdapter categoryAdapter = new CategoryAdapter(mMainActivity, categories);
        mBinding.btnBack.setOnClickListener(this);
        categoryAdapter.setOnItemClick(this);
        mBinding.recyclerView.setLayoutManager(mLinearLayoutManager);
        mBinding.recyclerView.setAdapter(categoryAdapter);
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
        mNavigator.addFragment(R.id.main_container, listMsgFragment, true,
                Navigator.NavigateAnim.NONE, ListMsgFragment.class.getSimpleName());
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
        }
    }
}
