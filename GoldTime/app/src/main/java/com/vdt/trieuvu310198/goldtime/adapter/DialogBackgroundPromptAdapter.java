package com.vdt.trieuvu310198.goldtime.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.vdt.trieuvu310198.goldtime.R;

import java.util.List;

public class DialogBackgroundPromptAdapter extends RecyclerView.Adapter<DialogBackgroundPromptAdapter.ViewHolder> {
    private Context context;
    private List<String> listCodeColor;
    private OnItemClickListener mlistener;

    public DialogBackgroundPromptAdapter(Context context, List<String> listCodeColor, OnItemClickListener mlistener) {
        this.context = context;
        this.listCodeColor = listCodeColor;
        this.mlistener = mlistener;
    }

    @NonNull
    @Override
    public DialogBackgroundPromptAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_background_prompt, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DialogBackgroundPromptAdapter.ViewHolder holder, final int position) {
        String str = listCodeColor.get(position);
        holder.rlBackground.setBackgroundColor(Color.parseColor(str.toString()));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlistener.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCodeColor.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rlBackground;
        public View view;
        public ViewHolder(View itemView) {
            super(itemView);
            rlBackground = itemView.findViewById(R.id.rl_background);
            view = itemView;
        }
    }

    public interface OnItemClickListener {
        public  void onItemClick(View view, int position);
    }
}
