package com.tinnhantet.loichuc.chuctet.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tinnhantet.loichuc.chuctet.R;
import com.tinnhantet.loichuc.chuctet.models.Message;
import com.tinnhantet.loichuc.chuctet.utils.Constant;

public class VPFragment extends Fragment {

    public static VPFragment newInstance(Message message) {
        VPFragment fragment = new VPFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constant.ARGUMENT_MSG, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vp_msg_item, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        TextView txtContent = view.findViewById(R.id.content_of_msg);
        Bundle bundle = getArguments();
        Message message = bundle.getParcelable(Constant.ARGUMENT_MSG);
        txtContent.setText(message.getContent());
        txtContent.setMovementMethod(new ScrollingMovementMethod());
    }
}
