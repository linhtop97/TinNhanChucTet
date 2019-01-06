package com.tinnhantet.nhantin.hengio.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.database.sharedprf.SharedPrefsImpl;
import com.tinnhantet.nhantin.hengio.listeners.OnDataClickListener;
import com.tinnhantet.nhantin.hengio.models.Contact;
import com.tinnhantet.nhantin.hengio.models.Message;
import com.tinnhantet.nhantin.hengio.utils.DateTimeUtil;
import com.tinnhantet.nhantin.hengio.utils.StringUtils;

import java.util.List;

public class MessageScheduleAdapter extends RecyclerView.Adapter<MessageScheduleAdapter.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private List<Message> mMessages;
    private Context mContext;
    private OnDataClickListener<Message> mListener;
    private SharedPrefsImpl mSharedPrefs;

    public MessageScheduleAdapter(Context context, List<Message> messages) {
        mMessages = messages;
        mContext = context;
        mSharedPrefs = new SharedPrefsImpl(mContext);
    }

    public void setOnDataListener(OnDataClickListener listener) {
        mListener = listener;
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


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextTime;
        private TextView mTextSendTo;
        private TextView mTextContent;

        ViewHolder(View itemView) {
            super(itemView);
            mTextTime = itemView.findViewById(R.id.txt_time_send);
            mTextSendTo = itemView.findViewById(R.id.txt_send_to);
            mTextContent = itemView.findViewById(R.id.txt_content);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    mListener.onItemClick(mMessages.get(pos), pos);
                }
            });
        }

        void bindData(Message message) {
            if (message == null) return;
            List<Contact> contacts = StringUtils.getAllContact(message.getListContact());
            String name = StringUtils.getAllNameContact(contacts);
            String content = message.getContent();
            String dateTime[] = DateTimeUtil.separateTime(Long.valueOf(message.getTime()));
            StringBuilder builder = new StringBuilder();
            builder.append("Vào lúc ").append(dateTime[0]).append(" Giờ : ").append(dateTime[1]).append(" Phút ")
                    .append("Ngày ").append(dateTime[2]).append("/").append(dateTime[3]).append("/").append(dateTime[4]);
            mTextTime.setText(builder);
            mTextContent.setText("Nội dung: " + content);
            mTextSendTo.setText("Gửi đến: " + name);
        }
    }
}
