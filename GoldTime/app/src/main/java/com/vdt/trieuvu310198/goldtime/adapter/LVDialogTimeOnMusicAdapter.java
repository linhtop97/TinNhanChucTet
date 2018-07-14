package com.vdt.trieuvu310198.goldtime.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.vdt.trieuvu310198.goldtime.R;
import com.vdt.trieuvu310198.goldtime.model.ModelLVDiaLogAmBao;

import java.util.List;

//sử dụng lại item, item listview cua music

public class LVDialogTimeOnMusicAdapter extends ArrayAdapter<ModelLVDiaLogAmBao> {
    private List<ModelLVDiaLogAmBao> listTimeOn;
    private LayoutInflater inflater;
    private OnDialogListClickListener mlistener;

    public LVDialogTimeOnMusicAdapter(@NonNull Context context, int resource, @NonNull List<ModelLVDiaLogAmBao> objects,
                                      OnDialogListClickListener listener) {
        super(context, resource, objects);
        this.listTimeOn = objects;
        this.mlistener = listener;
        this.inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LVDialogTimeOnMusicAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new LVDialogTimeOnMusicAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.item_listview_am_bao, parent, false);
            holder.nameTimeOn = convertView.findViewById(R.id.txt_name_item_am_bao);
            holder.rbMusic = convertView.findViewById(R.id.rb_item_am_bao);
            convertView.setTag(holder);

        } else {
            holder = (LVDialogTimeOnMusicAdapter.ViewHolder) convertView.getTag();

        }
        final ModelLVDiaLogAmBao modelLVDiaLogAmBao = listTimeOn.get(position);
        holder.nameTimeOn.setText(modelLVDiaLogAmBao.getNameMusic());
        holder.rbMusic.setChecked(modelLVDiaLogAmBao.isIscheckedMusic());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onItemClick(position);
            }
        });
        return convertView;
    }



    public static class ViewHolder {
        public TextView nameTimeOn;
        public RadioButton rbMusic;
    }

    public interface OnDialogListClickListener {
        void onItemClick(int position);
    }
}
