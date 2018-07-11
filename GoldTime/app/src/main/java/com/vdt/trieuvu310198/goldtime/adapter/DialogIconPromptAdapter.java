package com.vdt.trieuvu310198.goldtime.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vdt.trieuvu310198.goldtime.R;

import java.util.List;

public class DialogIconPromptAdapter extends RecyclerView.Adapter<DialogIconPromptAdapter.ViewHolder> {
    private List<Integer> listIcon;
    private Context context;
    private OnItemClickListener mlistener;

    public DialogIconPromptAdapter(List<Integer> listIcon, Context context, OnItemClickListener listener) {
        this.listIcon = listIcon;
        this.context = context;
        this.mlistener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_icon_dialog_prompt, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Integer icon = listIcon.get(position);
        holder.imgIcon.setImageResource(icon.intValue());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlistener.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listIcon.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgIcon;
        public View view;
        public ViewHolder(View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.item_icon);
            view = itemView;

        }
    }
    public interface OnItemClickListener {
        public  void onItemClick(View view, int position);
    }
}
