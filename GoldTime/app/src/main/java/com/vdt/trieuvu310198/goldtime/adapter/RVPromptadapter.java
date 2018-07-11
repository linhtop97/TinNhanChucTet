package com.vdt.trieuvu310198.goldtime.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vdt.trieuvu310198.goldtime.R;
import com.vdt.trieuvu310198.goldtime.model.PromptModel;

import java.util.ArrayList;
import java.util.List;

public class RVPromptadapter extends RecyclerView.Adapter<RVPromptadapter.ViewHolder>{
    private List<PromptModel> listPrompt;
    private Context context;
    private OnItemClickListener mListener;
    private OnCLickListener mOnclick;
    private List<String> listBackground;
    private List<Integer> listIcon;

    public RVPromptadapter(List<PromptModel> listPrompt, Context context, OnItemClickListener listener, OnCLickListener onClick) {
        this.mListener = listener;
        this.listPrompt = listPrompt;
        this.context = context;
        this.mOnclick = onClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_prompt, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        dataCodeColor();
        dataIcon();
        PromptModel promptModel = listPrompt.get(position);
        holder.rlbackground.setBackgroundDrawable(new ColorDrawable(promptModel.getBackground()));
        holder.imgIcon.setImageResource(listIcon.get(promptModel.getIcon()).intValue());
        holder.tvNote.setText(promptModel.getNote());
        holder.tvDate.setText(promptModel.getCalendar());
        holder.tvTime.setText(promptModel.getTime());
        holder.icPopupMenu.setImageResource(promptModel.getIcpopup());

        holder.icPopupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnclick.onClick(view, position);
            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(view, position);
            }
        });
    }



    @Override
    public int getItemCount() {
        return listPrompt.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout rlbackground;
        public ImageView imgIcon;
        public TextView tvNote;
        public TextView tvDate;
        public TextView tvTime;
        public ImageView icPopupMenu;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            rlbackground = itemView.findViewById(R.id.rl_background);
            imgIcon = itemView.findViewById(R.id.img_icon);
            tvNote = itemView.findViewById(R.id.tv_note);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTime = itemView.findViewById(R.id.tv_time);
            icPopupMenu = itemView.findViewById(R.id.ic_popup_menu);
            view = itemView;
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }


    public interface OnCLickListener {
        public void onClick(View view, int position);
    }

    private void dataCodeColor() {
        listBackground = new ArrayList<>();
        listBackground.add(new String("#FFFFFFFF"));
        listBackground.add(new String("#FFFFFF00"));
        listBackground.add(new String("#FF57F414"));
        listBackground.add(new String("#3153e9"));
        listBackground.add(new String("#f25ae8"));
        listBackground.add(new String("#FFDB2B31"));
        listBackground.add(new String("#FF5AF2E8"));
        listBackground.add(new String("#FFFFFFFF"));
        listBackground.add(new String("#FFFFFF00"));
        listBackground.add(new String("#FF57F414"));
        listBackground.add(new String("#3153e9"));
        listBackground.add(new String("#f25ae8"));
        listBackground.add(new String("#FFDB2B31"));
        listBackground.add(new String("#FF5AF2E8"));
        listBackground.add(new String("#FFFFFFFF"));
        listBackground.add(new String("#FFFFFF00"));
        listBackground.add(new String("#FF57F414"));
        listBackground.add(new String("#3153e9"));
        listBackground.add(new String("#f25ae8"));
        listBackground.add(new String("#FFDB2B31"));
        listBackground.add(new String("#FF5AF2E8"));
        listBackground.add(new String("#FFFFFFFF"));
        listBackground.add(new String("#FFFFFF00"));
        listBackground.add(new String("#FF57F414"));
        listBackground.add(new String("#3153e9"));
        listBackground.add(new String("#f25ae8"));
        listBackground.add(new String("#FFDB2B31"));
        listBackground.add(new String("#FF5AF2E8"));

    }
    private void dataIcon() {
        listIcon = new ArrayList<>();
        listIcon.add(new Integer(R.drawable.icon_bell_off));
        listIcon.add(new Integer(R.drawable.ic_menu_gallery));
        listIcon.add(new Integer(R.drawable.ic_menu_camera));
        listIcon.add(new Integer(R.drawable.ic_menu_send));
        listIcon.add(new Integer(R.drawable.ic_trash));
        listIcon.add(new Integer(R.drawable.ic_menu_share));
        listIcon.add(new Integer(R.drawable.icon_bell_off));
        listIcon.add(new Integer(R.drawable.ic_menu_gallery));
        listIcon.add(new Integer(R.drawable.ic_menu_camera));
        listIcon.add(new Integer(R.drawable.ic_menu_send));
        listIcon.add(new Integer(R.drawable.ic_trash));
        listIcon.add(new Integer(R.drawable.ic_menu_share));
        listIcon.add(new Integer(R.drawable.icon_bell_off));
        listIcon.add(new Integer(R.drawable.ic_menu_gallery));
        listIcon.add(new Integer(R.drawable.ic_menu_camera));
        listIcon.add(new Integer(R.drawable.ic_menu_send));
        listIcon.add(new Integer(R.drawable.ic_trash));
        listIcon.add(new Integer(R.drawable.ic_menu_share));
        listIcon.add(new Integer(R.drawable.icon_bell_off));
        listIcon.add(new Integer(R.drawable.ic_menu_gallery));
        listIcon.add(new Integer(R.drawable.ic_menu_camera));
        listIcon.add(new Integer(R.drawable.ic_menu_send));
        listIcon.add(new Integer(R.drawable.ic_trash));
        listIcon.add(new Integer(R.drawable.ic_menu_share));
    }
}
