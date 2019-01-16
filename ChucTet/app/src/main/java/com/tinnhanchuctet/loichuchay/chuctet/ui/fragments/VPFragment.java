package com.tinnhanchuctet.loichuchay.chuctet.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tinnhanchuctet.loichuchay.chuctet.R;
import com.tinnhanchuctet.loichuchay.chuctet.models.Message;
import com.tinnhanchuctet.loichuchay.chuctet.utils.Constant;

public class VPFragment extends Fragment {

    public static VPFragment newInstance(Message message) {
        VPFragment f = new VPFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constant.ARG_MESSAGE, message);
        f.setArguments(args);
        return f;
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
        Message message = bundle.getParcelable(Constant.ARG_MESSAGE);
        txtContent.setText(message.getContent());
        txtContent.setMovementMethod(new ScrollingMovementMethod());
    }
}
