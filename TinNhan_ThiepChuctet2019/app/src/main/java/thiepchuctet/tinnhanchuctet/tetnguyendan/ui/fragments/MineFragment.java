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
import thiepchuctet.tinnhanchuctet.tetnguyendan.databinding.FragmentMsgMineBinding;
import thiepchuctet.tinnhanchuctet.tetnguyendan.listeners.OnItemClickListener;
import thiepchuctet.tinnhanchuctet.tetnguyendan.models.Message;
import thiepchuctet.tinnhanchuctet.tetnguyendan.ui.activities.MainActivity;
import thiepchuctet.tinnhanchuctet.tetnguyendan.utils.Navigator;

public class MineFragment extends Fragment implements OnItemClickListener<Message>, View.OnClickListener {

    private FragmentMsgMineBinding mBinding;
    private List<Message> mMessages;
    private MainActivity mMainActivity;
    private Navigator mNavigator;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_msg_mine, container, false);
        initUI();
        return mBinding.getRoot();
    }

    private void initUI() {
        mNavigator = new Navigator(mMainActivity);
        mBinding.btnBack.setOnClickListener(this);
        mBinding.btnAddNew.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mMainActivity);
        mMessages = DatabaseHelper.getInstance(MyApplication.getInstance()).getListMsg(TableEntity.TBL_MY_MESSAGE);
        if (mMessages.size() == 0) {
            mBinding.txtNone.setVisibility(View.VISIBLE);
        } else {
            mBinding.txtNone.setVisibility(View.GONE);
        }
        MessageAdapter adapter = new MessageAdapter(mMainActivity, mMessages);
        adapter.setOnItemClick(this);
        mBinding.recyclerView.setLayoutManager(linearLayoutManager);
        mBinding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(Message data) {

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
            case R.id.btn_add_new:
                EditMessageFragment fragment = EditMessageFragment.newInstance(new Message(0, ""));
                mNavigator.addFragment(R.id.main_container, fragment, true,
                        Navigator.NavigateAnim.NONE, EditMessageFragment.class.getSimpleName());
                break;
        }
    }
}
