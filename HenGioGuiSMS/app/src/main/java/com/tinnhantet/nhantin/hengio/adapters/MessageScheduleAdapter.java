package com.tinnhantet.nhantin.hengio.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.listeners.OnDataClickListener;
import com.tinnhantet.nhantin.hengio.listeners.OnItemLongClickListener;
import com.tinnhantet.nhantin.hengio.models.Contact;
import com.tinnhantet.nhantin.hengio.models.Message;
import com.tinnhantet.nhantin.hengio.ui.activities.MainActivity;
import com.tinnhantet.nhantin.hengio.ui.fragments.PendingFragment;
import com.tinnhantet.nhantin.hengio.ui.fragments.SentFragment;
import com.tinnhantet.nhantin.hengio.utils.DateTimeUtil;
import com.tinnhantet.nhantin.hengio.utils.StringUtils;

import java.util.List;

public class MessageScheduleAdapter extends RecyclerView.Adapter<MessageScheduleAdapter.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private List<Message> mMessages;
    private Context mContext;
    private OnDataClickListener<Message> mListener;
    private OnItemLongClickListener<String> mItemLongClickListener;
    private boolean isShowCheckBox;
    private boolean mIsSelectAll;
    private int mSize;
    private MainActivity mMainActivity;
    private boolean mIsPending;

    public MessageScheduleAdapter(Context context, List<Message> messages, boolean isShow, boolean isPending) {
        mMessages = messages;
        mIsPending = isPending;
        mSize = mMessages.size();
        mContext = context;
        mMainActivity = (MainActivity) context;
        isShowCheckBox = isShow;
    }

    public void setOnDataListener(OnDataClickListener listener) {
        mListener = listener;
    }

    public void setOnLongItemClickListner(OnItemLongClickListener<String> listner) {
        mItemLongClickListener = listner;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }
        View v = mLayoutInflater.inflate(R.layout.item_msg_schedule, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.bindData(mMessages.get(position));
        Message message = mMessages.get(position);
        holder.mCheckBox.setChecked(message.getSelected());
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.contains("payload")) {
            Message message = mMessages.get(position);
            holder.mCheckBox.setChecked(message.getSelected());
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    public void setMessages(List<Message> messages) {
        mMessages = messages;
        notifyDataSetChanged();
    }

    public List<Message> getMessages() {
        return mMessages;
    }

    @Override
    public int getItemCount() {
        return mMessages != null ? mMessages.size() : 0;
    }

    public void setSelectedAll() {
        for (int i = 0; i < mSize; i++) {
            mMessages.get(i).setSelected(true);
        }
        mIsSelectAll = true;
        notifyDataSetChanged();
    }

    public void removeSelectedAll() {
        for (int i = 0; i < mSize; i++) {
            mMessages.get(i).setSelected(false);
        }
        mIsSelectAll = false;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextTime;
        private TextView mTextSendTo;
        private TextView mTextContent;
        private CheckBox mCheckBox;

        ViewHolder(View itemView) {
            super(itemView);
            mTextTime = itemView.findViewById(R.id.txt_time_send);
            mTextSendTo = itemView.findViewById(R.id.txt_send_to);
            mTextContent = itemView.findViewById(R.id.txt_content);
            mCheckBox = itemView.findViewById(R.id.cb_select_msg);
            Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/italic.ttf");
            mTextTime.setTypeface(font);
            if (isShowCheckBox) {
                mCheckBox.setVisibility(View.VISIBLE);
                mCheckBox.setClickable(false);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = getAdapterPosition();
                        Boolean b = mCheckBox.isChecked();
                        mCheckBox.setChecked(!b);
                        mMessages.get(pos).setSelected(!b);
                        int num = 0;
                        for (int i = 0; i < mSize; i++) {
                            if (mMessages.get(i).getSelected()) {
                                num++;
                            }
                        }
                        if (num == mSize) {
                            FragmentManager manager = mMainActivity.getSupportFragmentManager();
                            Fragment fragment = manager.findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + 0);
                            if (fragment != null) {
                                PendingFragment pendingFragment = (PendingFragment) fragment;
                                pendingFragment.setTextUnSelectAll();
                            }
                            Fragment fragment2 = manager.findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + 1);
                            if (fragment2 != null) {
                                SentFragment sentFragment = (SentFragment) fragment2;
                                sentFragment.setTextUnSelectAll();
                            }
                        }
                        if (num == 0) {
                            FragmentManager manager = mMainActivity.getSupportFragmentManager();
                            Fragment fragment = manager.findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + 0);
                            if (fragment != null) {
                                PendingFragment pendingFragment = (PendingFragment) fragment;
                                pendingFragment.setTextSelectAll();
                            }
                            Fragment fragment2 = manager.findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + 1);
                            if (fragment2 != null) {
                                SentFragment sentFragment = (SentFragment) fragment2;
                                sentFragment.setTextSelectAll();
                            }
                        }
                        notifyItemChanged(pos, "changed");

                    }
                });
            } else {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = getAdapterPosition();
                        mListener.onItemClick(mMessages.get(pos), pos);
                    }
                });
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mItemLongClickListener.onItemLongClick("");
                        return true;
                    }
                });
            }
        }

        void bindData(Message message) {
            if (message == null) return;
            List<Contact> contacts = StringUtils.getAllContact(message.getListContact());
            String name = StringUtils.getAllNameContact(contacts);
            String content = message.getContent();
            String dateTime[] = DateTimeUtil.separateTime(Long.valueOf(message.getTime()));
            StringBuilder builder = new StringBuilder();
            if (mIsPending) {
                builder.append("Sẽ gửi : ");
            } else {
                builder.append("Đã gửi : ");
            }
            builder.append(dateTime[0]).append(" giờ ").append(dateTime[1]).append(" phút ")
                    .append("Ngày : ").append(dateTime[2]).append("/").append(dateTime[3]).append("/").append(dateTime[4]);
            mTextTime.setText(builder);
            mTextContent.setText(content);
            mTextSendTo.setText(name);
        }
    }
}
