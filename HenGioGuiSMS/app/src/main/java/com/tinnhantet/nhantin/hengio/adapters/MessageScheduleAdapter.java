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

import java.text.SimpleDateFormat;
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

    public void setOnContactListener(OnDataClickListener listener) {
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
        private TextView mTextConent;

        ViewHolder(View itemView) {
            super(itemView);
            mTextTime = itemView.findViewById(R.id.txt_time_send);
            mTextSendTo = itemView.findViewById(R.id.txt_send_to);
            mTextConent = itemView.findViewById(R.id.txt_content);
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
            List<Contact> contacts = mSharedPrefs.getAllContact(message.getListContact());
            String content = message.getContent();
            String myFormat = "hh:mm dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
            String datetime = (sdf.format(Long.parseLong(message.getTime())));
            mTextTime.setText(datetime);
            mTextConent.setText(content);
            mTextSendTo.setText(contacts.toString());


        }
    }
}
