package thiepchuctet.tinnhanchuctet.tetnguyendan.ui.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import thiepchuctet.tinnhanchuctet.tetnguyendan.MyApplication;
import thiepchuctet.tinnhanchuctet.tetnguyendan.R;
import thiepchuctet.tinnhanchuctet.tetnguyendan.database.sharedprf.SharedPrefsImpl;
import thiepchuctet.tinnhanchuctet.tetnguyendan.database.sqlite.DatabaseHelper;
import thiepchuctet.tinnhanchuctet.tetnguyendan.database.sqlite.TableEntity;
import thiepchuctet.tinnhanchuctet.tetnguyendan.databinding.FragmentMainBinding;
import thiepchuctet.tinnhanchuctet.tetnguyendan.models.Message;
import thiepchuctet.tinnhanchuctet.tetnguyendan.utils.Navigator;

public class MainFragment extends Fragment implements View.OnClickListener {

    private FragmentMainBinding mBinding;
    private Navigator mNavigator;

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
        List<Message> mMessages = DatabaseHelper.getInstance(MyApplication.getInstance()).getListMsg(TableEntity.TBL_MY_MESSAGE);
        new SharedPrefsImpl(MyApplication.getInstance()).putListMsg(mMessages);
        mNavigator = new Navigator(Objects.requireNonNull(getActivity()));
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
                mNavigator.showToast("Sẽ đánh giá ứng dụng sau");
                break;
        }
    }
}
