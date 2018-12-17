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

import thiepchuctet.tinnhanchuctet.tetnguyendan.MyApplication;
import thiepchuctet.tinnhanchuctet.tetnguyendan.R;
import thiepchuctet.tinnhanchuctet.tetnguyendan.adapters.MessageAdapter;
import thiepchuctet.tinnhanchuctet.tetnguyendan.database.sqlite.DatabaseHelper;
import thiepchuctet.tinnhanchuctet.tetnguyendan.database.sqlite.TableEntity;
import thiepchuctet.tinnhanchuctet.tetnguyendan.databinding.FragmentMsgListBinding;
import thiepchuctet.tinnhanchuctet.tetnguyendan.listeners.OnItemClickListener;
import thiepchuctet.tinnhanchuctet.tetnguyendan.models.Message;
import thiepchuctet.tinnhanchuctet.tetnguyendan.ui.activities.MainActivity;
import thiepchuctet.tinnhanchuctet.tetnguyendan.utils.Constant;
import thiepchuctet.tinnhanchuctet.tetnguyendan.utils.Navigator;

public class ListMsgFragment extends Fragment implements OnItemClickListener<Message>, View.OnClickListener {

    private FragmentMsgListBinding mBinding;
    private MainActivity mMainActivity;
    private Navigator mNavigator;
    private List<Message> mMessages;

    public static ListMsgFragment newInstance(String categoryName) {
        ListMsgFragment fragment = new ListMsgFragment();
        Bundle args = new Bundle();
        args.putString(Constant.ARGUMENT_CATEGORY_NAME, categoryName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_msg_list, container, false);
        initUI();
        return mBinding.getRoot();
    }

    private void initUI() {
        mNavigator = new Navigator(mMainActivity);
        mBinding.txtTitle.setText(getArguments().getString(Constant.ARGUMENT_CATEGORY_NAME));
        mBinding.btnHome.setOnClickListener(this);
        mBinding.btnBack.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mMainActivity);
        mMessages = DatabaseHelper.getInstance(MyApplication.getInstance()).getListMsg(TableEntity.TBL_GENERAL);
        MessageAdapter adapter = new MessageAdapter(mMainActivity, mMessages);
        adapter.setOnItemClick(this);
        mBinding.recyclerView.setLayoutManager(linearLayoutManager);
        mBinding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public void onItemClick(Message data) {
        MessageFragment messageFragment = MessageFragment.newInstance(mMessages, data.getId());
        mNavigator.addFragment(R.id.main_container, messageFragment, true,
                Navigator.NavigateAnim.NONE, MessageFragment.class.getSimpleName());
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
