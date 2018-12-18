package thiepchuctet.tinnhanchuctet.tetnguyendan.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import thiepchuctet.tinnhanchuctet.tetnguyendan.R;
import thiepchuctet.tinnhanchuctet.tetnguyendan.listeners.OnItemClickListener;
import thiepchuctet.tinnhanchuctet.tetnguyendan.listeners.OnItemLongClickListener;
import thiepchuctet.tinnhanchuctet.tetnguyendan.models.Message;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private OnItemClickListener mListener;
    private OnItemLongClickListener<Message> mLongClickListener;
    private List<Message> mMessages;
    private Context mContext;

    public MessageAdapter(Context context, List<Message> messages) {
        mContext = context;
        mMessages = messages;
    }

    public void setOnItemClick(OnItemClickListener listener) {
        mListener = listener;
    }

    public void setOnItemLongClick(OnItemLongClickListener listener) {
        mLongClickListener = listener;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bindData(mMessages.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mMessages != null ? mMessages.size() : 0;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewNum;
        private TextView mTextViewContent;
        private int mPos;

        public MessageViewHolder(View itemView) {
            super(itemView);
            mTextViewNum = itemView.findViewById(R.id.txt_num);
            mTextViewContent = itemView.findViewById(R.id.txt_content);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(getAdapterPosition());
                }
            });
            if (mLongClickListener != null) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        mLongClickListener.onItemLongClick(mMessages.get(getAdapterPosition()));
                        return true;
                    }
                });
            }

        }

        private void bindData(Message message, int position) {
            if (message == null) {
                return;
            }
            mTextViewNum.setText(" " + (position + 1));
            mTextViewContent.setText(message.getContent());
        }
    }
}
