package com.tinnhanchuctet.loichuchay.chuctet.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tinnhanchuctet.loichuchay.chuctet.R;
import com.tinnhanchuctet.loichuchay.chuctet.listeners.OnItemClickListener;
import com.tinnhanchuctet.loichuchay.chuctet.listeners.OnItemLongClickListener;
import com.tinnhanchuctet.loichuchay.chuctet.models.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener<Message> mOnItemLongClickListener;
    private List<Message> mMessageList;
    private Context mCxt;

    public MessageAdapter(Context context, List<Message> messages) {
        mCxt = context;
        mMessageList = messages;
    }

    public void setOnItemClick(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setOnItemLongClick(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bindData(mMessageList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mMessageList != null ? mMessageList.size() : 0;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView mTxtNum;
        private TextView mTxtContent;

        public MessageViewHolder(View itemView) {
            super(itemView);
            mTxtNum = itemView.findViewById(R.id.txt_title_num);
            mTxtContent = itemView.findViewById(R.id.txt_content);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(getAdapterPosition());
                }
            });
            if (mOnItemLongClickListener != null) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        mOnItemLongClickListener.onItemLongClick(mMessageList.get(getAdapterPosition()));
                        return true;
                    }
                });
            }

        }

        private void bindData(Message message, int position) {
            if (message == null) {
                return;
            }
            Typeface font = Typeface.createFromAsset(mCxt.getAssets(), "fonts/font_tieude.otf");
            mTxtNum.setTypeface(font);
            mTxtNum.setText("Lời chúc " + (position + 1));
            mTxtContent.setText(message.getContent());
        }
    }
}
