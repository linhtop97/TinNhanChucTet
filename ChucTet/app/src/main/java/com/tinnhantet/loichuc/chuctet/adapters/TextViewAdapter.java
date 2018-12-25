package com.tinnhantet.loichuc.chuctet.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tinnhantet.loichuc.chuctet.R;
import com.tinnhantet.loichuc.chuctet.models.Message;

import java.util.List;

public class TextViewAdapter extends PagerAdapter {

    private static final String TAG = "TextViewAdapter";
    private List<Message> mMessages;
    private LayoutInflater mInflater;
    private IsetMsg mCallback;

    public TextViewAdapter(Context context, List<Message> messages, IsetMsg callback) {
        mMessages = messages;
        mInflater = LayoutInflater.from(context);
        mCallback = callback;
    }

    @Override
    public int getCount() {
        return mMessages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.d(TAG, "instantiateItem: " + position);
        View view = mInflater.inflate(R.layout.vp_msg_item, container, false);
        TextView textView = view.findViewById(R.id.content_of_msg);
        Message message = mMessages.get(position);
        textView.setText(message.getContent());
        mCallback.setMsg(message);
        textView.setMovementMethod(new ScrollingMovementMethod());
        container.addView(view);
//        if (mCurrentPosition >= mMessages.size() - 1) {
//            mCurrentPosition = 0;
//            return view;
//
//        }
//        mCurrentPosition++;
        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public interface IsetMsg {
        void setMsg(Message message);
    }
}

