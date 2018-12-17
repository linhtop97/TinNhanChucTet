package thiepchuctet.tinnhanchuctet.tetnguyendan.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import thiepchuctet.tinnhanchuctet.tetnguyendan.R;
import thiepchuctet.tinnhanchuctet.tetnguyendan.databinding.ItemMessageBinding;
import thiepchuctet.tinnhanchuctet.tetnguyendan.listeners.OnItemClickListener;
import thiepchuctet.tinnhanchuctet.tetnguyendan.models.Message;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private OnItemClickListener mListener;
    private List<Message> mMessages;
    private Context mContext;
    private int mNum = 1;

    public MessageAdapter(Context context, List<Message> messages) {
        mContext = context;
        mMessages = messages;
    }

    public void setOnItemClick(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMessageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_message, parent, false);
        return new MessageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bindData(mMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return mMessages != null ? mMessages.size() : 0;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        private ItemMessageBinding mBinding;

        private MessageViewHolder(ItemMessageBinding itemView) {
            super(itemView.getRoot());
            mBinding = itemView;

        }

        private void bindData(Message message) {
            if (message == null) {
                return;
            }
            mBinding.txtMsgNum.setText(mContext.getResources().getString(R.string.num_of_msg).concat(" " + mNum));
            mNum++;
            mBinding.setMessage(message);
            mBinding.setListener(mListener);
            mBinding.executePendingBindings();
        }
    }
}
